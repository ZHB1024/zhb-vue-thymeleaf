package com.zhb.vue.web.util;

import javax.servlet.http.HttpServletRequest;

import com.zhb.forever.framework.vo.UserInfoVO;
import com.zhb.vue.web.vo.LoginInfoVO;

public class FlushSessionUtil {

    //刷新用户信息缓存
    public static void flushWebAppUserInfo(HttpServletRequest request,UserInfoVO vo) {
        LoginInfoVO loginInfoVO = WebAppUtil.getLoginInfoVO(request);
        loginInfoVO.setUserInfoVO(vo);
        WebAppUtil.setLogInfoVO(request, loginInfoVO);
    }

}
