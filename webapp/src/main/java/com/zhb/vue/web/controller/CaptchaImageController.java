package com.zhb.vue.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhb.forever.captcha.jcaptcha.client.JCaptchaClient;
import com.zhb.forever.captcha.CaptchaClientFactory;
import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.vue.web.controller.base.BaseController;
import com.zhb.vue.web.util.WebAppUtil;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年11月15日下午4:07:36
*/

@Controller
@RequestMapping("/htgl/captchaimagecontroller")
public class CaptchaImageController extends BaseController {
    
    private JCaptchaClient jcaptchaClient = CaptchaClientFactory.getJCaptchaClientBean();
    
    //toindex
    @RequestMapping(value = "/toindex",method = RequestMethod.GET)
    @Transactional
    public String toIndex(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return "login.index";
        }
        return "htgl.captcha.index";
    }
    
     //获取验证码
    @RequestMapping(value = "/getcaptchaimage/api")
    @Transactional
    public void getCaptchaImage(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return ;
        }
        
        String tokenId = request.getSession().getId();
        jcaptchaClient.generateCaptchaImage(tokenId, request, response);
    }
    
    //验证-验证码
    @RequestMapping(value = "/validatecaptchaimage/api")
    @ResponseBody
    @Transactional
    public AjaxData validateCaptchaImage(HttpServletRequest request,HttpServletResponse response,String captcha) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请登录");
            return ajaxData;
        }
        
        String tokenId = request.getSession().getId();
        ajaxData = jcaptchaClient.validateCaptchaImage(captcha, tokenId);
        return ajaxData;
    }

}


