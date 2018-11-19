package com.zhb.vue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.page.PageUtil;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.dao.VerificationCodeInfoDao;
import com.zhb.vue.params.VerificationCodeInfoParam;
import com.zhb.vue.pojo.VerificationCodeInfoData;
import com.zhb.vue.service.VerificationCodeInfoService;
import com.zhb.vue.service.base.impl.CommonServiceImpl;

@Service
public class VerificationCodeInfoServiceImpl extends CommonServiceImpl  implements VerificationCodeInfoService {

    @Autowired
    private VerificationCodeInfoDao verificationCodeInfoDao;
    
    @Override
    @Transactional
    public void saveOrUpdate(VerificationCodeInfoData data) {
        verificationCodeInfoDao.saveOrUpdate(data);
    }

    @Override
    @Transactional(readOnly=true)
    public List<VerificationCodeInfoData> getVerificationCodes(VerificationCodeInfoParam param,
            List<OrderVO> orderVos) {
        return verificationCodeInfoDao.getVerificationCodes(param, orderVos);
    }

    @Override
    @Transactional
    public void delete(List<VerificationCodeInfoData> datas) {
        if (null != datas && datas.size() > 0) {
            for (VerificationCodeInfoData verificationCodeInfoData : datas) {
                verificationCodeInfoDao.delete(verificationCodeInfoData);
            }
        }
    }

    @Override
    public Page<VerificationCodeInfoData> getVerificationCodeInfosPage(VerificationCodeInfoParam param,
            List<OrderVO> orderVos) {
        long total = verificationCodeInfoDao.getVerificationCodesPageCount(param);
        if (total > 0 ) {
            List<VerificationCodeInfoData> verificationCodeInfoDatas = verificationCodeInfoDao.getVerificationCodesPage(param,orderVos);
            //上一页可能有数据
            if ((null == verificationCodeInfoDatas || verificationCodeInfoDatas.size() == 0) && param.getCurrentPage() > 1) {
                param.setStart(param.getPageSize() * (param.getCurrentPage()-2));
                verificationCodeInfoDatas = verificationCodeInfoDao.getVerificationCodesPage(param,orderVos);
            }
            Page<VerificationCodeInfoData> page = PageUtil.getPage(verificationCodeInfoDatas.iterator(), param.getStart(), param.getPageSize(), total);
            return page;
        }
        return null;
    }
    
    
}
