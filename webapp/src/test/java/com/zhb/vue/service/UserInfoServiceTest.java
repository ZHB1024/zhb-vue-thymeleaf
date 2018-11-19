package com.zhb.vue.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.zhb.vue.base.BaseTest;
import com.zhb.vue.params.UserInfoParam;
import com.zhb.vue.pojo.UserInfoData;

public class UserInfoServiceTest extends BaseTest {
    
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Test
    @Transactional
    @Rollback(true)
    public void getParamsTest() {
        UserInfoParam userInfoParam = new UserInfoParam();
        userInfoParam.setUserName("root");
        List<UserInfoData> datas = userInfoService.getUserInfos(userInfoParam,null);
        if (null != datas) {
            for (UserInfoData userInfoData : datas) {
                System.out.println(userInfoData.getRealName());
            }
        }
    }

}
