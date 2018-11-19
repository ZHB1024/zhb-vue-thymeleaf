package com.zhb.vue.web.timertask.task;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhb.forever.framework.dic.DeleteFlagEnum;
import com.zhb.vue.params.VerificationCodeInfoParam;
import com.zhb.vue.pojo.VerificationCodeInfoData;
import com.zhb.vue.service.ServiceFactory;
import com.zhb.vue.service.VerificationCodeInfoService;

public class DeleteVerificationCodeTask implements Runnable {
    
    private Logger logger = LoggerFactory.getLogger(DeleteVerificationCodeTask.class);
    
    private Integer beforeTime = 0;
    private VerificationCodeInfoService verificationCodeInfoService;

    
    public DeleteVerificationCodeTask(Integer beforeTime ) {
        this.beforeTime = beforeTime;
        verificationCodeInfoService = ServiceFactory.getVerificationCodeInfoService();
    }
    
    @Override
    public void run() {
        try {
            VerificationCodeInfoParam param = new VerificationCodeInfoParam();
            param.setDeleteFlag(DeleteFlagEnum.UDEL.getIndex());
            
            Calendar now = Calendar.getInstance();
            now.add(Calendar.MINUTE, beforeTime);// beforTime分钟之前的时间
            param.setCreateTime(now);
            List<VerificationCodeInfoData> datas = verificationCodeInfoService.getVerificationCodes(param, null);
            if (null != datas && datas.size() > 0) {
                logger.info("*******delete VerificationCode*******" + datas.size() + "个");
                for (VerificationCodeInfoData verificationCodeInfoData : datas) {
                    verificationCodeInfoData.setDeleteFlag(DeleteFlagEnum.DEL.getIndex());
                    verificationCodeInfoData.setUpdateTime(Calendar.getInstance());
                    verificationCodeInfoService.saveOrUpdate(verificationCodeInfoData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
