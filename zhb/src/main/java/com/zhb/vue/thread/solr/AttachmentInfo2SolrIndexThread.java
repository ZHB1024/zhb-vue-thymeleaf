package com.zhb.vue.thread.solr;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zhb.forever.search.solr.vo.AttachmentInfoSolrData;
import com.zhb.vue.pojo.AttachmentInfoData;
import com.zhb.vue.thread.solr.task.DeleteAttachmentSolrIndexRunnable;
import com.zhb.vue.thread.solr.task.UpdateAttachmentSolrIndexRunnable;
import com.zhb.vue.util.Data2SolrIndexUtil;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月26日上午10:50:04
*/

public class AttachmentInfo2SolrIndexThread {

    public static boolean createAttachmentSolrIndex(AttachmentInfoData fileInfoData) {
        boolean flag = true;
        try {
            List<AttachmentInfoSolrData> datas = Data2SolrIndexUtil.AttachmentInfoData2SolrIndex(fileInfoData);
            if (null != datas) {
                ExecutorService es = Executors.newFixedThreadPool(1);
                es.execute(new UpdateAttachmentSolrIndexRunnable("addAttachmentSolrIndex-thread", datas));
                es.shutdown();
            }
        }catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
    
    public static boolean createAttachmentSolrIndexs(List<AttachmentInfoData> fileInfoDatas) {
        boolean flag = true;
        try {
            List<AttachmentInfoSolrData> datas = Data2SolrIndexUtil.AttachmentInfoDatas2SolrIndex(fileInfoDatas);
            if (null != datas) {
                ExecutorService es = Executors.newFixedThreadPool(1);
                es.execute(new UpdateAttachmentSolrIndexRunnable("addAttachmentSolrIndex-thread", datas));
                es.shutdown();
            }
        }catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
    
    public static boolean deleteAttachmentSolrIndex(List<String> ids) {
        boolean flag = true;
        try {
            if (null != ids && ids.size() > 0) {
                ExecutorService es = Executors.newFixedThreadPool(1);
                es.execute(new DeleteAttachmentSolrIndexRunnable("deleteAttachmentSolrIndex-thread", ids,false));
                es.shutdown();
            }
        }catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
    
    public static boolean deleteAllAttachmentSolrIndex() {
        boolean flag = true;
        try {
            ExecutorService es = Executors.newFixedThreadPool(1);
            es.execute(new DeleteAttachmentSolrIndexRunnable("deleteAllAttachmentSolrIndex-thread", null,true));
            es.shutdown();
        }catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

}


