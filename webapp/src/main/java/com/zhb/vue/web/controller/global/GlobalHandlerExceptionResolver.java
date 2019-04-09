package com.zhb.vue.web.controller.global;

import com.zhb.vue.Constant;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**

*@author   zhanghb

*date 2018年10月18日上午10:22:22

*/
@Component
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {
    
    private static Logger logger = LoggerFactory.getLogger(GlobalHandlerExceptionResolver.class);
    
    private int order = Ordered.HIGHEST_PRECEDENCE;
    
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception exception) {
        logger.error(exception.getMessage());
        exception.printStackTrace();
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(Constant.REQUEST_ERROR, exception.getMessage());
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


