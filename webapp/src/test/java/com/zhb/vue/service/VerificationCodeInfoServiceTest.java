package com.zhb.vue.service;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.zhb.forever.framework.dic.DeleteFlagEnum;
import com.zhb.vue.base.BaseTest;
import com.zhb.vue.params.VerificationCodeInfoParam;
import com.zhb.vue.pojo.VerificationCodeInfoData;

public class VerificationCodeInfoServiceTest extends BaseTest {
    
    @Autowired
    private VerificationCodeInfoService verificationCodeInfoService;
    
    @Test
    @Transactional
    @Rollback(true)
    public void getVerificationCodes() {
        VerificationCodeInfoParam param = new VerificationCodeInfoParam();
        param.setDeleteFlag(DeleteFlagEnum.UDEL.getIndex());
        
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -50);// beforTime分钟之前的时间
        param.setCreateTime(now);
        List<VerificationCodeInfoData> datas = verificationCodeInfoService.getVerificationCodes(param, null);
        if (null != datas && datas.size() > 0) {
            System.out.println("***********" + datas.size() + " 个");
            for (VerificationCodeInfoData verificationCodeInfoData : datas) {
                System.out.println("***********" + verificationCodeInfoData.getCreateTime() + " 个");
            }
        }
    }
    
}
