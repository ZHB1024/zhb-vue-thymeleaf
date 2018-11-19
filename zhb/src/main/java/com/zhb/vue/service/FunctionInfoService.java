package com.zhb.vue.service;

import java.util.List;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.params.FunctionInfoParam;
import com.zhb.vue.params.UserFunctionInfoParam;
import com.zhb.vue.pojo.FunctionInfoData;
import com.zhb.vue.pojo.UserFunctionInfoData;
import com.zhb.vue.pojo.UserInfoData;
import com.zhb.vue.service.base.CommonService;

public interface FunctionInfoService extends CommonService{
    
    /**
     * *新增或修改功能
     * @param data
     */
    FunctionInfoData saveOrUpdateFunctionInfoData(FunctionInfoData data);
    
    /**
     * *删除 功能
     * @param data
     */
    void delFunctionInfoData(FunctionInfoData data);
    
    /**
     * *新增或修改 人员功能关系
     * @param data
     */
    UserFunctionInfoData saveOrUpdateUserFunctionInfoData(UserFunctionInfoData data);
    
    /**
     * *删除 人员功能关系
     * @param data
     */
    void delUserFunctionInfoData(UserFunctionInfoData data);
    
    
    /**
     * *获取功能
     * @param param
     * @return
     */
    List<FunctionInfoData> getFunctions(FunctionInfoParam param,List<OrderVO> orderVos);
    
    /**
     * *获取所有的功能
     * @param param
     * @return
     */
    List<FunctionInfoData> getAllFunctions(List<OrderVO> orderVos);
    
    /**
     * *获取功能,根据id
     * @param id
     * @return
     */
    FunctionInfoData getFunctionById(String id);
    
    /**
     * *获取最大排序号
     * @return
     */
    int getMaxOrder(FunctionInfoParam param);
    
    
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
    
    /**
     * *获取 用户功能关系（授权）,分页
     * @param param
     * @return
     */
    Page<UserFunctionInfoData>  getUserFunctionInfoDatasPage(UserFunctionInfoParam param);
    
    

}
