package com.zhb.vue.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.zhb.vue.Constant;

public class ServiceFactory {
    
    private static ClassPathXmlApplicationContext beanFac = new ClassPathXmlApplicationContext(Constant.APPLICATIONCONTEXT_CONF);
    
    private static WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
    
    public static Object getBean(String beanId){
        return beanFac.getBean(beanId);
    }
    
    public static VerificationCodeInfoService getVerificationCodeInfoService() {
        VerificationCodeInfoService verificationCodeInfoService = beanFac.getBean("verificationCodeInfoServiceImpl", VerificationCodeInfoService.class);
        return verificationCodeInfoService;
    }
    
    public static AttachmentInfoService getAttachmentInfoService() {
        AttachmentInfoService attachmentInfoService = beanFac.getBean("attachmentInfoServiceImpl", AttachmentInfoService.class);
        return attachmentInfoService;
    }
    
    public static DicInfoService getDicInfoService() {
        DicInfoService dicInfoService = wac.getBean("dicInfoServiceImpl", DicInfoService.class);
        return dicInfoService;
    }
    
    public static YXInfoService getYXInfoService() {
        YXInfoService yxInfoService = wac.getBean("YXInfoServiceImpl", YXInfoService.class);
        return yxInfoService;
    }

}
