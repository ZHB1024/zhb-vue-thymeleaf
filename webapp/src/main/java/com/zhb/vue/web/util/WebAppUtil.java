package com.zhb.vue.web.util;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

import com.zhb.vue.Constant;
import com.zhb.vue.web.vo.LoginInfoVO;

public class WebAppUtil {
    
    /**
       * 获取登录对象信息
     * @return
     */
    public static LoginInfoVO getLoginInfoVO(HttpServletRequest request) {
        return (LoginInfoVO) WebUtils.getSessionAttribute(request, Constant.SESSION_LOGIN_USER_VO);
    }

    /**
     * 设置登录对象信息
     * @return
     */
    public static void setLogInfoVO(HttpServletRequest request, LoginInfoVO data) {
        WebUtils.setSessionAttribute(request, Constant.SESSION_LOGIN_USER_VO, data);
    }
    
    /**
     * 获取用户UserId
     * @return
     */
    public static String getUserId(HttpServletRequest request) {
        return (String) WebUtils.getSessionAttribute(request,Constant.SESSION_USER_ID);
    }
    
    /**
         * 设置用户UserId
     * @return
     */
    public static void setUserId(HttpServletRequest request,String userId) {
         WebUtils.setSessionAttribute(request, Constant.SESSION_USER_ID, userId);
    }
    
    
    //退出系统
    public static void exit(HttpServletRequest request){
        request.getSession().removeAttribute(Constant.SESSION_LOGIN_USER_VO);
        request.getSession().removeAttribute(Constant.SESSION_USER_ID);
    }


    public static String getIp(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
    
    public static String getRootPath(HttpServletRequest request){
        return request.getSession().getServletContext().getRealPath("/");
    }

}
