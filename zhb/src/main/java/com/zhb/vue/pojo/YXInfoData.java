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

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月31日上午11:42:04
*/

@Entity
@Table(name="yx_info")
public class YXInfoData implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -1623149768430404368L;
    
    private String id;
    private String name;
    private String city;//所在省市
    private String belong;//隶属
    private String type;//院校类型
    private String xlcc;//学历层次
    private Integer is985;
    private Integer is211;
    private Integer isYJSY;//是否有研究生院
    private String point;//满分5
    private Integer deleteFlag;
    private Calendar createTime;
    private String createUserId;

    public YXInfoData() {
        this.deleteFlag = DeleteFlagEnum.UDEL.getIndex();
        this.createTime = Calendar.getInstance();
        this.is985 = 0;
        this.is211 = 0;
        this.isYJSY = 0;
        this.point="--";
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

    @Column(name = "name",length=50,nullable=false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "city",length=50,nullable=false)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "belong",length=50,nullable=false)
    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    @Column(name = "type",length=20,nullable=false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "xlcc",length=20,nullable=false)
    public String getXlcc() {
        return xlcc;
    }

    public void setXlcc(String xlcc) {
        this.xlcc = xlcc;
    }

    @Column(name = "is985",nullable=false,columnDefinition="int default 0")
    public Integer getIs985() {
        return is985;
    }

    public void setIs985(Integer is985) {
        this.is985 = is985;
    }

    @Column(name = "is211",nullable=false,columnDefinition="int default 0")
    public Integer getIs211() {
        return is211;
    }

    public void setIs211(Integer is211) {
        this.is211 = is211;
    }

    @Column(name = "isYJSY",nullable=false,columnDefinition="int default 0")
    public Integer getIsYJSY() {
        return isYJSY;
    }

    public void setIsYJSY(Integer isYJSY) {
        this.isYJSY = isYJSY;
    }

    @Column(name = "point",nullable=false,length=4)
    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    @Column(name = "delete_flag",nullable=false,columnDefinition="int default 0")
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

}


