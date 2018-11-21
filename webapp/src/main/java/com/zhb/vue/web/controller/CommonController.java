package com.zhb.vue.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.PropertyUtil;
import com.zhb.vue.web.controller.base.BaseController;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年11月21日下午4:08:49
*/
@Controller
@RequestMapping("/commoncontroller")
public class CommonController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(CommonController.class);
    
    @RequestMapping(value="/getlogoname",method=RequestMethod.GET)
    @Transactional
    @ResponseBody
    public AjaxData getLogoName(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        ajaxData.setFlag(true);
        ajaxData.setData(PropertyUtil.getSystemLogoName());
        return ajaxData;
    }

}


