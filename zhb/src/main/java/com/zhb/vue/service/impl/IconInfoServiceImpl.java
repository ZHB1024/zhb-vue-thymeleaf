package com.zhb.vue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.dao.IconInfoDao;
import com.zhb.vue.params.IconInfoParam;
import com.zhb.vue.pojo.IconInfoData;
import com.zhb.vue.service.IconInfoService;
import com.zhb.vue.service.base.impl.CommonServiceImpl;

@Service
public class IconInfoServiceImpl  extends CommonServiceImpl implements IconInfoService {
    
    @Autowired
    private IconInfoDao iconInfoDao;

    @Override
    public void saveOrUpdate(IconInfoData data) {
        iconInfoDao.saveOrUpdate(data);
    }

    @Override
    public List<IconInfoData> getIconInfos(IconInfoParam param,List<OrderVO> orderVos) {
        return iconInfoDao.getIconInfos(param,orderVos);
    }

    @Override
    public IconInfoData getIconInfoById(String id) {
        return iconInfoDao.getIconInfoById(id);
    }

}
