package com.zhb.vue.service;

import java.util.List;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.params.DicInfoParam;
import com.zhb.vue.pojo.DicInfoData;
import com.zhb.vue.service.base.CommonService;

public interface DicInfoService extends CommonService{
    
    void saveOrUpdate(DicInfoData data);
    
    void saveOrUpdate(List<DicInfoData> datas);
    
    List<DicInfoData> getDicInfos(DicInfoParam param,List<OrderVO> orderVos);
    
    Page<DicInfoData> getDicInfosPage(DicInfoParam param,List<OrderVO> orderVos);
    
    List<String> getDicCategory();
    
    List<String> getDicTypeByCategory(DicInfoParam param);
    
    /**
     * *统计
     */
    List<Object[]> statisticDic();

}
