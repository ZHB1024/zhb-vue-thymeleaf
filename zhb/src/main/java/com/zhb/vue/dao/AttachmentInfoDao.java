package com.zhb.vue.dao;

import java.util.List;

import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.vue.params.AttachmentInfoParam;
import com.zhb.vue.pojo.AttachmentInfoData;

public interface AttachmentInfoDao {
    
    /**
     * *新增或修改 附件
     * @param data
     */
    void saveOrUpdate(AttachmentInfoData data);
    
    /**
     * *获取 附件
     * @param param
     * 
     * @return
     */
    List<AttachmentInfoData> getAttachmentInfos(AttachmentInfoParam param,List<OrderVO> orderVos);
    
    /**
     * *获取 附件,根据id
     * 
     * @param id
     * 
     * @return
     */
    AttachmentInfoData getAttachmentInfoById(String id);
    
    /**
     * *获取 附件,根据fileName
     * 
     * @param fileName
     * 
     * @return
     */
    List<AttachmentInfoData> getAttachmentInfoByFileName(String fileName);

    /**
     * *删除附件
     * 
     * @param AttachmentInfoData
     */
    void deleteAttachmentInfo(AttachmentInfoData data);
    
    /**
     * *获取 附件数量
     * 
     * @param param
     * 
     * @return
     */
    Long getAttachmentInfosPageCount(AttachmentInfoParam param);

    /**
     * *获取 附件 ,分页
     * 
     * @param param
     * @param orderVos
     * 
     * @return
     */
    List<AttachmentInfoData> getAttachmentInfosPage(AttachmentInfoParam param,List<OrderVO> orderVos);
    
    /**
     * *统计
     */
    List<Object[]> statisticAttachment();
    
}
