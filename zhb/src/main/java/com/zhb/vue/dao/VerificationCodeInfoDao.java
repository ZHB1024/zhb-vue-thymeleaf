package com.zhb.vue.dao;

import java.util.List;

import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.params.VerificationCodeInfoParam;
import com.zhb.vue.pojo.VerificationCodeInfoData;

public interface VerificationCodeInfoDao {
    
    void saveOrUpdate(VerificationCodeInfoData data);
    
    List<VerificationCodeInfoData> getVerificationCodes(VerificationCodeInfoParam param,List<OrderVO> orderVos);
    
    Long getVerificationCodesPageCount(VerificationCodeInfoParam param);

    List<VerificationCodeInfoData> getVerificationCodesPage(VerificationCodeInfoParam param,List<OrderVO> orderVos);
    
    void delete(VerificationCodeInfoData data);

}
