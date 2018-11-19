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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.page.PageUtil;
import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.params.UserFunctionInfoParam;
import com.zhb.vue.params.UserInfoParam;
import com.zhb.vue.pojo.FunctionInfoData;
import com.zhb.vue.pojo.UserFunctionInfoData;
import com.zhb.vue.pojo.UserInfoData;
import com.zhb.vue.service.FunctionInfoService;
import com.zhb.vue.service.UserInfoService;
import com.zhb.vue.web.util.Data2JSONUtil;
import com.zhb.vue.web.util.Param2DataUtil;
import com.zhb.vue.web.util.WebAppUtil;

@Controller
@RequestMapping("/htgl/authoritycontroller")
public class AuthorityController {

    private Logger logger = LoggerFactory.getLogger(AuthorityController.class);
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private FunctionInfoService functionInfoService;
    
    //左侧功能菜单
    @RequestMapping("/getfunctions/api")
    @ResponseBody
    @Transactional
    public AjaxData getFunctions(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        UserInfoParam userInfoParam = new UserInfoParam();
        userInfoParam.setId(WebAppUtil.getUserId(request));
        List<UserInfoData> userInfoData = userInfoService.getUserInfos(userInfoParam,null);
        List<UserFunctionInfoData> datas = functionInfoService.getDataByUser(userInfoData.get(0));
        JSONArray jsonArray = Data2JSONUtil.generateJSonArray(datas);
        
        ajaxData.setData(jsonArray);
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //toindex
    @RequestMapping(value="/toindex",method=RequestMethod.GET)
    @Transactional
    public String toAuthority(HttpServletRequest request,HttpServletResponse response) {
        return "htgl.authority.index";
    }
    
    //查询授权信息
    @RequestMapping("/getauthority/api")
    @ResponseBody
    @Transactional
    public AjaxData getAuthority(HttpServletRequest request,HttpServletResponse response,UserFunctionInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        
        ajaxData = searchAuthority2AjaxData(param);
        return ajaxData;
    }
    
    //查询授权信息,分页
    @RequestMapping("/getauthoritypage/api")
    @ResponseBody
    @Transactional
    public AjaxData getAuthorityPage(HttpServletRequest request,HttpServletResponse response,UserFunctionInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        
        ajaxData = searchAuthority2AjaxDataPage(param);
        return ajaxData;
    }
    
    //to授权页
    @RequestMapping(value="/toadd",method=RequestMethod.GET)
    @Transactional
    public String toadd(HttpServletRequest request,HttpServletResponse response) {
        return "htgl.authority.add";
    }
    
    //新增授权
    @RequestMapping("/addauthority/api")
    @ResponseBody
    @Transactional
    public AjaxData addAuthority(HttpServletRequest request,HttpServletResponse response,UserFunctionInfoParam param,boolean opt) {
        AjaxData ajaxData = new AjaxData();
        
        if (StringUtil.isBlank(param.getUserId())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请选择用户");
            return ajaxData;
        }
        if (StringUtil.isBlank(param.getFunctionId())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请选择功能");
            return ajaxData;
        }
        
        UserInfoParam userInfoParam = new UserInfoParam();
        userInfoParam.setId(param.getUserId());
        List<UserInfoData> userInfoData = userInfoService.getUserInfos(userInfoParam,null);
        param.setUserInfoData(userInfoData.get(0));
        
        String[] functionIds = param.getFunctionId().split(",");
        for (String funId : functionIds) {
            FunctionInfoData functionInfoData = functionInfoService.getFunctionById(funId);
            param.setFunctionInfoData(functionInfoData);
            
            List<UserFunctionInfoData> userFunctionInfoDatas = functionInfoService.getUserFunctionInfoDatas(param);
            if (null == userFunctionInfoDatas || userFunctionInfoDatas.size() == 0) {
                if (opt) {//新增
                    UserFunctionInfoData data = new UserFunctionInfoData();
                    Param2DataUtil.userFunctionParam2Data(param, data);
                    data.setCreateTime(Calendar.getInstance());
                    functionInfoService.saveOrUpdateUserFunctionInfoData(data);
                }
            }else {
                if (!opt) {//删除
                    functionInfoService.delUserFunctionInfoData(userFunctionInfoDatas.get(0));
                }
            }
        }
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //删除授权
    @RequestMapping("/delauthority/api")
    @ResponseBody
    @Transactional
    public AjaxData delAuthority(HttpServletRequest request,HttpServletResponse response,UserFunctionInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        
        if (StringUtil.isBlank(param.getId())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请选择数据");
            return ajaxData;
        }
        
        List<UserFunctionInfoData> datas = functionInfoService.getUserFunctionInfoDatas(param);
        if (null == datas || datas.size() == 0) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("非法请求");
            return ajaxData;
        }
        functionInfoService.delUserFunctionInfoData(datas.get(0));
        
        ajaxData = searchAuthority2AjaxDataPage(param);
        return ajaxData;
    }
    
    //公共查询，不分页
    private AjaxData searchAuthority2AjaxData(UserFunctionInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new UserFunctionInfoParam();
        }
        
        param.setId("");
        
        if (StringUtil.isNotBlank(param.getUserId())&&!"undefined".equals(param.getUserId())) {
            UserInfoParam userInfoParam = new UserInfoParam();
            userInfoParam.setId(param.getUserId());
            List<UserInfoData> userInfoData = userInfoService.getUserInfos(userInfoParam,null);
            param.setUserInfoData(userInfoData.get(0));
        }else {
            param.setUserId("");
        }
        
        if (StringUtil.isNotBlank(param.getFunctionId())&&!"undefined".equals(param.getFunctionId())) {
            FunctionInfoData functionInfoData = functionInfoService.getFunctionById(param.getFunctionId());
            param.setFunctionInfoData(functionInfoData);
        }else {
            param.setFunctionId("");
        }
        
        List<UserFunctionInfoData> datas = functionInfoService.getUserFunctionInfoDatas(param);
        
        ajaxData.setData(Data2JSONUtil.userFunctionInfoDatas2JSONObject(datas));
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //共用查询,分页
    private AjaxData searchAuthority2AjaxDataPage(UserFunctionInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new UserFunctionInfoParam();
        }
        
        param.setId("");
        
        if (StringUtil.isNotBlank(param.getUserId())&&!"undefined".equals(param.getUserId())) {
            UserInfoParam userInfoParam = new UserInfoParam();
            userInfoParam.setId(param.getUserId());
            List<UserInfoData> userInfoData = userInfoService.getUserInfos(userInfoParam,null);
            param.setUserInfoData(userInfoData.get(0));
        }else {
            param.setUserId("");
        }
        
        if (StringUtil.isNotBlank(param.getFunctionId())&&!"undefined".equals(param.getFunctionId())) {
            FunctionInfoData functionInfoData = functionInfoService.getFunctionById(param.getFunctionId());
            param.setFunctionInfoData(functionInfoData);
        }else {
            param.setFunctionId("");
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
        
        Page<UserFunctionInfoData> page = functionInfoService.getUserFunctionInfoDatasPage(param);
        JSONObject jsonObject = new JSONObject();
        if (null != page) {
            JSONArray workDayJson = Data2JSONUtil.userFunctionInfoDatas2JSONObject(page.getList());
            jsonObject = PageUtil.pageInfo2JSON(page.getTotalCount(), page.getPageCount(), page.getCurrrentPage(), workDayJson);
        }else{
            jsonObject = PageUtil.pageInfo2JSON(0,param.getPageSize(),1,new JSONArray());
        }
        ajaxData.setFlag(true);
        ajaxData.setData(jsonObject);
        return ajaxData;
    }
}
