package com.zhb.vue.thread.spider.mtsqom.disruptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.WorkHandler;
import com.zhb.forever.framework.Constants;
import com.zhb.forever.framework.dic.AttachmentTypeEnum;
import com.zhb.forever.framework.util.DownloadUtil;
import com.zhb.forever.framework.util.PropertyUtil;
import com.zhb.forever.framework.util.UploadUtil;
import com.zhb.forever.framework.vo.KeyValueVO;
import com.zhb.vue.pojo.AttachmentInfoData;
import com.zhb.vue.service.AttachmentInfoService;
import com.zhb.vue.service.ServiceFactory;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年12月18日下午6:02:59
*/

public class ConsumerDisruptor implements WorkHandler<KeyValueVO>{

    private final Logger logger = LoggerFactory.getLogger(ConsumerDisruptor.class);

    private AttachmentInfoService attachmentInfoService = ServiceFactory.getAttachmentInfoService();
    
    private final String name;
    private final String userId;
    private AtomicInteger totalCount = new AtomicInteger(0);
    
    public ConsumerDisruptor(String name,String userId) {
        this.name = name;
        this.userId = userId;
    }
    
    @Override
    public void onEvent(KeyValueVO event) throws Exception {
        downloadImage(event.getKey(), event.getValue());
    }
    
    
    private void downloadImage(String fileName,String imagUrl) {
        String uploadPath = PropertyUtil.getUploadPath();
        String thumbnailPath = "";
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
        fileInfoData.setThumbnailPath(thumbnailPath);
        fileInfoData.setFileSize(String.valueOf(fileSize));
        fileInfoData.setContentType("image/jpg");
        fileInfoData.setType(AttachmentTypeEnum.YELLOW.getIndex());
        fileInfoData.setCreateUserId(userId);
        attachmentInfoService.saveOrUpdate(fileInfoData);
        logger.info("消费者-"+ name + " 下载成功****************第 " + totalCount.incrementAndGet() + " 个");
    }

}


