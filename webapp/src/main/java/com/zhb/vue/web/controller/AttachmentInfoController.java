package com.zhb.vue.web.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.solr.client.solrj.SolrQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhb.forever.framework.Constants;
import com.zhb.forever.framework.dic.AttachmentTypeEnum;
import com.zhb.forever.framework.dic.DeleteFlagEnum;
import com.zhb.forever.framework.dic.LikeDgreeEnum;
import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.page.PageUtil;
import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.DetectFaceUtil;
import com.zhb.forever.framework.util.DownloadUtil;
import com.zhb.forever.framework.util.EncodeUtil;
import com.zhb.forever.framework.util.FileUtil;
import com.zhb.forever.framework.util.ImageUtil;
import com.zhb.forever.framework.util.File2HtmlConvert;
import com.zhb.forever.framework.util.PropertyUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.util.UploadUtil;
import com.zhb.forever.framework.vo.ImageVO;
import com.zhb.forever.framework.vo.KeyValueVO;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.forever.search.SearchFactory;
import com.zhb.forever.search.solr.client.SolrClient;
import com.zhb.forever.search.solr.param.AttachmentInfoSolrIndexParam;
import com.zhb.forever.search.solr.vo.AttachmentInfoSolrData;
import com.zhb.vue.params.AttachmentInfoParam;
import com.zhb.vue.params.Param2SolrIndexParam;
import com.zhb.vue.pojo.AttachmentInfoData;
import com.zhb.vue.pojo.UserInfoData;
import com.zhb.vue.service.AttachmentInfoService;
import com.zhb.vue.service.UserInfoService;
import com.zhb.vue.thread.solr.AttachmentInfo2SolrIndexThread;
import com.zhb.vue.util.Data2VO;
import com.zhb.vue.web.util.Data2JSONUtil;
import com.zhb.vue.web.util.FlushSessionUtil;
import com.zhb.vue.web.util.WebAppUtil;

@Controller
@RequestMapping("/htgl/attachmentinfocontroller")
public class AttachmentInfoController {
    
    private Logger logger = LoggerFactory.getLogger(AttachmentInfoController.class);
    
    @Autowired
    private AttachmentInfoService attachmentInfoService;
    @Autowired
    private UserInfoService userInfoService;
    
    private SolrClient solrClient = SearchFactory.getSolrClientBean();
    
    //toindex
    @RequestMapping(value = "/toindex",method = RequestMethod.GET)
    @Transactional
    public String toIndex(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return "login/login";
        }
        return "htgl/attachment/index";
    }
    
   //toswiper 浏览图片
    @RequestMapping(value = "/toswiper",method = RequestMethod.GET)
    @Transactional
    public String toSwiper(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return "login/login";
        }
        return "htgl/attachment/swiper";
    }
    
    
    //查询
    @RequestMapping(value = "/getattachmentinfo/api")
    @ResponseBody
    @Transactional
    public AjaxData getAttachmentInfo(HttpServletRequest request,HttpServletResponse response,AttachmentInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        ajaxData = searchAttachmentInfo2AjaxData(param, request);
        return ajaxData;
    }
    
    //查询,分页
    @RequestMapping(value = "/getattachmentinfopage/api")
    @ResponseBody
    @Transactional
    public AjaxData getAttachmentInfoPage(HttpServletRequest request,HttpServletResponse response,AttachmentInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        
        AttachmentInfoSolrIndexParam params = Param2SolrIndexParam.attachmentParam2SolrIndexParam(param);
        ajaxData = searchAttachmentInfoSolrIndex2AjaxDataPage(params);
        
        //ajaxData = searchAttachmentInfo2AjaxDataPage(param);
        return ajaxData;
    }
    
    //浏览图片，分页
    @RequestMapping(value = "/scanattachmentinfopage/api")
    @ResponseBody
    @Transactional
    public AjaxData scanAttachmentInfoPage(HttpServletRequest request,HttpServletResponse response,AttachmentInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        param.setType(AttachmentTypeEnum.YELLOW.getIndex());
        AttachmentInfoSolrIndexParam params = Param2SolrIndexParam.attachmentParam2SolrIndexParam(param);
        ajaxData = scanAttachmentInfoSolrIndex2AjaxDataPage(params);
        
        //ajaxData = scanAttachmentInfo2AjaxDataPage(param);
        
        return ajaxData;
    }
    
    //修改偏爱程度
    @RequestMapping(value = "/updateattachmentinfo/api")
    @ResponseBody
    @Transactional
    public AjaxData updateAttachmentInfo(HttpServletRequest request,HttpServletResponse response,AttachmentInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        if (StringUtil.isBlank(param.getId())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请选择数据");
            return ajaxData;
        }
        
        AttachmentInfoData data = attachmentInfoService.get(AttachmentInfoData.class, param.getId());
        if (null == data) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("没有这个数据");
            return ajaxData;
        }
        
        if (null == param.getLikeDegree()) {
            param.setLikeDegree(LikeDgreeEnum.LIKE.getIndex());
        }else {
            if (LikeDgreeEnum.LIKE.getIndex() > 3 || LikeDgreeEnum.LIKE.getIndex() < 1) {
                ajaxData.setFlag(false);
                ajaxData.addMessage("不在喜爱程度范围内，请刷新页面再进行操作");
                return ajaxData;
            }
        }
        
        data.setLikeDegree(param.getLikeDegree());
        
        attachmentInfoService.updateEntitie(data);
        
        AttachmentInfo2SolrIndexThread.createAttachmentSolrIndex(data);
        
        /*param.setId(null);
        param.setLikeDegree(null);
        AttachmentInfoSolrIndexParam params = param2SolrIndexParam.attachmentParam2SolrIndexParam(param);
        ajaxData = searchAttachmentInfoSolrIndex2AjaxDataPage(params);*/
        
        //ajaxData = searchAttachmentInfo2AjaxDataPage(param);
        ajaxData.setFlag(true);
        JSONObject object = new JSONObject();
        object.put("likeDegree", data.getLikeDegree());
        object.put("likeDegreeName", LikeDgreeEnum.getName(data.getLikeDegree()));
        ajaxData.setData(object);
        return ajaxData;
    }
    
    //删除
    @RequestMapping(value = "/deleteattachmentinfo/api")
    @ResponseBody
    @Transactional
    public AjaxData deleteAttachmentInfo(HttpServletRequest request,HttpServletResponse response,AttachmentInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        if (StringUtil.isBlank(param.getId())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请选择待删除的数据");
            return ajaxData;
        }
        
        AttachmentInfoData data = attachmentInfoService.get(AttachmentInfoData.class, param.getId());
        if (null == data) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("没有待删除的数据");
            return ajaxData;
        }
        
        File origin = new File(data.getFilePath());
        if (origin.exists()) {
            origin.delete();
        }
        
        if (StringUtil.isNotBlank(data.getThumbnailPath())) {
            File thumbnail = new File(data.getThumbnailPath());
            if (thumbnail.exists()) {
                thumbnail.delete();
            }
        }
        
        attachmentInfoService.delete(data);
        
        List<String> ids = new ArrayList<>();
        ids.add(data.getId());
        AttachmentInfo2SolrIndexThread.deleteAttachmentSolrIndex(ids);
        
        /*AttachmentInfoSolrIndexParam params = param2SolrIndexParam.attachmentParam2SolrIndexParam(param);
        ajaxData = searchAttachmentInfoSolrIndex2AjaxDataPage(params);*/
        ajaxData = searchAttachmentInfo2AjaxDataPage(param);
        return ajaxData;
    }
    
    //toupload
    @RequestMapping(value = "/toupload",method = RequestMethod.GET)
    @Transactional
    public String toUpload(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return "login/login";
        }
        return "htgl/attachment/upload";
    }
    
    //上传
    @RequestMapping(value = "/uploadattachmentinfo")
    @ResponseBody
    @Transactional
    public AjaxData uploadAttachmentInfo(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件
        MultipartFile multipartFile = multipartRequest.getFile("upFile");
        if (null == multipartFile) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请选择待上传的附件");
            return ajaxData;
        }
        //文件大小
        Long fileSize = multipartFile.getSize();
        if (fileSize > Constants.FILE_MAX_SIZE) {
            ajaxData.setFlag(false);
            ajaxData.addMessage(multipartFile.getOriginalFilename() + " 文件大小不能超过" + Constants.FILE_MAX_SIZE_MB);
            return ajaxData;
        }
        //文件内容类型
        String contentType = multipartFile.getContentType();
        //文件名字
        String fileName = multipartFile.getOriginalFilename();
        Integer type = AttachmentTypeEnum.geTypeEnum(fileName).getIndex();
        
        //原始文件保存路径
        String filePath = PropertyUtil.getUploadPath();
        
        File fileUpload = new File(filePath);
        if (!fileUpload.exists()) {
            fileUpload.mkdirs();
        }
        String fileTempName = FileUtil.randomName() + fileName.substring(fileName.indexOf("."));
        String uploadPath = fileUpload + File.separator + fileTempName;
        
        File file = new File(uploadPath);
        try {
            UploadUtil.inputStream2File(multipartFile.getInputStream(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String uploadThumbnailPath = null;
        if (1 ==  type) {//图片时，才有缩略图
            if (fileSize > Constants.SMALL_IMAGE_SIZE) {
                try {
                    uploadThumbnailPath = UploadUtil.uploadThumbmail(multipartFile.getInputStream(), fileTempName, ImageUtil.getImageSuffix(file), fileSize);
                } catch (IOException e) {
                    e.printStackTrace();
                    uploadThumbnailPath = uploadPath;
                }
            }else {
                uploadThumbnailPath = uploadPath;
            }
        }
        
        AttachmentInfoData fileInfoData = new AttachmentInfoData();
        fileInfoData.setFileName(fileName);
        fileInfoData.setFilePath(uploadPath);
        fileInfoData.setThumbnailPath(uploadThumbnailPath);
        fileInfoData.setFileSize(String.valueOf(fileSize));
        fileInfoData.setContentType(contentType);
        fileInfoData.setType(AttachmentTypeEnum.geTypeEnum(fileName).getIndex());
        fileInfoData.setCreateUserId(WebAppUtil.getUserId(request));
        fileInfoData.setLikeDegree(LikeDgreeEnum.UN_LIKE.getIndex());
        attachmentInfoService.saveOrUpdate(fileInfoData);
        
        AttachmentInfo2SolrIndexThread.createAttachmentSolrIndex(fileInfoData);
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //获取附件
    @RequestMapping(value = "/downloadattachmentinfo")
    @Transactional
    public void downloadAttachmentInfo(HttpServletRequest request,HttpServletResponse response,String id) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return;
        }
        
        if(StringUtil.isBlank(id)) {
            return;
        }
        
        AttachmentInfoData data = attachmentInfoService.getAttachmentInfoById(id);
        if (null == data ){
            return;
        }
        File file = new File(data.getFilePath());
        if (!file.exists()) {
            attachmentInfoService.delete(data);
            return;
        }
        try {
            DownloadUtil.processBeforeDownload(request, response, data.getContentType(), data.getFileName());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        DownloadUtil.downloadAttachment(request, response, data.getFilePath());
    }
    
    //获取原图
    @RequestMapping(value = "/getoriginalattachmentinfo")
    @Transactional
    public void getOriginalAttachmentInfo(HttpServletRequest request,HttpServletResponse response,String id) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            String rootPath = WebAppUtil.getRootPath(request);
            String imagePath = rootPath + "images" + File.separator + "loading.gif";
            response.setContentType("image/jpeg");
            DownloadUtil.downloadAttachment(request, response, imagePath);
            return;
        }
        
        if(StringUtil.isBlank(id)) {
            String rootPath = WebAppUtil.getRootPath(request);
            String imagePath = rootPath + "images" + File.separator + "loading.gif";
            response.setContentType("image/jpeg");
            DownloadUtil.downloadAttachment(request, response, imagePath);
            return;
        }
        
        AttachmentInfoData data = attachmentInfoService.getAttachmentInfoById(id);
        if (null == data || (AttachmentTypeEnum.IMAGE.getIndex() != data.getType() && AttachmentTypeEnum.YELLOW.getIndex() != data.getType())){
            String rootPath = WebAppUtil.getRootPath(request);
            String imagePath = rootPath + "images" + File.separator + "loading.gif";
            response.setContentType("image/jpeg");
            DownloadUtil.downloadAttachment(request, response, imagePath);
            return;
        }
        
        // will return -1 if no header...(没缓存的照片时no header)
        long clientLastModified = request.getDateHeader("If-Modified-Since");
        if (clientLastModified != -1) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        
        try {
            //在浏览器缓存30天
            DownloadUtil.processExpiresTime(response);
            DownloadUtil.processBeforeDownload(request, response, data.getContentType(), data.getFileName());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        File file = new File(data.getFilePath());
        if (!file.exists()) {
            attachmentInfoService.delete(data);
            return ;
        }
        
        //加水印 下载
        DownloadUtil.downloadAttachmentWithWaterPrint(request, response, data.getFilePath(), data.getContentType().contains("gif"));
        
    }
    
    //获取缩略图
    @RequestMapping(value = "/getthumbnailattachmentinfo")
    @Transactional
    public void getThumbnailAttachmentInfo(HttpServletRequest request,HttpServletResponse response,String id) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            String rootPath = WebAppUtil.getRootPath(request);
            String imagePath = rootPath + "images" + File.separator + "loading.gif";
            response.setContentType("image/jpeg");
            DownloadUtil.downloadAttachment(request, response, imagePath);
            return;
        }
        
        if(StringUtil.isBlank(id)) {
            String rootPath = WebAppUtil.getRootPath(request);
            String imagePath = rootPath + "images" + File.separator + "loading.gif";
            response.setContentType("image/jpeg");
            DownloadUtil.downloadAttachment(request, response, imagePath);
            return;
        }
        
        AttachmentInfoData data = attachmentInfoService.getAttachmentInfoById(id);
        if (null == data || (AttachmentTypeEnum.IMAGE.getIndex() != data.getType()&& AttachmentTypeEnum.YELLOW.getIndex() != data.getType())){
            String rootPath = WebAppUtil.getRootPath(request);
            String imagePath = rootPath + "images" + File.separator + "loading.gif";
            response.setContentType("image/jpeg");
            DownloadUtil.downloadAttachment(request, response, imagePath);
            return;
        }
        
        long clientLastModified = request.getDateHeader("If-Modified-Since");
        if (clientLastModified != -1) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        
        try {
            //在浏览器缓存30天
            DownloadUtil.processExpiresTime(response);
            DownloadUtil.processBeforeDownload(request, response, data.getContentType(), data.getFileName());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        File file = new File(data.getThumbnailPath());
        if (!file.exists()) {
            DownloadUtil.downloadAttachment(request, response, data.getFilePath());
        }else {
            DownloadUtil.downloadAttachment(request, response, data.getThumbnailPath());
        }
        
        
    }
    
    //上传头像 获取layer
    @RequestMapping("/getlayercontent/api")
    @Transactional
    @ResponseBody
    public AjaxData layerContent(HttpServletRequest request, HttpServletResponse response,String image,String id) {
        AjaxData ajaxData = new AjaxData();

        /*可以将以下内容 整理到jsp中*/
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"box\">");
        sb.append(      image);
        sb.append(     "<input id=\"userId\" type=\"hidden\" value='" + id + "' >");
        sb.append("</div>");
        sb.append("<div align=\"center\" style=\"margin-top:20px;\" > ");
        sb.append(     "<button id=\"cut_upload\" type=\"button\" class=\"layui-btn\" >");
        sb.append(     "上传</button>");
        sb.append(     "<button id=\"cut_cancle\" type=\"button\" class=\"layui-btn\" >");
        sb.append(     "取消</button>");
        sb.append("</div>");
        sb.append("<script type=\"text/javascript\">");
        sb.append("     $('#new_me_image').cropper({");
        sb.append("             aspectRatio: 1 / 1,");
        sb.append("             viewMode:1,");
        sb.append("             crop: function (e) {");
        sb.append("                 ");
        sb.append("             }");
        sb.append("     });");
        sb.append("     $(\"#cut_upload\").on(\"click\", function () {");
        sb.append("          var image_target = $('#new_me_image').cropper('getData', true); ");
        sb.append("          var image_content = $('#new_me_image').attr('src');");
        sb.append("          var userId = $('#userId').val();");
        sb.append("          var data = {");
        sb.append("                         'id':userId,");
        sb.append("                         'image_content':image_content,");
        sb.append("                         'image_target':image_target,");
        sb.append("                         'x':image_target.x,");
        sb.append("                         'y':image_target.y,");
        sb.append("                         'width':image_target.width,");
        sb.append("                         'height':image_target.height");
        sb.append("          };");

        sb.append("          $.ajax({");
        sb.append("             url: '/htgl/attachmentinfocontroller/uploadHeadPhoto/api',");
        sb.append("             type: 'POST',");
        sb.append("             data:data,");
        sb.append("             success: function (result) { ");
        sb.append("                 layer.closeAll(); ");
        sb.append("                 window.location.reload() ; ");
        sb.append("             }, ");
        sb.append("             error: function (result) {");
        sb.append("                 layer.closeAll(); ");
        sb.append("             }");
        sb.append("          });");

        sb.append("     });");
        sb.append("     $(\"#cut_cancle\").on(\"click\", function () {");
        sb.append("          layer.closeAll(); ");
        sb.append("          window.location.reload() ; ");
        sb.append("     });");
        sb.append("</script>");

        ajaxData.setFlag(true);
        ajaxData.setData(sb.toString());
        return ajaxData;
    }
    
    //上传头像
    @RequestMapping("/uploadheadphoto/api")
    @ResponseBody
    @Transactional
    public AjaxData uploadHeadPhoto(HttpServletRequest request, HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        
        String userId = request.getParameter("userId");
        if (StringUtil.isBlank(WebAppUtil.getUserId(request)) || StringUtil.isBlank(userId) || !userId.equals(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("非法访问");
            return ajaxData;
        }
        
        UserInfoData userInfoData = userInfoService.getUserInfoById(userId);
        if (null == userInfoData) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("非法访问");
            return ajaxData;
        }
        
       // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件
        MultipartFile multipartFile = multipartRequest.getFile("upFile");
        if (null == multipartFile) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请选择待上传的附件");
            return ajaxData;
        }
        //文件内容类型
        String contentType = multipartFile.getContentType();
        //文件名字
        String originalFileName = multipartFile.getOriginalFilename();
        
        
        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        ImageVO imageVO = new ImageVO(Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(width), Integer.valueOf(height), 0);
        imageVO.setSuffix(originalFileName.substring(originalFileName.indexOf(".")+1));
        
        byte[] decodedBytes = null;
        
        try {
            InputStream is = multipartFile.getInputStream();
            decodedBytes = FileUtil.readInputStreamAsBytes(is);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        decodedBytes = ImageUtil.getCropPhotoBytes(imageVO,new ByteArrayInputStream(decodedBytes) );
        
        int num = DetectFaceUtil.getPersonNum(decodedBytes);
        if (0 == num) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("图片必须包含人脸，占比不能太小");
            return ajaxData;
        }
        if (num > 1) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("图片不能有多个人脸，只能包含一个人脸");
            return ajaxData;
        }

        //文件保存路径
        String filePath = PropertyUtil.getUploadPath();
        
        File fileUpload = new File(filePath);
        if (!fileUpload.exists()) {
            fileUpload.mkdirs();
        }
        String fileName = userInfoData.getUserName() + "_" + FileUtil.randomName() + originalFileName.substring(originalFileName.indexOf("."));
        String uploadPath = fileUpload + File.separator + fileName;
        
        File file = new File(uploadPath);
        OutputStream licOutput = null;
        try {
            licOutput = new FileOutputStream(file);
            licOutput.write(decodedBytes);
            licOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != licOutput) {
                try {
                    licOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        
        AttachmentInfoData fileInfoData = new AttachmentInfoData();
        fileInfoData.setFileName(fileName);
        fileInfoData.setFilePath(uploadPath);
        fileInfoData.setThumbnailPath(uploadPath);
        fileInfoData.setFileSize(String.valueOf(decodedBytes.length));
        fileInfoData.setContentType(contentType);
        fileInfoData.setType(AttachmentTypeEnum.IMAGE.getIndex());
        fileInfoData.setCreateUserId(userId);
        attachmentInfoService.saveOrUpdate(fileInfoData);
        
        if (StringUtil.isNotBlank(userInfoData.getLobId())) {
            AttachmentInfoData oldData = attachmentInfoService.getAttachmentInfoById(userInfoData.getLobId());
            if (null != oldData) {
                oldData.setDeleteFlag(DeleteFlagEnum.DEL.getIndex());
                attachmentInfoService.saveOrUpdate(oldData);
            }
        }
        userInfoData.setLobId(fileInfoData.getId());
        userInfoService.saveOrUpdate(userInfoData);
        
        //刷新用户缓存
        FlushSessionUtil.flushWebAppUserInfo(request,Data2VO.userInfoDat2VO(userInfoData));
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //附件类型
    @RequestMapping(value = "/getattachmenttype/api")
    @ResponseBody
    @Transactional
    public AjaxData getAttachmentType(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        List<KeyValueVO> vos = AttachmentTypeEnum.getAll();
        ajaxData.setFlag(true);
        ajaxData.setData(vos);
        return ajaxData;
    }
    
    //附件类型
    @ModelAttribute("attachmentTypeList")
    public List<KeyValueVO> algorithmTypeList() {
        return AttachmentTypeEnum.getAll();
    }
    
    //读取附件内容
    @RequestMapping(value = "/readfile/api")
    @Transactional
    @ResponseBody
    public AjaxData readFile(HttpServletRequest request,HttpServletResponse response,String id) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        
        if(StringUtil.isBlank(id)) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("文件不存在");
            return ajaxData;
        }
        
        AttachmentInfoData data = attachmentInfoService.getAttachmentInfoById(id);
        if (null == data ){
            ajaxData.setFlag(false);
            ajaxData.addMessage("文件不存在");
            return ajaxData;
        }
        File file = new File(data.getFilePath());
        if (!file.exists()) {
            attachmentInfoService.delete(data);
            ajaxData.setFlag(false);
            ajaxData.addMessage("文件不存在");
            return ajaxData;
        }
        
        try {
            String result = "";
            String fileName = data.getFileName();
            if (fileName.contains("docx")) {
                result = File2HtmlConvert.docx2Html(new FileInputStream(file), EncodeUtil.getUtf8(), PropertyUtil.getTempDownloadPath());
            }else if (fileName.contains("doc")) {
                result = File2HtmlConvert.doc2Html(new FileInputStream(file), EncodeUtil.getUtf8(), PropertyUtil.getTempDownloadPath());
            }else if (fileName.contains("xlsx")) {
                byte[] bytes = FileUtil.readFileAsBytes(file);
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                result = File2HtmlConvert.xls2Html(bais, "xlsx", EncodeUtil.getUtf8());
            }else if (fileName.contains("xls")) {
                byte[] bytes = FileUtil.readFileAsBytes(file);
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                result = File2HtmlConvert.xls2Html(bais, "xls", EncodeUtil.getUtf8());
            }else if(fileName.contains("txt")) {
                result = File2HtmlConvert.readTxt2Html(file);
            }else if (fileName.contains("ppt")) {
                result = File2HtmlConvert.readPPT2String(file);
            }else if (fileName.contains("pdf")) {
                result = File2HtmlConvert.readPDF2String(file);
            }
            ajaxData.setFlag(true);
            ajaxData.setData(result);
            return ajaxData;
        } catch (IOException | TransformerException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        
        ajaxData.setFlag(false);
        ajaxData.addMessage("读取失败");
        return ajaxData;
    }
    
    //共用查询,不分页
    private AjaxData searchAttachmentInfo2AjaxData(AttachmentInfoParam param,HttpServletRequest request) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new AttachmentInfoParam();
        }
        param.setId(null);
        param.setLikeDegree(null);
        //排序字段
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo = new OrderVO("category",true);
        orderVos.add(vo);
        OrderVO vo2 = new OrderVO("orderIndex",true);
        orderVos.add(vo2);
        
        List<AttachmentInfoData> attachmentInfos = attachmentInfoService.getAttachmentInfos(param,orderVos);
        if (null != attachmentInfos) {
            ajaxData.setData(Data2JSONUtil.attachmentInfoDatas2JSONArray(attachmentInfos));
        }
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //共用查询,分页
    private AjaxData searchAttachmentInfo2AjaxDataPage(AttachmentInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new AttachmentInfoParam();
        }
        param.setId(null);
        param.setLikeDegree(null);
        //排序字段
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo1 = new OrderVO("likeDegree",false);
        orderVos.add(vo1);
        OrderVO vo2 = new OrderVO("fileName",true);
        orderVos.add(vo2);
        OrderVO vo3 = new OrderVO("type",true);
        orderVos.add(vo3);
        
        //设置分页信息
        if(null == param.getCurrentPage()){
            param.setCurrentPage(1);
        }
        if(null == param.getPageSize()){
            param.setPageSize(PageUtil.PAGE_SIZE);
        }
        param.setStart(param.getPageSize()*(param.getCurrentPage()-1));
        
        Page<AttachmentInfoData> page = attachmentInfoService.getAttachmentInfosPage(param,orderVos);
        JSONObject jsonObject = new JSONObject();
        if (null != page) {
            JSONArray jsonArray = Data2JSONUtil.attachmentInfoDatas2JSONArray(page.getList());
            jsonObject = PageUtil.pageInfo2JSON(page.getTotalCount(), page.getPageCount(), page.getCurrrentPage(), jsonArray);
        }else{
            jsonObject = PageUtil.pageInfo2JSON(0,param.getPageSize(),1,new JSONArray());
        }
        ajaxData.setFlag(true);
        ajaxData.setData(jsonObject);
        return ajaxData;
    }
    
    //共用查询附件索引,分页
    private AjaxData searchAttachmentInfoSolrIndex2AjaxDataPage(AttachmentInfoSolrIndexParam param) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new AttachmentInfoSolrIndexParam();
        }
        //排序字段
        param.addSort("likeDegree",SolrQuery.ORDER.desc);
        param.addSort("fileName",SolrQuery.ORDER.asc);
        param.addSort("type",SolrQuery.ORDER.asc);
        
        //设置分页信息
        if(null == param.getCurrentPage()){
            param.setCurrentPage(1);
        }
        if(null == param.getPageSize()){
            param.setPageSize(PageUtil.PAGE_SIZE);
        }
        param.setStart(param.getPageSize()*(param.getCurrentPage()-1));
        
        Page<AttachmentInfoSolrData> page = solrClient.searchAttachmentsForPage(param);
        JSONObject jsonObject = new JSONObject();
        if (null != page) {
            JSONArray jsonArray = Data2JSONUtil.attachmentInfoSolrIndexDatas2JSONObject(page.getList());
            jsonObject = PageUtil.pageInfo2JSON(page.getTotalCount(), page.getPageCount(), page.getCurrrentPage(), jsonArray);
        }else{
            jsonObject = PageUtil.pageInfo2JSON(0,param.getPageSize(),1,new JSONArray());
        }
        ajaxData.setFlag(true);
        ajaxData.setData(jsonObject);
        return ajaxData;
    }
    
    //浏览图片，分页
    private AjaxData scanAttachmentInfo2AjaxDataPage(AttachmentInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new AttachmentInfoParam();
        }
        param.setId(null);
        param.setLikeDegree(null);
        param.setType(AttachmentTypeEnum.YELLOW.getIndex());
        
        //排序字段
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo1 = new OrderVO("likeDegree",false);
        orderVos.add(vo1);
        OrderVO vo = new OrderVO("fileName",true);
        orderVos.add(vo);
        OrderVO vo2 = new OrderVO("type",true);
        orderVos.add(vo2);
        
        //设置分页信息
        if(null == param.getCurrentPage()){
            param.setCurrentPage(1);
        }
        if(null == param.getPageSize()){
            param.setPageSize(PageUtil.PAGE_SIZE);
        }
        param.setStart(param.getPageSize()*(param.getCurrentPage()-1));
        
        Page<AttachmentInfoData> page = attachmentInfoService.getAttachmentInfosPage(param,orderVos);
        JSONObject jsonObject = new JSONObject();
        if (null != page) {
            JSONArray jsonArray = Data2JSONUtil.imageDatas2JSONArray(page.getList());
            jsonObject = PageUtil.pageInfo2JSON(page.getTotalCount(), page.getPageCount(), page.getCurrrentPage(), jsonArray);
        }else{
            jsonObject = PageUtil.pageInfo2JSON(0,param.getPageSize(),1,new JSONArray());
        }
        ajaxData.setFlag(true);
        ajaxData.setData(jsonObject);
        return ajaxData;
    }
    
    //浏览索引图片，分页
    private AjaxData scanAttachmentInfoSolrIndex2AjaxDataPage(AttachmentInfoSolrIndexParam param) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new AttachmentInfoSolrIndexParam();
        }
        //排序字段
        param.addSort("likeDegree",SolrQuery.ORDER.desc);
        param.addSort("fileName",SolrQuery.ORDER.asc);
        param.addSort("type",SolrQuery.ORDER.asc);
        
        //设置分页信息
        if(null == param.getCurrentPage()){
            param.setCurrentPage(1);
        }
        if(null == param.getPageSize()){
            param.setPageSize(PageUtil.PAGE_SIZE);
        }
        param.setStart(param.getPageSize()*(param.getCurrentPage()-1));
        
        Page<AttachmentInfoSolrData> page = solrClient.searchAttachmentsForPage(param);
        JSONObject jsonObject = new JSONObject();
        if (null != page) {
            JSONArray jsonArray = Data2JSONUtil.scanImageSolrIndex2JSONArray(page.getList());
            jsonObject = PageUtil.pageInfo2JSON(page.getTotalCount(), page.getPageCount(), page.getCurrrentPage(), jsonArray);
        }else{
            jsonObject = PageUtil.pageInfo2JSON(0,param.getPageSize(),1,new JSONArray());
        }
        ajaxData.setFlag(true);
        ajaxData.setData(jsonObject);
        return ajaxData;
    }
    
}
