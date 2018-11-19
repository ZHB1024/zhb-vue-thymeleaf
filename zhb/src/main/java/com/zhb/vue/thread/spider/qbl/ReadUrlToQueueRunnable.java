package com.zhb.vue.thread.spider.qbl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.zhb.forever.framework.Constants;
import com.zhb.forever.framework.dic.AttachmentTypeEnum;
import com.zhb.forever.framework.util.DownloadUtil;
import com.zhb.forever.framework.util.FileUtil;
import com.zhb.forever.framework.util.JsoupUtil;
import com.zhb.forever.framework.util.PropertyUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.util.UploadUtil;
import com.zhb.vue.pojo.AttachmentInfoData;
import com.zhb.vue.service.AttachmentInfoService;
import com.zhb.vue.service.ServiceFactory;

public class ReadUrlToQueueRunnable implements Runnable {

    private Logger logger = LoggerFactory.getLogger(ReadUrlToQueueRunnable.class);
    
    private AttachmentInfoService attachmentInfoService = ServiceFactory.getAttachmentInfoService();
    private String name;
    private String url;
    private String userId;
    
    private int endPage ;
    private int beginPage ;
    public static AtomicInteger totalCount = new AtomicInteger(0);
    
    public ReadUrlToQueueRunnable(String name,String url,int beginPage,int endPage,String userId) {
        this.name = name;
        this.url = url;
        this.endPage = endPage;
        this.beginPage = beginPage;
        this.userId = userId;
    }

    @Override
    public void run() {
        logger.info("******爬取网页链接线程" + name + "---开始");
        String targetUrl = "";
        while(endPage >= beginPage) {
            if (beginPage == 1) {
                targetUrl = url + ".html";
            }else {
                targetUrl = url + "_" + beginPage + ".html";
            }
            addChildPageImagePath(targetUrl);
            beginPage++;
        }
        
        logger.info("******爬取网页链接线程" + name + "---结束******************************************");

    }
    
    private void addChildPageImagePath(String targetPath) {
        if (StringUtil.isNotBlank(targetPath)) {
            Document doc = JsoupUtil.getDocumentByUrl(targetPath);
            if (null == doc) {
                return;
            }
            Elements divs = doc.getElementsByClass("art");
            if (null != divs) {
                Elements hrefs = divs.get(0).select("a[href]");
                if (null != hrefs) {
                    for (Element a : hrefs) {
                        addImagePath(a.attr("abs:href"));
                    }
                }
            }
        }
    }
    
    private void addImagePath(String targetPath) {
        if (StringUtil.isNotBlank(targetPath)) {
            Document doc = JsoupUtil.getDocumentByUrl(targetPath);
            if (null == doc) {
                return;
            }
            Elements artbody = doc.getElementsByClass("artbody");
            if (null == artbody) {
                return;
            }
            
            String name = FileUtil.randomName();
            int i = 0;
            for (Element link : artbody) {
                Elements hrefs = link.select("img[src]");
                if (null == hrefs) {
                    continue ;
                }
                for (Element element : hrefs) {
                    if (null == element) {
                        continue;
                    }
                    i++;
                    StringBuilder fileName = new StringBuilder(name);
                    fileName.append("_"+i);
                    fileName.append(".jpg");
                    downloadImage(fileName.toString(), element.attr("abs:src"));
                    /*while (!resources.offer(object)) {
                        logger.info("******队列已满--等待消费");
                        try {
                            Thread.currentThread().sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            logger.error("向队列里添加url异常");
                        }
                    }*/
                }
            }
        }
    }
    
    private void downloadImage(String fileName,String imagUrl) {
        String uploadPath = PropertyUtil.getUploadPath();
        String thumbnailPath = null;
        try {
            DownloadUtil.downLoadFromUrl(imagUrl, fileName, uploadPath);
        } catch (IOException e) {
            logger.error("从网络地址下载图片失败.................");
            e.printStackTrace();
            return;
        }
        
        String filePath = uploadPath + File.separator + fileName;
        File file = new File(filePath);
        Long fileSize = file.length();
        if (fileSize > Constants.SMALL_IMAGE_SIZE) {
            try {
                thumbnailPath = UploadUtil.uploadThumbmail(new FileInputStream(file), fileName, "jpg", fileSize);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                thumbnailPath = filePath;
            }
        }
        
        AttachmentInfoData fileInfoData = new AttachmentInfoData();
        fileInfoData.setFileName(fileName);
        fileInfoData.setFilePath(filePath);
        fileInfoData.setThumbnailPath(thumbnailPath);
        fileInfoData.setFileSize(String.valueOf(fileSize));
        fileInfoData.setContentType("image/jpeg");
        fileInfoData.setType(AttachmentTypeEnum.YELLOW.getIndex());
        fileInfoData.setCreateUserId(userId);
        attachmentInfoService.saveOrUpdate(fileInfoData);
        logger.info("------线程"+ name + "--------------下载成功---第 " + beginPage + " 页------第 " + totalCount.incrementAndGet() + " 个");
    }

}
