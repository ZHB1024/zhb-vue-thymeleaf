package com.zhb.vue.thread.spider.qbl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.zhb.forever.framework.Constants;
import com.zhb.forever.framework.dic.AttachmentTypeEnum;
import com.zhb.forever.framework.util.DownloadUtil;
import com.zhb.forever.framework.util.PropertyUtil;
import com.zhb.forever.framework.util.UploadUtil;
import com.zhb.vue.pojo.AttachmentInfoData;
import com.zhb.vue.service.AttachmentInfoService;
import com.zhb.vue.service.ServiceFactory;

public class DownloadImageRunnable implements Runnable{
    
private Logger logger = LoggerFactory.getLogger(DownloadImageRunnable.class);
    
    private JSONObject url ;
    private String creatorId;
    private Integer count ;
    private AttachmentInfoService attachmentInfoService = ServiceFactory.getAttachmentInfoService();
    
    public DownloadImageRunnable(JSONObject url,String creatorId,Integer count) {
        this.url = url;
        this.creatorId = creatorId;
        this.count = count;
    }
    
    @Override
    public void run() {
        if (null ==url) {
            return;
        }
        String uploadPath = PropertyUtil.getUploadPath();
        String thumbnailPath = null;
        String fileName = url.getString("name");
        try {
            DownloadUtil.downLoadFromUrl(url.getString("url"), fileName, uploadPath);
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
        fileInfoData.setCreateUserId(creatorId);
        attachmentInfoService.saveOrUpdate(fileInfoData);
        logger.info("--------------------下载成功---第 " + count + " 个");
    }

}
