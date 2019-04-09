package com.zhb.vue.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhb.vue.Constant;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2019年4月9日上午10:49:55
*/

@Controller
@RequestMapping(value="/errorcontroller")
public class ErrorController {

    private final Logger logger = LoggerFactory.getLogger(ErrorController.class);
    
  //error
    @RequestMapping("/toerror")
    @Transactional(readOnly=true)
    public String toError(HttpServletRequest request,HttpServletResponse response){
        logger.info(String.valueOf(request.getAttribute(Constant.REQUEST_ERROR)));
        return "error/index";
    }

}


