package com.zhb.vue.dao;

import java.util.List;

import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.params.FunctionInfoParam;
import com.zhb.vue.pojo.FunctionInfoData;

public interface FunctionInfoDao {
    
    /**
     * *新增或修改功能
     * @param data
     */
    void saveOrUpdate(FunctionInfoData data);
    
    /**
     * *删除 功能
     * @param data
     */
    void delFunctionInfoData(FunctionInfoData data);
    
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
     * * @param param
     * @return
     */
    int getMaxOrder(FunctionInfoParam param);
    
    
}
