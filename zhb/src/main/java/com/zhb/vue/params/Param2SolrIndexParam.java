package com.zhb.vue.params;

import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.search.solr.param.AttachmentInfoSolrIndexParam;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月26日下午2:30:46
*/

public class Param2SolrIndexParam {
    
    public static AttachmentInfoSolrIndexParam attachmentParam2SolrIndexParam(AttachmentInfoParam infoParam) {
        if (null == infoParam) {
            return null;
        }
        AttachmentInfoSolrIndexParam indexParam = new AttachmentInfoSolrIndexParam();
        if (StringUtil.isNotBlank(infoParam.getFileName())) {
            indexParam.setKeyWord(infoParam.getFileName());
        }
        
        if (null != infoParam.getType()) {
            indexParam.addParams("type", infoParam.getType().toString());
        }
        
        if (null != infoParam.getLikeDegree()) {
            indexParam.addParams("likeDegree", infoParam.getLikeDegree().toString());
        }
        
        if (StringUtil.isNotBlank(infoParam.getCreateUserId())) {
            indexParam.addParams("createUserId", infoParam.getCreateUserId());
        }
        
        indexParam.setPageSize(infoParam.getPageSize());
        indexParam.setCurrentPage(infoParam.getCurrentPage());
        return indexParam;
    }

}


