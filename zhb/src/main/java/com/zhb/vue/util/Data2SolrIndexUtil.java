package com.zhb.vue.util;

import java.util.ArrayList;
import java.util.List;

import com.zhb.forever.search.solr.vo.AttachmentInfoSolrData;
import com.zhb.vue.pojo.AttachmentInfoData;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月26日上午9:31:40
*/

public class Data2SolrIndexUtil {

    //attachmentInfodatas 2 solrindex
    public static List<AttachmentInfoSolrData> AttachmentInfoDatas2SolrIndex(List<AttachmentInfoData> infoDatas){
        List<AttachmentInfoSolrData> solrIndex = null;
        if (null != infoDatas) {
            solrIndex = new ArrayList<>();
            for (AttachmentInfoData infoData : infoDatas) {
                AttachmentInfoSolrData data = new AttachmentInfoSolrData(infoData.getId(),infoData.getFileName(),infoData.getType());
                data.setFilePath(infoData.getFilePath());
                data.setThumbnailPath(infoData.getThumbnailPath());
                data.setCreateUserId(infoData.getCreateUserId());
                data.setCreateTime(infoData.getCreateTime().getTime());
                data.setLikeDegree(infoData.getLikeDegree());
                solrIndex.add(data);
            }
        }
        return solrIndex;
    }
    
    //attachmentInfodata 2 solrindex
    public static List<AttachmentInfoSolrData> AttachmentInfoData2SolrIndex(AttachmentInfoData infoData){
        List<AttachmentInfoSolrData> solrIndex = null;
        if (null != infoData) {
            solrIndex = new ArrayList<>();
            AttachmentInfoSolrData data = new AttachmentInfoSolrData(infoData.getId(),infoData.getFileName(),infoData.getType());
            data.setFilePath(infoData.getFilePath());
            data.setThumbnailPath(infoData.getThumbnailPath());
            data.setCreateUserId(infoData.getCreateUserId());
            data.setCreateTime(infoData.getCreateTime().getTime());
            data.setLikeDegree(infoData.getLikeDegree());
            solrIndex.add(data);
        }
        return solrIndex;
    }

}


