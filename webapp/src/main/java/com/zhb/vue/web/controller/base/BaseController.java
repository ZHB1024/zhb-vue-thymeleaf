package com.zhb.vue.web.controller.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.zhb.forever.framework.util.AjaxData;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年11月14日下午2:04:40
*/

public abstract class BaseController {
    
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    protected AjaxData errors(BindingResult result) {
        StringBuilder builder = new StringBuilder();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                builder.append(error.getDefaultMessage()).append("；");
            }
        }
        return returnError(builder.toString());
    }
    
    protected AjaxData returnError(Object msg) {
        AjaxData ajaxData = new AjaxData();
        ajaxData.setFlag(false);
        ajaxData.addMessage(msg.toString());
        return ajaxData;
    }
    
    protected AjaxData returnSuccess(Object msg) {
        AjaxData ajaxData = new AjaxData();
        ajaxData.setFlag(true);
        ajaxData.setData(msg);
        return ajaxData;
    }

}


