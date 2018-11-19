package com.zhb.vue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.page.PageUtil;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.dao.AttachmentInfoDao;
import com.zhb.vue.params.AttachmentInfoParam;
import com.zhb.vue.pojo.AttachmentInfoData;
import com.zhb.vue.service.AttachmentInfoService;
import com.zhb.vue.service.base.impl.CommonServiceImpl;

@Service
public class AttachmentInfoServiceImpl extends CommonServiceImpl implements AttachmentInfoService{
    
    @Autowired
    private AttachmentInfoDao attachmentInfoDao;

    @Override
    @Transactional
    public void saveOrUpdate(AttachmentInfoData data) {
        attachmentInfoDao.saveOrUpdate(data);
    }

    @Override
    public List<AttachmentInfoData> getAttachmentInfos(AttachmentInfoParam param, List<OrderVO> orderVos) {
        return attachmentInfoDao.getAttachmentInfos(param, orderVos);
    }

    @Override
    public AttachmentInfoData getAttachmentInfoById(String id) {
        return attachmentInfoDao.getAttachmentInfoById(id);
    }

    @Override
    public List<AttachmentInfoData> getAttachmentInfoByFileName(String fileName) {
        return attachmentInfoDao.getAttachmentInfoByFileName(fileName);
    }

    @Override
    public void deleteAttachmentInfo(AttachmentInfoData data) {
        attachmentInfoDao.deleteAttachmentInfo(data);
    }

    @Override
    public Page<AttachmentInfoData> getAttachmentInfosPage(AttachmentInfoParam param, List<OrderVO> orderVos) {
        long total = attachmentInfoDao.getAttachmentInfosPageCount(param);
        if (total > 0 ) {
            List<AttachmentInfoData> attachmentInfoDatas = attachmentInfoDao.getAttachmentInfosPage(param,orderVos);
            //上一页可能有数据
            if ((null == attachmentInfoDatas || attachmentInfoDatas.size() == 0) && param.getCurrentPage() > 1) {
                param.setStart(param.getPageSize() * (param.getCurrentPage()-2));
                attachmentInfoDatas = attachmentInfoDao.getAttachmentInfosPage(param,orderVos);
            }
            Page<AttachmentInfoData> page = PageUtil.getPage(attachmentInfoDatas.iterator(), param.getStart(), param.getPageSize(), total);
            return page;
        }
        return null;
    }

    @Override
    public List<Object[]> statisticAttachment() {
        return attachmentInfoDao.statisticAttachment();
    }
    
}
