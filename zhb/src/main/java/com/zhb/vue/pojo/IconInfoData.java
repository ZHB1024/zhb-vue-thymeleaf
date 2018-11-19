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

@Entity
@Table(name="icon_info")
public class IconInfoData implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 4140547790524129549L;
    
    private String id;
    private String name;
    private String value;
    private Integer deleteFlag;
    private Calendar createTime;
    private Calendar updateTime;
    private String createUserId;
    
    public IconInfoData() {
        this.deleteFlag = DeleteFlagEnum.UDEL.getIndex();
        this.createTime = Calendar.getInstance();
    }
    
    @Id
    @GeneratedValue(generator = "app_seq")
    @GenericGenerator(name = "app_seq", strategy = "com.zhb.vue.pojo.strategy.StringRandomGenerator")
    @Column(name = "ID")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name = "name",nullable = false, length = 20)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name = "value",nullable = false, length = 50)
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    
    @Column(name = "delete_flag",nullable = false)
    public Integer getDeleteFlag() {
        return deleteFlag;
    }
    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Column(name = "create_time")
    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_time")
    public Calendar getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Calendar updateTime) {
        this.updateTime = updateTime;
    }
    
    @Column(name = "CREATE_USER_ID")
    public String getCreateUserId() {
        return createUserId;
    }
    
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
}
