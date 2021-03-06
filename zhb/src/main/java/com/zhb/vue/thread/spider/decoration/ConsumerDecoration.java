package com.zhb.vue.thread.spider.decoration;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhb.forever.framework.util.DownloadUtil;
import com.zhb.forever.mq.disruptor.consumer.Consumer;
import com.zhb.vue.thread.spider.SpiderProperty;

/** 
 * @ClassName: ConsumerDecoration
 * @description: 
 * @author: 张会彬
 * @Date: 2019年8月4日 下午12:28:22
 */

public class ConsumerDecoration extends Consumer {
    
    private final Logger logger = LoggerFactory.getLogger(ConsumerDecoration.class);

    private AtomicInteger totalCount ;

    public ConsumerDecoration(String name,AtomicInteger totalCount) {
        super(name);
        this.totalCount = totalCount;
    }

    public void consume(String key, String value) {
        downloadImage(key, value);
    }
    
    private void downloadImage(String fileName,String imagUrl) {
        String downloadPath = SpiderProperty.getSpiderDownloadPath();
        try {
            DownloadUtil.downLoadFromUrl(imagUrl, fileName, downloadPath);
        } catch (IOException e) {
            logger.error("从网络地址下载图片失败.**************");
            e.printStackTrace();
            return;
        }
        
        //logger.info("消费者-"+ name + " 下载成功****************第 " + totalCount.incrementAndGet() + " 个");
    }

}
