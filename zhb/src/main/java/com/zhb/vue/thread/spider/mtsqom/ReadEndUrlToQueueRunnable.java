package com.zhb.vue.thread.spider.mtsqom;

import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhb.forever.framework.util.FileUtil;
import com.zhb.forever.framework.util.JsoupUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.mq.activemq.ActiveMQClientFactory;
import com.zhb.forever.mq.activemq.client.ActiveMQClient;

public class ReadEndUrlToQueueRunnable implements Runnable {

    private Logger logger = LoggerFactory.getLogger(ReadEndUrlToQueueRunnable.class);
    
    private String name;
    private String queueName;
    private int endPage ;
    private int beginPage ;
    private String url;
    private AtomicInteger totalCount = new AtomicInteger(0);
    
    private ActiveMQClient activeMqClient = ActiveMQClientFactory.getActiveClientBean();
    
    public ReadEndUrlToQueueRunnable(String name,String queueName,String url,int beginPage,int endPage) {
        this.name = name;
        this.queueName = queueName;
        this.url = url;
        this.endPage = endPage;
        this.beginPage = beginPage;
    }
    
    @Override
    public void run() {
        logger.info("ReadUrlThread-" + name + "--------------------------开始-------");
        String targetUrl = "";
        while(endPage >= beginPage) {
            //targetUrl = url + beginPage + ".html";
            targetUrl = url + beginPage;
            addChildPageImagePath(targetUrl);
            beginPage++;
        }
        logger.info("ReadUrlThread-" + name + "--------------------------结束-------");
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
                    StringBuilder target = new StringBuilder(name);
                    target.append("-"+i);
                    target.append(".jpg");
                    target.append(",-" + element.attr("abs:file"));
                    activeMqClient.sendQueueDestinationNameMsg(queueName, target.toString());
                    logger.info("向队列里添加成功--------------------------------------第 " + beginPage + " 页");
                }
            }
        }
    }

}
