package com.zhb.vue.dao;

import java.util.List;

import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.params.UserInfoParam;
import com.zhb.vue.pojo.UserInfoData;

public interface UserInfoDao {
    
    /**
     * *新增或修改用户
     * @param data
     */
    void saveOrUpdate(UserInfoData data);
    
    /**
     * *获取用户
     * @param userInfoParam
     */
    List<UserInfoData> getUserInfos(UserInfoParam userInfoParam,List<OrderVO> orders);
    
    /**
     ** 获取所有的用户
     * @param orderVos
     */
    List<UserInfoData> getAllUserInfos(List<OrderVO> orderVos);

    
    /**
     * *获取用户,根据id
     * @param id
     */
    UserInfoData getUserInfoById(String id);

}
