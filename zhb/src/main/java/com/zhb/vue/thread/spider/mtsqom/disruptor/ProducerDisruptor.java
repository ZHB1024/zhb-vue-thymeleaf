package com.zhb.vue.thread.spider.mtsqom.disruptor;

import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;
import com.zhb.forever.framework.util.FileUtil;
import com.zhb.forever.framework.util.JsoupUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.KeyValueVO;
import com.zhb.forever.mq.disruptor.producer.Producer;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年12月18日下午5:44:24
*/

public class ProducerDisruptor extends Producer {
    
    private final Logger logger = LoggerFactory.getLogger(ProducerDisruptor.class);
    
    private final int endPage ;
    private final int beginPage ;
    private int currentPage = 0;
    private final String url;
    private AtomicInteger totalCount = new AtomicInteger(0);

    public ProducerDisruptor(RingBuffer<KeyValueVO> ringBuffer, String name, int beginPage, int endPage, String url) {
        super(ringBuffer, name);
        this.beginPage = beginPage;
        this.endPage = endPage;
        this.url = url;
    }
    
    public void produce() {
        this.currentPage = beginPage;
        String targetUrl = "";
        while(currentPage <= endPage) {
            targetUrl = url + currentPage + ".html";
            //targetUrl = url + currentPage;
            addChildPageImagePath(targetUrl);
            currentPage++;
        }
    }
    
    private void addChildPageImagePath(String targetPath) {
        if (StringUtil.isNotBlank(targetPath)) {
            Document doc = JsoupUtil.getDocumentByUrl(targetPath);
            if (null == doc) {
                return;
            }
            Element main = doc.getElementById("moderate");
            if (null == main) {
                return;
            }
            Elements liEle = main.getElementsByTag("li");
            if (null == liEle) {
                return;
            }
            for (Element link : liEle) {
                Elements links = link.getElementsByClass("z");
                if (null == links) {
                    continue;
                }
                addImagePath(links.get(0).attr("abs:href"));
            }
        }
    }
    
    private void addImagePath(String targetPath) {
        if (StringUtil.isNotBlank(targetPath)) {
            Document doc = JsoupUtil.getDocumentByUrl(targetPath);
            if (null == doc) {
                return;
            }
            Elements main = doc.getElementsByClass("t_f");
            if (null == main) {
                return;
            }
            
            String name = FileUtil.randomName();
            int i = 0;
            for (Element link : main) {
                Elements hrefs = link.select("img[src]");
                if (null == hrefs) {
                    continue;
                }
                for (Element element : hrefs) {
                    if (null == element) {
                        continue;
                    }
                    i++;
                    StringBuilder target = new StringBuilder(name);
                    target.append("-"+i);
                    target.append(".jpg");
                    
                    //向ringBuffer中添加资源
                    pushData(target.toString(),element.attr("abs:file"));
                }
            }
        }
    }
    
    private void pushData(String key,String value) {
        long sequence = ringBuffer.next();
        try {
            KeyValueVO vo = ringBuffer.get(sequence);
            vo.setKey(key);
            vo.setValue(value);
        }finally {
            ringBuffer.publish(sequence);
            logger.info("生产者-" + name + "：发布成功------第 " + currentPage + " 页，第 " + totalCount.incrementAndGet() + " 个" );
        }
    }

}


