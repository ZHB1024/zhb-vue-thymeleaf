package com.zhb.vue.thread.spider;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.zhb.forever.framework.util.StringUtil;

public class DownloadFromQueueRunnable implements Runnable {
    
    private Logger logger = LoggerFactory.getLogger(DownloadFromQueueRunnable.class);
    
    private ArrayBlockingQueue<JSONObject> resources ;
    private String creatorId;
    private AtomicInteger count = new AtomicInteger(0);
    private AtomicInteger shutdowmFlag = new AtomicInteger(0);
    
    
    public DownloadFromQueueRunnable(ArrayBlockingQueue<JSONObject> resources,String creatorId) {
        this.resources = resources;
        this.creatorId = creatorId;
    }

    @Override
    public void run() {
        logger.info("--DownloadThread------开始----------");
        while(true) {
            JSONObject url = null;
            while(null == (url=resources.poll())){
                int flag = shutdowmFlag.incrementAndGet();
                if (flag > 5000) {
                    logger.info("--队列已空-------下载线程-----------------------------------------------结束---");
                    return;
                }
                logger.info("--队列已空----等待添加----" + shutdowmFlag.get()+"---");
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    logger.error("从队列里取url异常...............");
                    e.printStackTrace();
                    continue;
                }
            }
            shutdowmFlag = new AtomicInteger(0);
            ExecutorService es = Executors.newFixedThreadPool(1);
            es.execute(new DownloadImageRunnable(url,creatorId,count.incrementAndGet()));
            es.shutdown();
        }
    }

}
