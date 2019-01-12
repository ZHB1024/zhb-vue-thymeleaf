package com.zhb.vue.web.controller.global;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.zhb.forever.framework.exception.ExceptionUtil;
import com.zhb.forever.framework.util.AjaxData;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/**

*@author   zhanghb

*date 2018年10月18日上午10:22:22

*/
@Component
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {
    
    private static Logger logger = LoggerFactory.getLogger(GlobalHandlerExceptionResolver.class);
    
    private int order = Ordered.HIGHEST_PRECEDENCE;
    
    @Autowired
    public GlobalHandlerExceptionResolver() {
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception exception) {
        
        String exceptionMessage = ExceptionUtil.getExceptionMessage(exception);
        logger.error(exceptionMessage);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("errorMessage", exception.getMessage());
        return new ModelAndView("error/index", model);
    }
    
    /**
     * *判断是否ajax请求
     *
     * @param request 请求对象
     * @return true:ajax请求  false:非ajax请求
     */
    private boolean isAjax(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        String ajaxParam = request.getParameter("ajaxSource");
        String requestWith = request.getHeader("X-Requested-With");
        logger.info(acceptHeader);
        logger.info(ajaxParam);
        logger.info(requestWith);
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    
    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}


