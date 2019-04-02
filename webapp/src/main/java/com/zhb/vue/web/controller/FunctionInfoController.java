package com.zhb.vue.web.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhb.forever.framework.dic.DeleteFlagEnum;
import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.params.FunctionInfoParam;
import com.zhb.vue.params.IconInfoParam;
import com.zhb.vue.params.UserFunctionInfoParam;
import com.zhb.vue.pojo.FunctionInfoData;
import com.zhb.vue.pojo.IconInfoData;
import com.zhb.vue.pojo.UserFunctionInfoData;
import com.zhb.vue.pojo.UserInfoData;
import com.zhb.vue.service.FunctionInfoService;
import com.zhb.vue.service.IconInfoService;
import com.zhb.vue.service.UserInfoService;
import com.zhb.vue.web.util.Data2JSONUtil;
import com.zhb.vue.web.util.Param2DataUtil;
import com.zhb.vue.web.util.WebAppUtil;
import com.zhb.vue.web.util.WriteJSUtil;

@Controller
@RequestMapping("/htgl/functioninfocontroller")
public class FunctionInfoController {
    
    private static Logger logger = LoggerFactory.getLogger(FunctionInfoController.class);
    
    @Autowired
    private FunctionInfoService functionInfoService;
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private IconInfoService iconInfoService;
    
    //toindex
    @RequestMapping(value="/toindex",method=RequestMethod.GET)
    @Transactional
    public String toIndex(HttpServletRequest request,HttpServletResponse response) {
        return "htgl/function/index";
    }
    
    //查询功能
    @RequestMapping("/getfunctions/api")
    @ResponseBody
    @Transactional
    public AjaxData getFunctions(HttpServletRequest request,HttpServletResponse response,FunctionInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        param.setType(0);
        ajaxData = searchFunctionInfo2AjaxData(param,request);
        return ajaxData;
    }
    
    //查询所有的功能
    @RequestMapping("/getallfunctions/api")
    @ResponseBody
    @Transactional
    public AjaxData getAllFunctions(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo2 = new OrderVO("order",true);
        orderVos.add(vo2);
        List<FunctionInfoData> functionInfos = functionInfoService.getAllFunctions(orderVos);
        if (null != functionInfos) {
            //ajaxData.setData(Data2JSONUtil.functionInfoDatas2JSONArray(functionInfos));
        }
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //toadd
    @RequestMapping(value="/toadd",method=RequestMethod.GET)
    @Transactional
    public String toAdd(HttpServletRequest request,HttpServletResponse response) {
        return "htgl/function/add";
    }
    
    //新增功能
    @RequestMapping(value="/addfunctioninfo/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData addFunctionInfo(HttpServletRequest request,HttpServletResponse response,FunctionInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        
        if (StringUtil.isBlank(param.getName())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请输入功能名称");
            return ajaxData;
        }
        if (StringUtil.isBlank(param.getPath())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请输入功能路径");
            return ajaxData;
        }
        
        if (StringUtil.isNotBlank(param.getParentId()) && !"undefined".equals(param.getParentId())) {
            FunctionInfoData functionInfoData = functionInfoService.getFunctionById(param.getParentId());
            if (null != functionInfoData) {
                param.setParentFunctionInfo(functionInfoData);
                param.setType(1);
            }else {
                param.setType(0);
            }
        }else {
            param.setType(0);
        }
        
        if (StringUtil.isNotBlank(param.getIconId())) {
            IconInfoParam iconInfoParam = new IconInfoParam();
            iconInfoParam.setId(param.getIconId());
            List<IconInfoData> iconInfoDatas = iconInfoService.getIconInfos(iconInfoParam,null);
            if (null != iconInfoDatas && iconInfoDatas.size() > 0) {
                param.setIconInfoData(iconInfoDatas.get(0));
            }
        }
        param.setDeleteFlag(DeleteFlagEnum.UDEL.getIndex());
        
        FunctionInfoData data = new FunctionInfoData();
        Param2DataUtil.functionParam2Data(param, data);
        data.setCreateUserId(WebAppUtil.getUserId(request));
        
        functionInfoService.saveOrUpdateFunctionInfoData(data);
        
        //将功能授权给管理员
        if (data.getType() == 1) {
            UserInfoData userInfoData = userInfoService.getUserInfoById(WebAppUtil.getUserId(request));
            UserFunctionInfoData userFunctionInfoData = new UserFunctionInfoData();
            userFunctionInfoData.setFunctionInfoData(data);
            userFunctionInfoData.setUserInfoData(userInfoData);
            functionInfoService.saveOrUpdateUserFunctionInfoData(userFunctionInfoData);
        }
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //获取父级功能
    @RequestMapping("/getparentfunctions/api")
    @ResponseBody
    @Transactional
    public AjaxData getParentFunctions(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        FunctionInfoParam param = new FunctionInfoParam();
        param.setType(0);
        ajaxData = searchFunctionInfo2AjaxData(param, request);
        return ajaxData;
    }
    
    //toupdate
    @RequestMapping(value="/toupdate",method = RequestMethod.GET)
    @Transactional
    public String toUpdate(HttpServletRequest request,HttpServletResponse response,FunctionInfoParam param,Model model) {
        if (StringUtil.isBlank(param.getId())) {
            return WriteJSUtil.writeJS("非法操作", response);
        }
        FunctionInfoData data = functionInfoService.getFunctionById(param.getId());
        if (null == data) {
            return WriteJSUtil.writeJS("非法操作", response);
        }
        
        JSONObject object = Data2JSONUtil.functionInfoData2JSONObject(data);
        request.setAttribute("functionInfoJson", object);
        //model.addAttribute("functionInfoJson",object);
        
        return "htgl/function/update";
    }
    
    //修改功能信息
    @RequestMapping(value="/updatefunctioninfo/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData updateFunctionInfo(HttpServletRequest request,HttpServletResponse response,FunctionInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(param.getId())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("非法操作");
            return ajaxData;
        }
        FunctionInfoData data = functionInfoService.getFunctionById(param.getId());
        if (null == data) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("非法操作");
            return ajaxData;
        }
        if (StringUtil.isNotBlank(param.getParentId())) {
            FunctionInfoData parent = functionInfoService.getFunctionById(param.getParentId());
            if (null != parent) {
                param.setParentFunctionInfo(parent);
                param.setType(1);
            }else {
                param.setType(0);
            }
        }else {
            param.setType(0);
        }
        if (StringUtil.isNotBlank(param.getIconId())) {
            IconInfoData iconInfoData = iconInfoService.getIconInfoById(param.getIconId());
            param.setIconInfoData(iconInfoData);
        }
        param.setDeleteFlag(data.getDeleteFlag());
        
        Param2DataUtil.functionParam2Data(param, data);
        data.setUpdateTime(Calendar.getInstance());
        
        functionInfoService.saveOrUpdateFunctionInfoData(data);
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //删除功能信息
    @RequestMapping(value="/delfunctioninfo/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData delFunctionInfo(HttpServletRequest request,HttpServletResponse response,FunctionInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(param.getId())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("非法操作");
            return ajaxData;
        }
        FunctionInfoData data = functionInfoService.getFunctionById(param.getId());
        if (null == data) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("非法操作");
            return ajaxData;
        }
        
        if (data.getType() == 1) {//子功能
            UserFunctionInfoParam userFunctionInfoParam = new UserFunctionInfoParam();
            userFunctionInfoParam.setFunctionInfoData(data);
            List<UserFunctionInfoData> datas = functionInfoService.getUserFunctionInfoDatas(userFunctionInfoParam);
            if (null != data && datas.size() > 0) {
                for (UserFunctionInfoData userFunctionInfoData : datas) {
                    functionInfoService.delUserFunctionInfoData(userFunctionInfoData);
                }
            }
            //删除子功能
            data.setDeleteFlag(DeleteFlagEnum.DEL.getIndex());
            functionInfoService.saveOrUpdateFunctionInfoData(data);
            
            FunctionInfoData parent = data.getParentFunctionInfo();
            if (parent.getChildFunctionInfos().size() == 1) {
                //删除父功能
                parent.setDeleteFlag(DeleteFlagEnum.DEL.getIndex());
                functionInfoService.saveOrUpdateFunctionInfoData(parent);
            }
        }else {//父功能
            //获取子功能
            List<FunctionInfoData> datas = data.getChildFunctionInfos();
            for (FunctionInfoData functionInfoData : datas) {
                UserFunctionInfoParam userFunctionInfoParam = new UserFunctionInfoParam();
                userFunctionInfoParam.setFunctionInfoData(functionInfoData);
                List<UserFunctionInfoData> userFunctionInfoDatas = functionInfoService.getUserFunctionInfoDatas(userFunctionInfoParam);
                if (null != data && datas.size() > 0) {//删除子功能授权数据
                    for (UserFunctionInfoData userFunctionInfoData : userFunctionInfoDatas) {
                        functionInfoService.delUserFunctionInfoData(userFunctionInfoData);
                    }
                }
                //删除子功能
                functionInfoData.setDeleteFlag(DeleteFlagEnum.DEL.getIndex());
                functionInfoService.saveOrUpdateFunctionInfoData(functionInfoData);
            }
            //删除父功能
            data.setDeleteFlag(DeleteFlagEnum.DEL.getIndex());
            functionInfoService.saveOrUpdateFunctionInfoData(data);
        }
        param.setType(0);
        ajaxData = searchFunctionInfo2AjaxData(param, request);
        return ajaxData;
    }
    
    //恢复功能信息
    @RequestMapping(value="/openfunctioninfo/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData openFunctionInfo(HttpServletRequest request,HttpServletResponse response,FunctionInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(param.getId())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("非法操作");
            return ajaxData;
        }
        FunctionInfoData data = functionInfoService.getFunctionById(param.getId());
        if (null == data) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("非法操作");
            return ajaxData;
        }
        
        if (data.getType() == 1) {//子功能
            //恢复子功能
            data.setDeleteFlag(DeleteFlagEnum.UDEL.getIndex());
            functionInfoService.saveOrUpdateFunctionInfoData(data);
            FunctionInfoData parent = data.getParentFunctionInfo();
            //恢复子功能
            parent.setDeleteFlag(DeleteFlagEnum.UDEL.getIndex());
            functionInfoService.saveOrUpdateFunctionInfoData(parent);
            
            //将功能授权给管理员
            UserInfoData userInfoData = userInfoService.getUserInfoById("WebAppUtil.getUserId(request)");
            UserFunctionInfoData userFunctionInfoData = new UserFunctionInfoData();
            userFunctionInfoData.setFunctionInfoData(data);
            userFunctionInfoData.setUserInfoData(userInfoData);
            functionInfoService.saveOrUpdateUserFunctionInfoData(userFunctionInfoData);
        }else {//父功能
            //获取子功能
            List<FunctionInfoData> datas = data.getChildFunctionInfos();
            for (FunctionInfoData functionInfoData : datas) {
                //恢复子功能
                functionInfoData.setDeleteFlag(DeleteFlagEnum.UDEL.getIndex());
                functionInfoService.saveOrUpdateFunctionInfoData(functionInfoData);
                //将功能授权给管理员
                UserInfoData userInfoData = userInfoService.getUserInfoById("WebAppUtil.getUserId(request)");
                UserFunctionInfoData userFunctionInfoData = new UserFunctionInfoData();
                userFunctionInfoData.setFunctionInfoData(functionInfoData);
                userFunctionInfoData.setUserInfoData(userInfoData);
                functionInfoService.saveOrUpdateUserFunctionInfoData(userFunctionInfoData);
            }
            //恢复父功能
            data.setDeleteFlag(DeleteFlagEnum.UDEL.getIndex());
            functionInfoService.saveOrUpdateFunctionInfoData(data);
        }
        param.setType(0);
        ajaxData = searchFunctionInfo2AjaxData(param, request);
        return ajaxData;
    }
    
    //获取子功能
    @RequestMapping("/getchildfunctions/api")
    @ResponseBody
    @Transactional
    public AjaxData getChildFunctions(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        FunctionInfoParam param = new FunctionInfoParam();
        param.setType(1);
        //排序字段
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo2 = new OrderVO("order",true);
        orderVos.add(vo2);
        List<FunctionInfoData> datas = functionInfoService.getFunctions(param,orderVos);
        JSONArray jsonArray = new JSONArray();
        for(FunctionInfoData funData : datas){
            JSONObject json = new JSONObject();
            json.put("id", funData.getId());
            json.put("name", funData.getName());
            json.put("parentName", funData.getParentFunctionInfo().getName());
            json.put("path", funData.getPath());
            jsonArray.add(json);
        }
        
        ajaxData.setData(jsonArray);
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //最大序号 + 1
    @RequestMapping("/getmaxorder/api")
    @ResponseBody
    @Transactional
    public AjaxData getMaxOrder(HttpServletRequest request,HttpServletResponse response,FunctionInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isNotBlank(param.getParentId()) && !"undefined".equals(param.getParentId())) {
            FunctionInfoData data = functionInfoService.getFunctionById(param.getParentId());
            param.setParentFunctionInfo(data);
        }
        int max = functionInfoService.getMaxOrder(param);
        if (StringUtil.isNotBlank(param.getParentId()) && !"undefined".equals(param.getParentId())) {
            max += 1;
        }else {
            int temp = max / 10;
            max = (temp+1)*10 + 1;
        }
        ajaxData.setData(max);
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    
    //共用查询
    private AjaxData searchFunctionInfo2AjaxData(FunctionInfoParam param,HttpServletRequest request) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new FunctionInfoParam();
        }
        param.setId("");
        //排序字段
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo = new OrderVO("deleteFlag",true);
        orderVos.add(vo);
        OrderVO vo2 = new OrderVO("order",true);
        orderVos.add(vo2);
        
        List<FunctionInfoData> functionInfos = functionInfoService.getFunctions(param,orderVos);
        if (null != functionInfos) {
            ajaxData.setData(Data2JSONUtil.functionInfoDatas2JSONArray(functionInfos));
        }
        ajaxData.setFlag(true);
        return ajaxData;
    }
}
