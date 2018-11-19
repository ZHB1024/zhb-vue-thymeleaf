package com.zhb.vue.thread.spider;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.zhb.forever.framework.util.FileUtil;
import com.zhb.forever.framework.util.JsoupUtil;
import com.zhb.forever.framework.util.StringUtil;

public class ReadEndUrlToQueueRunnable implements Runnable {
    
    private Logger logger = LoggerFactory.getLogger(ReadEndUrlToQueueRunnable.class);
    
    private String url;
    private String urlTarget;
    private ArrayBlockingQueue<JSONObject> resources ;
    
    private AtomicInteger endPage ;
    private AtomicInteger beginPage ;
    private AtomicInteger totalCount;
    
    public ReadEndUrlToQueueRunnable(String url,String urlTarget,AtomicInteger beginPage,AtomicInteger endPage,AtomicInteger totalCount,ArrayBlockingQueue<JSONObject> resources) {
        this.url = url;
        this.urlTarget = urlTarget;
        this.resources = resources;
        this.endPage = endPage;
        this.beginPage = beginPage;
        this.totalCount = totalCount;
    }

    @Override
    public void run() {
        logger.info("--Read-EndUrlThread--------------------------开始-------");
        while(endPage.get() > beginPage.get()) {
            String targetUrl = url + urlTarget + endPage.get() + ".html";
            addChildPageImagePath(targetUrl);
            endPage.decrementAndGet();
        }
        
        logger.info("--Read-EndUrlThread--------------------------结束-------");

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
                    return;
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
                    return ;
                }
                for (Element element : hrefs) {
                    if (null == element) {
                        return ;
                    }
                    i++;
                    StringBuilder fileName = new StringBuilder(name);
                    fileName.append("-"+i);
                    fileName.append(".jpg");
                    JSONObject object = new JSONObject();
                    object.put("name", fileName);
                    object.put("url", element.attr("abs:file"));
                    while (!resources.offer(object)) {
                        logger.info("--队列已满------等待消费-------");
                        try {
                            Thread.currentThread().sleep(10);
                        } catch (InterruptedException e) {
                            logger.error("向队列里添加url异常.............");
                            e.printStackTrace();
                            continue;
                        }
                    }
                    logger.info("--向队列里添加成功----第 " + endPage.get() + " 页------第 " + totalCount.incrementAndGet() + " 个");
                }
            }
        }
    }

}
