package com.zhb.vue.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.zhb.vue.base.BaseTest;
import com.zhb.vue.params.IconInfoParam;
import com.zhb.vue.pojo.IconInfoData;

public class IconInfoServiceTest extends BaseTest {
    
    @Autowired
    private IconInfoService iconInfoService;
    
    @Test
    @Transactional
    @Rollback(true)
    public void saveOrUpdateTest() {
        IconInfoData data = new IconInfoData();
        data.setName("授权管理");
        data.setValue("logo-designernews");
        data.setCreateUserId("8cjyg3qe735dtdcf");
        iconInfoService.saveOrUpdate(data);
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void getIconInfoTest() {
        IconInfoParam param = new IconInfoParam();
        param.setName("授权管理");
        List<IconInfoData> datas = iconInfoService.getIconInfos(param,null);
        if (null != datas) {
            for (IconInfoData iconInfoData : datas) {
                System.out.println(iconInfoData.getId() + "--" + iconInfoData.getName() + "--" + iconInfoData.getValue());
            }
        }
    }

}
