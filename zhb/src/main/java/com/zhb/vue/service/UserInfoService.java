package com.zhb.vue.service;

import java.util.List;

import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.params.UserInfoParam;
import com.zhb.vue.pojo.UserInfoData;
import com.zhb.vue.service.base.CommonService;

public interface UserInfoService extends CommonService{
    
    /**
     * *新增或修改用户
     * @param data
     */
    UserInfoData saveOrUpdate(UserInfoData data);
    
    /**
     ** 获取用户
     * @param userInfoParam
     */
    List<UserInfoData> getUserInfos(UserInfoParam userInfoParam,List<OrderVO> orderVos);
    
    /**
     ** 获取所有的用户
     * @param orderVos
     */
    List<UserInfoData> getAllUserInfos(List<OrderVO> orderVos);

    /**
     ** 获取用户,根据id
     * @param id
     */
    UserInfoData getUserInfoById(String id);


}
