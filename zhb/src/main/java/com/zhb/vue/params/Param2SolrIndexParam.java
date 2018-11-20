package com.zhb.vue.params;

import java.text.ParseException;
import java.util.Calendar;

import com.zhb.forever.framework.util.DateTimeUtil;
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
        
        if (StringUtil.isNotBlank(infoParam.getCreateDate()) && !infoParam.getCreateDate().equals(",")) {
            String[] dates = infoParam.getCreateDate().split(",");
            try {
                Calendar startTime = DateTimeUtil.formatGMT(dates[0], "yyyy-MM-dd");
                Calendar endTime = DateTimeUtil.formatGMT(dates[1], "yyyy-MM-dd");
                infoParam.setStartDate(startTime);
                infoParam.setEndDate(endTime);
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                sb.append(DateTimeUtil.getDateTime(startTime, "yyyy-MM-dd"));
                sb.append("T");
                sb.append(DateTimeUtil.getDateTime(startTime, "HH:mm:ss"));
                sb.append("Z");
                sb.append(" TO ");
                sb.append(DateTimeUtil.getDateTime(endTime, "yyyy-MM-dd"));
                sb.append("T");
                sb.append(DateTimeUtil.getDateTime(endTime, "HH:mm:ss"));
                sb.append("Z");
                sb.append("]");
                indexParam.addParams("createTime", sb.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
        }
        
        indexParam.setPageSize(infoParam.getPageSize());
        indexParam.setCurrentPage(infoParam.getCurrentPage());
        return indexParam;
    }

}


