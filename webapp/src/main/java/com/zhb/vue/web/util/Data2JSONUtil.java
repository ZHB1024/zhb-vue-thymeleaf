package com.zhb.vue.web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhb.forever.framework.dic.DeleteFlagEnum;
import com.zhb.forever.framework.dic.LikeDgreeEnum;
import com.zhb.forever.framework.dic.AttachmentTypeEnum;
import com.zhb.forever.framework.util.ComparatorVOComparator;
import com.zhb.forever.framework.util.DateTimeUtil;
import com.zhb.forever.framework.util.FileUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.ComparatorVO;
import com.zhb.forever.framework.vo.UserInfoVO;
import com.zhb.forever.search.solr.vo.AttachmentInfoSolrData;
import com.zhb.vue.dic.VerificationCodeTypeEnum;
import com.zhb.vue.pojo.AttachmentInfoData;
import com.zhb.vue.pojo.DicInfoData;
import com.zhb.vue.pojo.FunctionInfoData;
import com.zhb.vue.pojo.IconInfoData;
import com.zhb.vue.pojo.UserFunctionInfoData;
import com.zhb.vue.pojo.UserInfoData;
import com.zhb.vue.pojo.VerificationCodeInfoData;

public class Data2JSONUtil {
    
    public static JSONObject userInfoData2JSONObject(UserInfoData data) {
        if (null == data) {
            return null;
        }
        JSONObject object = new JSONObject();
        object.put("id", data.getId());
        object.put("userName", data.getUserName());
        object.put("realName", data.getRealName());
        object.put("identityCard", data.getIdentityCard());
        object.put("sex", data.getSex());
        object.put("birthday", DateTimeUtil.getDateTime(data.getBirthday(),"yyyy-MM-dd"));
        object.put("lobId", data.getLobId());
        object.put("mobilePhone", data.getMobilePhone());
        object.put("byyx", data.getByyx());
        object.put("country", data.getCountry());
        object.put("nation", data.getNation());
        object.put("email", data.getEmail());
        object.put("createTime", data.getCreateTime());
        object.put("updateTime", data.getUpdateTime());
        object.put("deleteFlag", data.getDeleteFlag());
        object.put("deleteFlagName", DeleteFlagEnum.getName(data.getDeleteFlag()));
        return object;
    }
    
    public static JSONArray userInfoDatas2JSONArray(List<UserInfoData> datas) {
        JSONArray jsonArray = null;
        if (null != datas && datas.size() >0) {
            jsonArray = new JSONArray();
            for (UserInfoData data : datas) {
                JSONObject object = new JSONObject();
                object.put("id", data.getId());
                object.put("userName", data.getUserName());
                object.put("realName", data.getRealName());
                object.put("identityCard", data.getIdentityCard());
                object.put("sex", data.getSex());
                object.put("birthday", DateTimeUtil.getDateTime(data.getBirthday(),"yyyy-MM-dd"));
                object.put("lobId", data.getLobId());
                object.put("mobilePhone", data.getMobilePhone());
                object.put("byyx", data.getByyx());
                object.put("country", data.getCountry());
                object.put("nation", data.getNation());
                object.put("email", data.getEmail());
                object.put("createTime", data.getCreateTime());
                object.put("updateTime", data.getUpdateTime());
                object.put("deleteFlag", data.getDeleteFlag());
                object.put("deleteFlagName", DeleteFlagEnum.getName(data.getDeleteFlag()));
                jsonArray.add(object);
            }
        }
        return jsonArray;
    }
    
    
    
    public static JSONObject userInfoVO2JSONObject(UserInfoVO data) {
        if (null == data) {
            return null;
        }
        JSONObject object = new JSONObject();
        object.put("id", data.getId());
        object.put("userName", data.getUserName());
        object.put("realName", data.getRealName());
        object.put("identityCard", data.getIdentityCard());
        object.put("sex", data.getSex());
        object.put("birthday", DateTimeUtil.getDateTime(data.getBirthday(),"yyyy-MM-dd"));
        object.put("lobId", data.getLobId());
        object.put("mobilePhone", data.getMobilePhone());
        object.put("byyx", data.getByyx());
        object.put("country", data.getCountry());
        object.put("nation", data.getNation());
        object.put("email", data.getEmail());
        object.put("createTime", data.getCreateTime());
        object.put("updateTime", data.getUpdateTime());
        object.put("deleteFlag", data.getDeleteFlag());
        object.put("deleteFlagName", DeleteFlagEnum.getName(data.getDeleteFlag()));
        
        return object;
    }
    
    public static JSONArray userInfoVOs2JSONArray(List<UserInfoVO> datas) {
        JSONArray jsonArray = null;
        if (null != datas && datas.size() >0) {
            jsonArray = new JSONArray();
            for (UserInfoVO data : datas) {
                JSONObject object = new JSONObject();
                object.put("id", data.getId());
                object.put("userName", data.getUserName());
                object.put("realName", data.getRealName());
                object.put("identityCard", data.getIdentityCard());
                object.put("sex", data.getSex());
                object.put("birthday", DateTimeUtil.getDateTime(data.getBirthday(),"yyyy-MM-dd"));
                object.put("lobId", data.getLobId());
                object.put("mobilePhone", data.getMobilePhone());
                object.put("byyx", data.getByyx());
                object.put("country", data.getCountry());
                object.put("nation", data.getNation());
                object.put("email", data.getEmail());
                object.put("createTime", data.getCreateTime());
                object.put("updateTime", data.getUpdateTime());
                object.put("deleteFlag", data.getDeleteFlag());
                object.put("deleteFlagName", DeleteFlagEnum.getName(data.getDeleteFlag()));
                jsonArray.add(object);
            }
        }
        
        return jsonArray;
    }
    
    public static JSONArray dicInfoData2JSONArray(DicInfoData data) {
        if (null == data ) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("id", data.getId());
        object.put("category", data.getCategory());
        object.put("code", data.getCode());
        object.put("name", data.getName());
        object.put("name2", data.getName2());
        object.put("name3", data.getName3());
        object.put("type", data.getType());
        object.put("orderIndex", data.getOrderIndex());
        object.put("deleteFlag", data.getDeleteFlag());
        object.put("deleteFlagName", DeleteFlagEnum.getName(data.getDeleteFlag()));
        object.put("remark", data.getRemark());
        jsonArray.add(object);
        return jsonArray;
    }
    
    public static JSONArray dicInfoDatas2JSONArray(List<DicInfoData> datas) {
        if (null == datas || datas.size() == 0) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (DicInfoData data : datas) {
            JSONObject object = new JSONObject();
            object.put("id", data.getId());
            object.put("category", data.getCategory());
            object.put("code", data.getCode());
            object.put("name", data.getName());
            object.put("name2", data.getName2());
            object.put("name3", data.getName3());
            object.put("type", data.getType());
            object.put("orderIndex", data.getOrderIndex());
            object.put("deleteFlag", data.getDeleteFlag());
            object.put("deleteFlagName", DeleteFlagEnum.getName(data.getDeleteFlag()));
            object.put("remark", data.getRemark());
            jsonArray.add(object);
        }
        
        return jsonArray;
    }
    public static JSONObject iconInfoData2JSONObject(IconInfoData data) {
        if (null == data ) {
            return null;
        }
        JSONObject object = new JSONObject();
        object.put("id", data.getId());
        object.put("name", data.getName());
        object.put("value", data.getValue());
        object.put("deleteFlag", data.getDeleteFlag());
        object.put("deleteFlagName", DeleteFlagEnum.getName(data.getDeleteFlag()));
        object.put("createTime", DateTimeUtil.getDateTime(data.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        object.put("updateTime", DateTimeUtil.getDateTime(data.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
        return object;
    }
    
    public static JSONArray iconInfoDatas2JSONArray(List<IconInfoData> datas) {
        if (null == datas || datas.size() == 0) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (IconInfoData data : datas) {
            JSONObject object = new JSONObject();
            object.put("id", data.getId());
            object.put("name", data.getName());
            object.put("value", data.getValue());
            object.put("deleteFlag", data.getDeleteFlag());
            object.put("deleteFlagName", DeleteFlagEnum.getName(data.getDeleteFlag()));
            object.put("createTime", DateTimeUtil.getDateTime(data.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            object.put("updateTime", DateTimeUtil.getDateTime(data.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
            jsonArray.add(object);
        }
        
        return jsonArray;
    }
    
    //功能
    public static JSONObject functionInfoData2JSONObject(FunctionInfoData data) {
        if (null == data) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", data.getId());
        jsonObject.put("path", data.getPath());
        jsonObject.put("name", data.getName());
        if (null != data.getIconInfoData()) {
            jsonObject.put("icon", data.getIconInfoData().getName());
            jsonObject.put("iconId", data.getIconInfoData().getId());
        }else {
            jsonObject.put("icon", "");
            jsonObject.put("iconId", "");
        }
        jsonObject.put("order", data.getOrder());
        jsonObject.put("deleteFlag", data.getDeleteFlag());
        if (null != data.getParentFunctionInfo()) {
            jsonObject.put("parentId", data.getParentFunctionInfo().getId());
        }else {
            jsonObject.put("parentId", "");
        }
        
        return jsonObject;
    }
    
    //父功能
    public static JSONArray functionInfoDatas2JSONArray(List<FunctionInfoData> datas) {
        if (null == datas || datas.size() == 0) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (FunctionInfoData parent : datas) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", parent.getId());
            jsonObject.put("path", parent.getPath());
            jsonObject.put("name", parent.getName());
            jsonObject.put("icon", null==parent.getIconInfoData()?"":parent.getIconInfoData().getName());
            jsonObject.put("order", parent.getOrder());
            jsonObject.put("deleteFlag", parent.getDeleteFlag());
            
            JSONArray jbjlChildrenMenu = new JSONArray();
            for(FunctionInfoData data : parent.getChildFunctionInfos()){
                JSONObject json = new JSONObject();
                json.put("id", data.getId());
                json.put("path", data.getPath());
                json.put("name", data.getName());
                json.put("icon", null==data.getIconInfoData()?"":data.getIconInfoData().getName());
                json.put("order", data.getOrder());
                json.put("deleteFlag", data.getDeleteFlag());
                jbjlChildrenMenu.add(json);
            }
            
            jsonObject.put("children", jbjlChildrenMenu);
            
            jsonArray.add(jsonObject);
            
        }
        
        return jsonArray;
    }
    
    //左侧菜单
    public static JSONArray generateJSonArray(List<UserFunctionInfoData> datas) {
        JSONArray jsonArray = new JSONArray();
        Map<FunctionInfoData, List<FunctionInfoData>> map = null;
        if (null != datas && datas.size() >0) {
            //将父级相同的功能整合到map中
            map = new HashMap<FunctionInfoData, List<FunctionInfoData>>();
            for (UserFunctionInfoData data : datas) {
                FunctionInfoData functionInfo = data.getFunctionInfoData();
                if (!map.containsKey(functionInfo.getParentFunctionInfo())) {
                    List<FunctionInfoData> functionsList = new ArrayList<>();
                    functionsList.add(functionInfo);
                    map.put(functionInfo.getParentFunctionInfo(), functionsList);
                }else {
                    List<FunctionInfoData> functionsList = map.get(functionInfo.getParentFunctionInfo());
                    functionsList.add(functionInfo);
                    map.put(functionInfo.getParentFunctionInfo(), functionsList);
                }
            }
            
            //将map整理到可排序的ComparatorVO中
            List<ComparatorVO> vos = new ArrayList<>();
            for (Map.Entry<FunctionInfoData,List<FunctionInfoData>> object : map.entrySet()) {
                FunctionInfoData parent = object.getKey();
                List<FunctionInfoData> childrens = object.getValue();
                
                ComparatorVO vo = new ComparatorVO(parent.getOrder());
                vo.setId(parent.getId());
                vo.setName(parent.getName());
                vo.setPath(parent.getPath());
                vo.setIconName(null == parent.getIconInfoData()?"":parent.getIconInfoData().getValue());
                List<ComparatorVO> childs = new ArrayList<>();
                for(FunctionInfoData funData : childrens){
                    ComparatorVO child = new ComparatorVO(funData.getOrder());
                    child.setId(funData.getId());
                    child.setName(funData.getName());
                    child.setPath(funData.getPath());
                    child.setIconName(null==funData.getIconInfoData()?"":funData.getIconInfoData().getValue());
                    childs.add(child);
                }
                //排序,对子菜单排序
                Collections.sort(childs, new ComparatorVOComparator());
                vo.setChilds(childs);
                vos.add(vo);
            }
            
            //排序,对父菜单排序
            Collections.sort(vos, new ComparatorVOComparator());
            
            //将排序后的数据 整理到 JSONArray中返回给用户
            for (ComparatorVO comparatorVO : vos) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", comparatorVO.getId());
                jsonObject.put("path", comparatorVO.getPath());
                jsonObject.put("name", comparatorVO.getName());
                jsonObject.put("icon", comparatorVO.getIconName());
                
                JSONArray jbjlChildrenMenu = new JSONArray();
                for(ComparatorVO funData : comparatorVO.getChilds()){
                    JSONObject json = new JSONObject();
                    json.put("id", funData.getId());
                    json.put("path", funData.getPath());
                    json.put("name", funData.getName());
                    json.put("icon", funData.getIconName());
                    jbjlChildrenMenu.add(json);
                }
                
                jsonObject.put("children", jbjlChildrenMenu);
                
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }
    
    public static JSONObject userFunctionInfoData2JSONObject(UserFunctionInfoData data) {
        if (null == data) {
            return null;
        }
        JSONObject object = new JSONObject();
        object.put("id", data.getId());
        object.put("userName", data.getUserInfoData().getUserName());
        object.put("realName", data.getUserInfoData().getRealName());
        object.put("functionName", data.getFunctionInfoData().getName());
        return object;
    }
    
    public static JSONArray userFunctionInfoDatas2JSONObject(List<UserFunctionInfoData> datas) {
        JSONArray jsonArray = null;
        if (null != datas && datas.size() >0) {
            jsonArray = new JSONArray();
            for (UserFunctionInfoData data : datas) {
                JSONObject object = new JSONObject();
                object.put("id", data.getId());
                object.put("userName", data.getUserInfoData().getUserName());
                object.put("realName", data.getUserInfoData().getRealName());
                object.put("functionName", data.getFunctionInfoData().getName());
                jsonArray.add(object);
            }
        }
        return jsonArray;
    }
    
    public static JSONObject verificationCodeInfoData2JSONObject(VerificationCodeInfoData data) {
        if (null == data ) {
            return null;
        }
        JSONObject object = new JSONObject();
        object.put("id", data.getId());
        object.put("email", data.getEmail());
        object.put("mobilePhone", data.getMobilePhone());
        object.put("type", VerificationCodeTypeEnum.getName(data.getType()));
        object.put("code", data.getCode());
        object.put("remark", data.getRemark());
        object.put("deleteFlag", data.getDeleteFlag());
        object.put("deleteFlagName", DeleteFlagEnum.getName(data.getDeleteFlag()));
        object.put("createTime", DateTimeUtil.getDateTime(data.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        object.put("updateTime", DateTimeUtil.getDateTime(data.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
        return object;
    }
    
    public static JSONArray verificationCodeInfoDatas2JSONArray(List<VerificationCodeInfoData> datas) {
        if (null == datas || datas.size() == 0) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (VerificationCodeInfoData data : datas) {
            JSONObject object = new JSONObject();
            object.put("id", data.getId());
            object.put("email", data.getEmail());
            object.put("mobilePhone", data.getMobilePhone());
            object.put("type", VerificationCodeTypeEnum.getName(data.getType()));
            object.put("code", data.getCode());
            object.put("remark", data.getRemark());
            object.put("deleteFlag", data.getDeleteFlag());
            object.put("deleteFlagName", DeleteFlagEnum.getName(data.getDeleteFlag()));
            object.put("createTime", DateTimeUtil.getDateTime(data.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            object.put("updateTime", DateTimeUtil.getDateTime(data.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            jsonArray.add(object);
        }
        
        return jsonArray;
    }
    
    public static JSONObject attachmentInfoData2JSONObject(AttachmentInfoData data) {
        if (null == data ) {
            return null;
        }
        JSONObject object = new JSONObject();
        object.put("id", data.getId());
        object.put("fileName", data.getFileName());
        object.put("filePath", data.getFilePath());
        object.put("fileSize", FileUtil.getFileSizeKB(Double.valueOf(data.getFileSize()), 2) + "KB");
        object.put("type", data.getType());
        object.put("typeName", AttachmentTypeEnum.getName(data.getType()));
        object.put("contentType", data.getContentType());
        object.put("deleteFlag", data.getDeleteFlag());
        object.put("deleteFlagName", DeleteFlagEnum.getName(data.getDeleteFlag()));
        object.put("createTime", DateTimeUtil.getDateTime(data.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        object.put("attachmentUrl", "/htgl/attachmentinfocontroller/downloadattachmentinfo?id=" + data.getId());
        object.put("originalUrl", "/htgl/attachmentinfocontroller/getoriginalattachmentinfo?id=" + data.getId());
        object.put("thumbnailUrl", "/htgl/attachmentinfocontroller/getthumbnailattachmentinfo?id=" + data.getId());
        return object;
    }
    
    public static JSONArray attachmentInfoDatas2JSONArray(List<AttachmentInfoData> datas) {
        if (null == datas || datas.size() == 0) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (AttachmentInfoData data : datas) {
            JSONObject object = new JSONObject();
            object.put("id", data.getId());
            object.put("fileName", data.getFileName());
            object.put("filePath", data.getFilePath());
            object.put("fileSize", FileUtil.getFileSizeKB(Double.valueOf(data.getFileSize()), 2) + "KB");
            object.put("type", data.getType());
            object.put("typeName", AttachmentTypeEnum.getName(data.getType()));
            object.put("likeDegree", data.getLikeDegree());
            object.put("likeDegreeName", LikeDgreeEnum.getName(data.getLikeDegree()));
            object.put("contentType", data.getContentType());
            object.put("deleteFlag", data.getDeleteFlag());
            object.put("deleteFlagName", DeleteFlagEnum.getName(data.getDeleteFlag()));
            object.put("createTime", DateTimeUtil.getDateTime(data.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            object.put("attachmentUrl", "/htgl/attachmentinfocontroller/downloadattachmentinfo?id=" + data.getId());
            object.put("originalUrl", "/htgl/attachmentinfocontroller/getoriginalattachmentinfo?id=" + data.getId());
            object.put("thumbnailUrl", "/htgl/attachmentinfocontroller/getthumbnailattachmentinfo?id=" + data.getId());
            jsonArray.add(object);
        }
        return jsonArray;
    }
    
    
    //solrIndexData2JSONObject
    public static JSONObject attachmentInfoSolrIndexData2JSONObject(AttachmentInfoSolrData data) {
        if (null == data ) {
            return null;
        }
        JSONObject object = new JSONObject();
        object.put("id", data.getId());
        object.put("fileName", data.getFileName());
        object.put("filePath", data.getFilePath());
        object.put("type", data.getType());
        object.put("typeName", AttachmentTypeEnum.getName(Integer.valueOf(data.getType())));
        object.put("likeDegree", data.getLikeDegree());
        object.put("likeDegreeName", LikeDgreeEnum.getName(Integer.valueOf(data.getLikeDegree())));
        object.put("deleteFlag", DeleteFlagEnum.UDEL.getIndex());
        object.put("deleteFlagName", DeleteFlagEnum.UDEL.getName());
        object.put("createTime", DateTimeUtil.getDateTime(data.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        object.put("attachmentUrl", "/htgl/attachmentinfocontroller/downloadattachmentinfo?id=" + data.getId());
        object.put("originalUrl", "/htgl/attachmentinfocontroller/getoriginalattachmentinfo?id=" + data.getId());
        object.put("thumbnailUrl", "/htgl/attachmentinfocontroller/getthumbnailattachmentinfo?id=" + data.getId());
        return object;
    }
    
    //solrIndexDatas2JSONArray
    public static JSONArray attachmentInfoSolrIndexDatas2JSONObject(List<AttachmentInfoSolrData> datas) {
        if (null == datas || datas.size() == 0) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (AttachmentInfoSolrData data : datas) {
            JSONObject object = new JSONObject();
            object.put("id", data.getId());
            object.put("fileName", data.getFileName());
            object.put("filePath", data.getFilePath());
            object.put("type", data.getType());
            object.put("typeName", AttachmentTypeEnum.getName(Integer.valueOf(data.getType())));
            object.put("likeDegree", data.getLikeDegree());
            object.put("likeDegreeName", LikeDgreeEnum.getName(Integer.valueOf(data.getLikeDegree())));
            object.put("deleteFlag", DeleteFlagEnum.UDEL.getIndex());
            object.put("deleteFlagName", DeleteFlagEnum.UDEL.getName());
            object.put("createTime", DateTimeUtil.getDateTime(data.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            object.put("attachmentUrl", "/htgl/attachmentinfocontroller/downloadattachmentinfo?id=" + data.getId());
            object.put("originalUrl", "/htgl/attachmentinfocontroller/getoriginalattachmentinfo?id=" + data.getId());
            object.put("thumbnailUrl", "/htgl/attachmentinfocontroller/getthumbnailattachmentinfo?id=" + data.getId());
            jsonArray.add(object);
        }
        return jsonArray;
    }
    
    //浏览图片,solrIndex
    public static JSONArray scanImageSolrIndex2JSONArray(List<AttachmentInfoSolrData> datas) {
        if (null == datas || datas.size() == 0) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (AttachmentInfoSolrData data : datas) {
            jsonArray.add("/htgl/attachmentinfocontroller/downloadattachmentinfo?id=" + data.getId());
        }
        return jsonArray;
    }
    
    //浏览图片
    public static JSONArray imageDatas2JSONArray(List<AttachmentInfoData> datas) {
        if (null == datas || datas.size() == 0) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (AttachmentInfoData data : datas) {
            jsonArray.add("/htgl/attachmentinfocontroller/downloadattachmentinfo?id=" + data.getId());
        }
        return jsonArray;
    }
    
    //统计附件
    public static JSONObject statisticAttachment2JSONObject(String titleName,List<Object[]> results) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isBlank(titleName)) {
            jsonObject.put("titleName", "统计");
        }else{
            jsonObject.put("titleName", titleName);
        }
        
        JSONArray names = new JSONArray();
        JSONArray values = new JSONArray();
        JSONArray nameValues = new JSONArray();
        if (null != results) {
            for (Object[] object : results) {
                names.add(AttachmentTypeEnum.getName(Integer.valueOf(object[0].toString())));
                values.add(object[1]);
                JSONObject json = new JSONObject();
                json.put("name", AttachmentTypeEnum.getName(Integer.valueOf(object[0].toString())));
                json.put("value", object[1]);
                nameValues.add(json);
            }
        }
        jsonObject.put("names", names);
        jsonObject.put("values", values);
        jsonObject.put("nameValues", nameValues);
        return jsonObject;
    }
    
    //统计字典项
    public static JSONObject statisticDic2JSONObject(String titleName,List<Object[]> results) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isBlank(titleName)) {
            jsonObject.put("titleName", "统计");
        }else{
            jsonObject.put("titleName", titleName);
        }
        
        JSONArray names = new JSONArray();
        JSONArray values = new JSONArray();
        JSONArray nameValues = new JSONArray();
        if (null != results) {
            for (Object[] object : results) {
                names.add(object[0].toString());
                values.add(object[1]);
                JSONObject json = new JSONObject();
                json.put("name", object[0].toString());
                json.put("value", object[1]);
                nameValues.add(json);
            }
        }
        jsonObject.put("names", names);
        jsonObject.put("values", values);
        jsonObject.put("nameValues", nameValues);
        return jsonObject;
    }

}
