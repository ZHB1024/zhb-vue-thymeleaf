package com.zhb.vue.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhb.vue.Constant;
import com.zhb.vue.web.util.WebAppUtil;
import com.zhb.vue.web.vo.LoginInfoVO;

public class LoginFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        LoginInfoVO loginUserVO = WebAppUtil.getLoginInfoVO(request);
        if (null == loginUserVO || null != loginUserVO.getUserInfoVO()) {
            String ctxPath = request.getContextPath();
            request.setAttribute(Constant.REQUEST_ERROR, "登陆后才能访问系统");
            request.setAttribute("redirectUrl", request.getRequestURL());
            HttpServletResponse response = (HttpServletResponse) arg1;
            //请求转发,共享的是同一个request，整个过程是一个请求，一个响应。
            //能传参数
            request.getRequestDispatcher(ctxPath + "/logincontroller/tologin").forward(request, response);
            //request.getRequestDispatcher(ctxPath + "/errorcontroller/toerror").forward(request, response);
            
            //传不过去参数
            // request.getRequestDispatcher(ctxPath + "/error/index.html").forward(request,response);
            
            //重定向
            //response.sendRedirect(request.getContextPath() + "/error/index.html");
            return;
        }
        arg2.doFilter(request, arg1);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}
