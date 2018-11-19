package com.zhb.vue.thread.solr.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhb.forever.search.SearchFactory;
import com.zhb.forever.search.solr.client.SolrClient;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月26日上午10:52:19
*/

public class DeleteAttachmentSolrIndexRunnable implements Runnable {
    
    private Logger logger = LoggerFactory.getLogger(DeleteAttachmentSolrIndexRunnable.class);
    
    private String name;
    private boolean deleteAll = false;
    private List<String> ids;
    
    private SolrClient solrClient = null;


    public DeleteAttachmentSolrIndexRunnable(String name,List<String> ids,boolean deleteAll) {
        this.name = name;
        this.solrClient = SearchFactory.getSolrClientBean();
        this.ids = ids;
        this.deleteAll = deleteAll;
    }

    @Override
    public void run() {
        if (deleteAll) {
            try {
                solrClient.deleteAllAttachments();
                logger.info("删除附件索引线程" + name + " 删除了所有附件索引");
            } catch (Exception e) {
                logger.error("删除所有附件索引线程" + name + "异常.........");
                e.printStackTrace();
            }
        }else {
            if (null != ids && ids.size() > 0) {
                try {
                    solrClient.deleteAttachmentsByIds(ids);
                    logger.info("删除附件索引线程" + name + " 删除了" + ids.size() + "个索引");
                } catch (Exception e) {
                    logger.error("删除附件索引线程" + name + "异常.........");
                    e.printStackTrace();
                }
            }
        }
        
    }

}


