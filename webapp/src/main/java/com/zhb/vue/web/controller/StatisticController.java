package com.zhb.vue.web.controller;

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

import com.alibaba.fastjson.JSONObject;
import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.vue.service.AttachmentInfoService;
import com.zhb.vue.service.DicInfoService;
import com.zhb.vue.service.UserInfoService;
import com.zhb.vue.web.util.Data2JSONUtil;
import com.zhb.vue.web.util.WebAppUtil;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月22日下午4:56:11
*/

@Controller
@RequestMapping("/htgl/statisticController")
public class StatisticController {

    private Logger logger = LoggerFactory.getLogger(StatisticController.class);
    
    @Autowired
    private AttachmentInfoService attachmentInfoService;
    @Autowired
    private DicInfoService dicInfoService;
    
    //toindex
    @RequestMapping(value = "/toindex",method = RequestMethod.GET)
    @Transactional
    public String toIndex(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return "login/login";
        }
        return "htgl/statistic/attachment";
    }
    
    //统计附件
    @RequestMapping("/statisticattachment/api")
    @ResponseBody
    @Transactional
    public AjaxData statisticAttachment(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        List<Object[]> results = attachmentInfoService.statisticAttachment();
        JSONObject jsonObject = Data2JSONUtil.statisticAttachment2JSONObject("附件类别统计汇总", results);
        ajaxData.setFlag(true);
        ajaxData.setData(jsonObject);
        return ajaxData;
    }
    
    //toindex
    @RequestMapping(value = "/todicindex",method = RequestMethod.GET)
    @Transactional
    public String toDicIndex(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return "login/login";
        }
        return "htgl/statistic/dic";
    }
    
    //统计字典项
    @RequestMapping("/statisticdic/api")
    @ResponseBody
    @Transactional
    public AjaxData statisticDic(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        List<Object[]> results = dicInfoService.statisticDic();
        JSONObject jsonObject = Data2JSONUtil.statisticDic2JSONObject("字典项统计汇总", results);
        ajaxData.setFlag(true);
        ajaxData.setData(jsonObject);
        return ajaxData;
    }

}


