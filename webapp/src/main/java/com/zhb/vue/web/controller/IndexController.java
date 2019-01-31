package com.zhb.vue.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zhb.forever.framework.util.StringUtil;
import com.zhb.vue.web.util.WebAppUtil;

@Controller
@RequestMapping(value="/htgl/indexcontroller")
public class IndexController {
    
    private Logger logger = LoggerFactory.getLogger(IndexController.class);
    
    @RequestMapping(value="/index",method=RequestMethod.GET)
    @Transactional
    public String index(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            logger.info("请登录");
            return "login/login";
        }
        return "htgl/index";
    }

}
