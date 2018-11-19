package com.zhb.vue.service;

import java.util.List;

import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.params.IconInfoParam;
import com.zhb.vue.pojo.IconInfoData;
import com.zhb.vue.service.base.CommonService;

public interface IconInfoService extends CommonService{
    
    /**
     * *新增或修改 图标
     * @param data
     */
    void saveOrUpdate(IconInfoData data);
    
    /**
     * *获取 图标
     * @param param
     */
    List<IconInfoData> getIconInfos(IconInfoParam param,List<OrderVO> orderVos);
    
    /**
     * *获取 图标,根据id
     * 
     * @param id
     */
    IconInfoData getIconInfoById(String id);

}
