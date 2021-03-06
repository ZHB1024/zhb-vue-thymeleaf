package com.zhb.vue.thread.spider.decoration;

import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;
import com.zhb.forever.framework.util.JsoupUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.KeyValueVO;
import com.zhb.forever.mq.disruptor.producer.Producer;

/** 
 * @ClassName: ProducerDecoration
 * @description: 
 * @author: 张会彬
 * @Date: 2019年8月4日 下午12:30:57
 */

public class ProducerDecoration extends Producer implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(ProducerDecoration.class);
    
    private final int endPage ;
    private final int beginPage ;
    private int currentPage = 0;
    private final String url;
    private AtomicInteger totalCount = new AtomicInteger(0);
    private StringBuilder sb = new StringBuilder();
    
    public ProducerDecoration(RingBuffer<KeyValueVO> ringBuffer, String name, int beginPage, int endPage, String url) {
        super(ringBuffer, name);
        this.beginPage = beginPage;
        this.endPage = endPage;
        this.url = url;
    }

    @Override
    public void run() {
        produce();
    }
    
    @Override
    public void produce() {
        logger.info("produceThread"+ name + "***********开始");
        this.currentPage = beginPage;
        StringBuilder targetUrl = new StringBuilder();
        while(currentPage <= endPage) {
            targetUrl.append(url).append("/p").append(currentPage);
            addChildPagePath(targetUrl.toString());
            currentPage++;
            targetUrl.setLength(0);
        }
        logger.info("produceThread"+ name + "***********结束");
    }
    
    private void addChildPagePath(String targetPath) {
        if (StringUtil.isNotBlank(targetPath)) {
            Document doc = JsoupUtil.getDocumentByUrl(targetPath);
            if (null == doc) {
                return;
            }
            
            Elements imgContents = doc.getElementsByClass("img-content");
            if (null == imgContents) {
                return;
            }
            int childIndex = 1;
            for (Element element : imgContents) {
                Elements aEle = element.getElementsByTag("a");
                if (null != aEle) {
                    addChildPageImagePath(aEle.get(1).attr("abs:href"),childIndex);
                }
                childIndex++;
            }
        }
    }
    
    private void addChildPageImagePath(String childPath,int childIndex) {
        /*if (StringUtil.isBlank(childPath)) {
            return;
        }
        
        Document doc = JsoupUtil.getDocumentByUrl(childPath);
        if (null == doc) {
            return;
        }
        Element urlsEle = doc.getElementById("preloadPics");
        if (null == urlsEle) {
            return;
        }
        String urls = JsoupUtil.getElementsByAttr(urlsEle, "value");
        if (StringUtil.isBlank(urls)) {
            return;
        }
        
        String[]  urlsArray = urls.split("\\|");
        if (null == urlsArray || urlsArray.length == 0) {
            return;
        }
        
        int i = 1;
        StringBuilder name = new StringBuilder();
        for (String url : urlsArray) {
            name.append(currentPage).append("_").append(childIndex).append("_").append(i).append(".jpg");
           //向ringBuffer中添加资源 
            pushData(name.toString(),url);
            i++;
            name.setLength(0);
        }
        */
         
    }
    
    private void pushData(String key,String value) {
        long sequence = ringBuffer.next();
        try {
            KeyValueVO vo = ringBuffer.get(sequence);
            vo.setKey(key);
            vo.setValue(value);
        }finally {
            ringBuffer.publish(sequence);
            sb.append("生产者-").append(name).append("：发布成功------第").append(currentPage);
            sb.append("页，第").append(totalCount.incrementAndGet()).append("个 , ").append(value);
            logger.info(sb.toString());
            sb.setLength(0);
        }
    }

}
