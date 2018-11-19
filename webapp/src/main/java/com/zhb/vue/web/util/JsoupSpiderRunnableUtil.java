package com.zhb.vue.web.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhb.forever.framework.util.DownloadUtil;
import com.zhb.forever.framework.util.JsoupUtil;
import com.zhb.forever.framework.util.StringUtil;

public class JsoupSpiderRunnableUtil implements Runnable{
    
    private Logger logger = LoggerFactory.getLogger(JsoupSpiderRunnableUtil.class);

    private String url;
    private String targetSavePath;
    private int begin;
    private int end;
    private String name;
    private AtomicInteger count = new AtomicInteger(0);

    public JsoupSpiderRunnableUtil(String url,String targetSavePath,int nameIndex,int begin,int end) {
        this.url = url;
        this.targetSavePath = targetSavePath;
        this.begin = begin;
        this.end = end;
        this.name = "thread" + nameIndex + "-";
    }

    @Override
    public void run() {
        logger.info(name + " 开始执行---------------------------");
        Long start = System.currentTimeMillis();
        for(;begin <= end;begin++) {
            String page = "index_" + begin;
            if (begin == 1) {
                page = "index";
            }
            String targetUrl = url + page + ".html";
            List<String> imagePaths = getImagePath(targetUrl);
            if (null != imagePaths) {
                for (String urlPath : imagePaths) {
                    try {
                        int increment = count.incrementAndGet();
                        String fileName = begin + "page_" + increment + ".jpg";
                        DownloadUtil.downLoadFromUrl(urlPath, fileName, targetSavePath);
                        logger.info(name + fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.info(name + " 异常");
                    }
                }
            }
        }
        Long end = System.currentTimeMillis();
        Long time = (end-start)/1000;
        logger.info(name + "执行结束---------------------------");
        logger.info("耗时：" + time + " 秒");
    }

    private List<String> getImagePath(String targetPath) {
        List<String> images = null;
        if (StringUtil.isNotBlank(targetPath)) {
            images = new ArrayList<String>();
            Document doc = JsoupUtil.getDocumentByUrl(targetPath);
            if (null == doc) {
                return null;
            }
            Element main = doc.getElementById("main");
            Elements liEle = main.getElementsByTag("li");
            for (Element link : liEle) {
                Elements hrefs = link.select("a[href]");
                for (Element element : hrefs) {
                    String picutres = element.attr("abs:href");
                    Document picDom = JsoupUtil.getDocumentByUrl(picutres);
                    if (null != picDom) {
                        Elements alinks = picDom.getElementsByClass("artbody imgbody");
                        Elements picSrc = alinks.select("[src]");
                        for (Element ele : picSrc) {
                            String imageUrl = ele.attr("abs:src");
                            images.add(imageUrl);
                        }
                    }
                }
            }
        }
        return images;
    }


}
