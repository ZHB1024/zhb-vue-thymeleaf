package com.zhb.vue.thread.spider.qbl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class DownloadQBLFromQueueRunnable implements Runnable {

private Logger logger = LoggerFactory.getLogger(DownloadQBLFromQueueRunnable.class);
    
    private ArrayBlockingQueue<JSONObject> resources1 ;
    private ArrayBlockingQueue<JSONObject> resources2 ;
    private String creatorId;
    private AtomicInteger count = new AtomicInteger(0);
    private AtomicInteger shutdowmFlag = new AtomicInteger(0);
    
    
    public DownloadQBLFromQueueRunnable(ArrayBlockingQueue<JSONObject> resources1,ArrayBlockingQueue<JSONObject> resources2,String creatorId) {
        this.resources1 = resources1;
        this.resources2 = resources2;
        this.creatorId = creatorId;
    }

    @Override
    public void run() {
        logger.info("--------------------读取下载线程----开始");
        ExecutorService es = 
                Executors.newCachedThreadPool();
        while(true) {
            JSONObject url = null;
            while(null == (url=resources1.poll()) && null == (url=resources2.poll())){
                int flag = shutdowmFlag.incrementAndGet();
                if (flag > 10000) {//等待后结束
                    logger.info("^^^^^^^^^^^^^^^^^^^^^^队列已空--总共 "+ count.get() + " 个--读取下载线程--结束-^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                    es.shutdown();
                    return;
                }
                logger.info("^^^^^^^^^^^^^^^^^^^^^^队列已空--等待添加---" + shutdowmFlag.get());
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    logger.error("从队列里取url异常");
                    e.printStackTrace();
                }
            }
            shutdowmFlag = new AtomicInteger(0);
            logger.info("--------------------从队列里取----第 " + count.incrementAndGet() + " 个");
            es.execute(new DownloadImageRunnable(url,creatorId,count.get()));
        }
    }

}
