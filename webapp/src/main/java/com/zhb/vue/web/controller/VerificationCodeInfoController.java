package com.zhb.vue.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.page.PageUtil;
import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.KeyValueVO;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.dic.VerificationCodeTypeEnum;
import com.zhb.vue.params.VerificationCodeInfoParam;
import com.zhb.vue.pojo.VerificationCodeInfoData;
import com.zhb.vue.service.VerificationCodeInfoService;
import com.zhb.vue.web.util.Data2JSONUtil;
import com.zhb.vue.web.util.WebAppUtil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequestMapping("/htgl/verificationcodeinfocontroller")
public class VerificationCodeInfoController {
    
    private Logger logger = LoggerFactory.getLogger(VerificationCodeInfoController.class);
    
    @Autowired
    private VerificationCodeInfoService verificationCodeInfoService;
    
    //toindex
    @RequestMapping(value = "/toindex",method = RequestMethod.GET)
    @Transactional
    public String toIndex(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return "login/login";
        }
        return "htgl/verification/index";
    }
    
    //查询
    @RequestMapping(value = "/getverificationcodeinfo/api")
    @ResponseBody
    @Transactional
    public AjaxData getVerificationCodeInfo(HttpServletRequest request,HttpServletResponse response,VerificationCodeInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        ajaxData = searchVerificationCodeInfo2AjaxData(param);
        return ajaxData;
    }
    
    //查询,分页
    @RequestMapping(value = "/getverificationcodeinfopage/api")
    @ResponseBody
    @Transactional
    public AjaxData getVerificationCodeInfoPage(HttpServletRequest request,HttpServletResponse response,VerificationCodeInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        ajaxData = searchVerificationCodeInfo2AjaxDataPage(param);
        return ajaxData;
    }
    
    //验证码类别
    @RequestMapping(value = "/getverificationcodetype/api")
    @ResponseBody
    @Transactional
    public AjaxData getVerificationCodeType(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        List<KeyValueVO> vos = VerificationCodeTypeEnum.getAll();
        ajaxData.setFlag(true);
        ajaxData.setData(vos);
        return ajaxData;
    }
    
    //共用查询,不分页
    private AjaxData searchVerificationCodeInfo2AjaxData(VerificationCodeInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new VerificationCodeInfoParam();
        }
        
        //排序字段
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo = new OrderVO("deleteFlag",true);
        orderVos.add(vo);
        OrderVO vo2 = new OrderVO("type",true);
        orderVos.add(vo2);
        OrderVO vo3 = new OrderVO("updateTime",false);
        orderVos.add(vo3);
        
        List<VerificationCodeInfoData> verificationCodeInfos = verificationCodeInfoService.getVerificationCodes(param,orderVos);
        if (null != verificationCodeInfos) {
            ajaxData.setData(Data2JSONUtil.verificationCodeInfoDatas2JSONArray(verificationCodeInfos));
        }
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //共用查询,分页
    private AjaxData searchVerificationCodeInfo2AjaxDataPage(VerificationCodeInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new VerificationCodeInfoParam();
        }
        //排序字段
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo = new OrderVO("deleteFlag",true);
        orderVos.add(vo);
        OrderVO vo2 = new OrderVO("type",true);
        orderVos.add(vo2);
        OrderVO vo3 = new OrderVO("updateTime",false);
        orderVos.add(vo3);
        
        //设置分页信息
        if(null == param.getCurrentPage()){
            param.setCurrentPage(1);
        }
        if(null == param.getPageSize()){
            param.setPageSize(PageUtil.PAGE_SIZE);
        }
        param.setStart(param.getPageSize()*(param.getCurrentPage()-1));
        
        Page<VerificationCodeInfoData> page = verificationCodeInfoService.getVerificationCodeInfosPage(param,orderVos);
        JSONObject jsonObject = new JSONObject();
        if (null != page) {
            JSONArray workDayJson = Data2JSONUtil.verificationCodeInfoDatas2JSONArray(page.getList());
            jsonObject = PageUtil.pageInfo2JSON(page.getTotalCount(), page.getPageCount(), page.getCurrrentPage(), workDayJson);
        }else{
            jsonObject = PageUtil.pageInfo2JSON(0,param.getPageSize(),1,new JSONArray());
        }
        ajaxData.setFlag(true);
        ajaxData.setData(jsonObject);
        return ajaxData;
    }
    
    
}
