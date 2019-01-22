package com.zhb.vue.thread.spider.mtsqom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhb.forever.framework.Constants;
import com.zhb.forever.framework.dic.AttachmentTypeEnum;
import com.zhb.forever.framework.util.DownloadUtil;
import com.zhb.forever.framework.util.PropertyUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.util.UploadUtil;
import com.zhb.forever.mq.activemq.ActiveMQClientFactory;
import com.zhb.forever.mq.activemq.client.ActiveMQClient;
import com.zhb.vue.pojo.AttachmentInfoData;
import com.zhb.vue.service.AttachmentInfoService;
import com.zhb.vue.service.ServiceFactory;

public class DownloadFromQueueRunnable implements Runnable {
    
    private Logger logger = LoggerFactory.getLogger(DownloadFromQueueRunnable.class);
    
    private String name;
    private String queueName;
    private String userId;
    private AtomicInteger totalCount = new AtomicInteger(0);
    private AtomicInteger shutdowmFlag = new AtomicInteger(0);
    
    private ActiveMQClient activeMqClient = ActiveMQClientFactory.getActiveClientBean();
    private AttachmentInfoService attachmentInfoService = ServiceFactory.getAttachmentInfoService();
    
    public DownloadFromQueueRunnable(String name,String queueName,String userId) {
        this.name = name;
        this.queueName = queueName;
        this.userId = userId;
    }

    @Override
    public void run() {
        logger.info("DownloadThread"+ name + "***********开始");
        while(true) {
            TextMessage textMessage = null;
            while(null == (textMessage= activeMqClient.receiveQueueMessage(queueName))){
                int flag = shutdowmFlag.incrementAndGet();
                if (flag > 10) {
                    logger.info("DownloadThread"+ name + "********结束");
                    return;
                }
                logger.info("队列等待添加***************" + shutdowmFlag.get());
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    logger.error("从队列里取url异常*******************");
                    e.printStackTrace();
                    continue;
                }
            }
            shutdowmFlag = new AtomicInteger(0);
            String target = null;
            try {
                target = textMessage.getText();
            } catch (JMSException e) {
                e.printStackTrace();
                continue;
            }
            if (StringUtil.isNotBlank(target)) {
                String[] arrays = target.split(",-");
                if (null != arrays && arrays.length==2) {
                    downloadImage(arrays[0],arrays[1]);
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
            logger.error("从网络地址下载图片失败.**************");
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
        fileInfoData.setThumbnailPath(filePath);
        fileInfoData.setFileSize(String.valueOf(fileSize));
        fileInfoData.setContentType("image/jpg");
        fileInfoData.setType(AttachmentTypeEnum.YELLOW.getIndex());
        fileInfoData.setCreateUserId(userId);
        attachmentInfoService.saveOrUpdate(fileInfoData);
        logger.info("线程-"+ name + "下载成功****************第 " + totalCount.incrementAndGet() + " 个");
    }

}
