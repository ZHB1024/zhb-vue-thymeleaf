package com.zhb.vue.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhb.forever.framework.dic.DeleteFlagEnum;
import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.page.PageUtil;
import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.PoiUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.params.DicInfoParam;
import com.zhb.vue.pojo.DicInfoData;
import com.zhb.vue.service.DicInfoService;
import com.zhb.vue.web.util.Data2JSONUtil;
import com.zhb.vue.web.util.WebAppUtil;
import com.zhb.vue.web.util.WriteJSUtil;

@Controller
@RequestMapping("/htgl/dicinfocontroller")
public class DicInfoController {

    private static Logger logger = LoggerFactory.getLogger(DicInfoController.class);
    
    @Autowired
    private DicInfoService dicInfoService;
    
    //toindex
    @RequestMapping(value = "/toindex",method = RequestMethod.GET)
    @Transactional
    public String toDicIndex(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return "login/login";
        }
        return "htgl/dic/index";
    }
    
    //查询
    @RequestMapping(value = "/getdicinfo/api")
    @ResponseBody
    @Transactional
    public AjaxData getDicInfo(HttpServletRequest request,HttpServletResponse response,DicInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        ajaxData = searchDicInfo2AjaxData(param, request);
        return ajaxData;
    }
    
    //查询,分页
    @RequestMapping(value = "/getdicinfopage/api")
    @ResponseBody
    @Transactional
    public AjaxData getDicInfoPage(HttpServletRequest request,HttpServletResponse response,DicInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        ajaxData = searchDicInfo2AjaxDataPage(param, request);
        return ajaxData;
    }
    
    //toupload
    @RequestMapping(value = "/toupload",method = RequestMethod.GET)
    @Transactional
    public String toUploadDic(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return "login/login";
        }
        return "htgl/dic/upload";
    }
    
    //上传
    @RequestMapping(value = "/uploaddicinfo")
    @Transactional
    public String uploadDicInfo(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return WriteJSUtil.writeJS("请先登录", response);
        }
        
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile multipartFile = multipartRequest.getFile("upFile");
        Long fileSize = multipartFile.getSize();
        String fileType = multipartFile.getContentType();
        String fileName = multipartFile.getOriginalFilename();;
        
        Workbook wb = null;;
        try {
            wb = PoiUtil.createWorkbook(multipartFile.getInputStream());
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        Map<Integer, List<DicInfoData>> dataMap = new HashMap<>();
        int sheetCount = wb.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            Sheet sheet = wb.getSheetAt(i);
            if (null == sheet) {
                continue;
            }
            int totalRow = sheet.getPhysicalNumberOfRows();
            if (totalRow ==0 ) {
                continue;
            }
            if (totalRow == 1) {
                return WriteJSUtil.writeJS("请补全第 " + (i+1) +" 个sheet页的内容", response);
            }
            List<DicInfoData> dicInfoDatas = new ArrayList<>();
            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            for(int currentRow=firstRow+1; currentRow <= lastRow; currentRow++) {
                Row row = sheet.getRow(currentRow);
                if (null == row) {
                    continue;
                }
                
                String[] values = new String[9];
                for(int cellIndex=0;cellIndex < 9;cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    if (null == cell) {
                        continue;
                    }
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String content = cell.getStringCellValue();
                    if (StringUtil.isNotBlank(content)) {
                        values[cellIndex] = content;
                    }
                }
                
                DicInfoData data = new DicInfoData();
                if (StringUtil.isBlank(values[0])) {
                    return WriteJSUtil.writeJS("请补全第 " + (i+1) +" 个sheet页,第 " + (currentRow+1) + " 行，第 1 列的内容" , response);
                }
                data.setCategory(values[0]);
                
                if (StringUtil.isBlank(values[1])) {
                    return WriteJSUtil.writeJS("请补全第 " + (i+1) +" 个sheet页,第 " + (currentRow+1) + " 行，第 2 列的内容" , response);
                }
                data.setCode(values[1]);
                
                if (StringUtil.isBlank(values[2])) {
                    return WriteJSUtil.writeJS("请补全第 " + (i+1) +" 个sheet页,第 " + (currentRow+1) + " 行，第 3 列的内容" , response);
                }
                data.setName(values[2]);
                data.setName2(values[3]);
                data.setName3(values[4]);
                
                if (StringUtil.isBlank(values[5])) {
                    return WriteJSUtil.writeJS("请补全第 " + (i+1) +" 个sheet页,第 " + (currentRow+1) + " 行，第 6 列的内容" , response);
                }
                data.setType(values[5]);
                
                if (StringUtil.isBlank(values[6])) {
                    return WriteJSUtil.writeJS("请补全第 " + (i+1) +" 个sheet页,第 " + (currentRow+1) + " 行，第 7 列的内容" , response);
                }
                data.setOrderIndex(Integer.valueOf(values[6]));
                
                data.setRemark(values[7]);
                
                if (StringUtil.isBlank(values[8])) {
                    data.setDeleteFlag(DeleteFlagEnum.UDEL.getIndex());
                }else {
                    data.setDeleteFlag(Integer.valueOf(values[8]));
                }
                
                data.setCreateUserId(WebAppUtil.getUserId(request));
                
                dicInfoDatas.add(data);
                
            }
            dataMap.put(i, dicInfoDatas);
        }
        if (dataMap.size() > 0) {
            for (Map.Entry<Integer, List<DicInfoData>> entry : dataMap.entrySet()) {
                List<DicInfoData> datas = entry.getValue();
                if (datas.size() > 0) {
                    dicInfoService.saveOrUpdate(datas);
                }
            }
        }
        ajaxData.setFlag(true);
        return "htgl/dic/index";
    }
    
    //获取category，排重
    @RequestMapping(value = "/getdiccategory/api")
    @ResponseBody
    @Transactional
    public AjaxData getDicCategory(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        JSONArray jsonArray = new JSONArray();
        List<String> categorys = dicInfoService.getDicCategory();
        if (null != categorys && categorys.size() > 0) {
            for (String category : categorys) {
                JSONObject object = new JSONObject();
                object.put("value", category);
                jsonArray.add(object);
            }
            ajaxData.setData(jsonArray);
            ajaxData.setFlag(true);
        }
        return ajaxData;
    }
    //获取类型，排重
    @RequestMapping(value = "/getdictype/api")
    @ResponseBody
    @Transactional
    public AjaxData getDicType(HttpServletRequest request,HttpServletResponse response,DicInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        JSONArray jsonArray = new JSONArray();
        if (StringUtil.isNotBlank(param.getCategory())) {
            List<String> types = dicInfoService.getDicTypeByCategory(param);
            if (null != types && types.size() > 0) {
                for (String type : types) {
                    JSONObject object = new JSONObject();
                    object.put("value", type);
                    jsonArray.add(object);
                }
            }
        }
        ajaxData.setData(jsonArray);
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //共用查询,不分页
    private AjaxData searchDicInfo2AjaxData(DicInfoParam param,HttpServletRequest request) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new DicInfoParam();
        }
        
        //排序字段
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo = new OrderVO("category",true);
        orderVos.add(vo);
        OrderVO vo2 = new OrderVO("orderIndex",true);
        orderVos.add(vo2);
        
        List<DicInfoData> dicInfos = dicInfoService.getDicInfos(param,orderVos);
        if (null != dicInfos) {
            ajaxData.setData(Data2JSONUtil.dicInfoDatas2JSONArray(dicInfos));
        }
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //共用查询,分页
    private AjaxData searchDicInfo2AjaxDataPage(DicInfoParam param,HttpServletRequest request) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new DicInfoParam();
        }
        if ("undefined".equals(param.getCategory())) {
            param.setCategory(null);
        }
        if ("undefined".equals(param.getType())) {
            param.setType(null);
        }
        //排序字段
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo = new OrderVO("category",true);
        orderVos.add(vo);
        OrderVO vo2 = new OrderVO("orderIndex",true);
        orderVos.add(vo2);
        
        //设置分页信息
        if(null == param.getCurrentPage()){
            param.setCurrentPage(1);
        }
        if(null == param.getPageSize()){
            param.setPageSize(PageUtil.PAGE_SIZE);
        }
        param.setStart(param.getPageSize()*(param.getCurrentPage()-1));
        
        Page<DicInfoData> page = dicInfoService.getDicInfosPage(param,orderVos);
        JSONObject jsonObject = new JSONObject();
        if (null != page) {
            JSONArray workDayJson = Data2JSONUtil.dicInfoDatas2JSONArray(page.getList());
            jsonObject = PageUtil.pageInfo2JSON(page.getTotalCount(), page.getPageCount(), page.getCurrrentPage(), workDayJson);
        }else{
            jsonObject = PageUtil.pageInfo2JSON(0,param.getPageSize(),1,new JSONArray());
        }
        ajaxData.setFlag(true);
        ajaxData.setData(jsonObject);
        return ajaxData;
    }
    
    
}
