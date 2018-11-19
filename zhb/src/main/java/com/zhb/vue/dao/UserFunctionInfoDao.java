package com.zhb.vue.dao;

import java.util.List;

import com.zhb.vue.params.UserFunctionInfoParam;
import com.zhb.vue.pojo.UserFunctionInfoData;
import com.zhb.vue.pojo.UserInfoData;

public interface UserFunctionInfoDao {

    /**
     ** 新增或修改 人员功能关系
     * @param data
     */
    void saveOrUpdate(UserFunctionInfoData data);
    
    /**
     * *删除 人员功能关系
     * @param data
     */
    void delUserFunctionInfoData(UserFunctionInfoData data);
    
    /**
     * *根据用户信息 获取 用户功能关系
     * @param data
     * @return
     */
    List<UserFunctionInfoData> getDataByUser(UserInfoData data);
    
    /**
     * *获取 用户功能关系（授权）
     * @param param
     * @return
     */
    List<UserFunctionInfoData> getUserFunctionInfoDatas(UserFunctionInfoParam param);
    
    Long countUserFunctionInfos(UserFunctionInfoParam param);
    
    /**
     * *获取 用户功能关系（授权）,分页
     * @param param
     * @return
     */
    List<UserFunctionInfoData> getUserFunctionInfoDatasPage(UserFunctionInfoParam param);
    
}
