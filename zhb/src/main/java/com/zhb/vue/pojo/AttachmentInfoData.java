package com.zhb.vue.pojo;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.zhb.forever.framework.dic.DeleteFlagEnum;
import com.zhb.forever.framework.dic.LikeDgreeEnum;

@Entity
@Table(name="attachment_info")
public class AttachmentInfoData implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -1623149768430404368L;
    
    private String id;
    private String fileName;
    private String fileSize;
    private String filePath;
    private String thumbnailPath;
    private String contentType;
    private Integer type;
    private Integer deleteFlag;
    private Integer likeDegree;
    private Calendar createTime;
    private String createUserId;

    public AttachmentInfoData() {
        this.deleteFlag = DeleteFlagEnum.UDEL.getIndex();
        this.createTime = Calendar.getInstance();
        this.likeDegree = LikeDgreeEnum.UN_LIKE.getIndex();
    }

    @Id
    @GeneratedValue(generator = "app_seq")
    @GenericGenerator(name = "app_seq", strategy = "com.zhb.vue.pojo.strategy.StringRandomGenerator")
    @Column(name = "ID",length=16,nullable=false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "file_name",length=16,nullable=false)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "file_size",length=10,nullable=false)
    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    @Column(name = "file_path",length=100,nullable=false)
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Column(name = "thumbnail_path",length=100,nullable=true)
    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    @Column(name = "content_type",length=200,nullable=false)
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Column(name = "type",nullable=false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "delete_flag",nullable=false)
    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Column(name = "create_time",nullable=false)
    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    @Column(name = "CREATE_USER_ID",length=16,nullable=false)
    public String getCreateUserId() {
        return createUserId;
    }
    
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Column(name = "LIKE_DEGREE",nullable=false,columnDefinition="int default 1")
    public Integer getLikeDegree() {
        return likeDegree;
    }

    public void setLikeDegree(Integer likeDegree) {
        this.likeDegree = likeDegree;
    }

}
