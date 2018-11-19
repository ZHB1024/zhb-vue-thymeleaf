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
@Table(name="user_info")
public class UserInfoData implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -8723796548579948339L;
    
    private String id;
    private String userName;
    private String realName;
    private String password;
    private String salt;
    private String sex;
    private Calendar birthday;
    private String identityCard;
    private String country;
    private String nation;
    private String byyx;//毕业院校
    private String mobilePhone;
    private String email;
    private String lobId;
    private Calendar createTime;
    private Calendar updateTime;
    private Integer deleteFlag;
    
    public UserInfoData() {
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
    
    @Column(name = "user_name",nullable = false,length=15)
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    @Column(name = "real_name",length=20)
    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    @Column(name = "password",nullable = true,length=100)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Column(name = "salt",nullable = true,length=8)
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    @Column(name = "sex",length=2)
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    @Column(name = "birthday")
    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    @Column(name = "identity_card",length=18)
    public String getIdentityCard() {
        return identityCard;
    }
    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }
    
    @Column(name = "country",length=50)
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    
    @Column(name = "nation",length=50)
    public String getNation() {
        return nation;
    }
    public void setNation(String nation) {
        this.nation = nation;
    }
    
    @Column(name = "byyx",length=50)
    public String getByyx() {
        return byyx;
    }
    public void setByyx(String byyx) {
        this.byyx = byyx;
    }
    
    @Column(name = "mobile_phone",length=11)
    public String getMobilePhone() {
        return mobilePhone;
    }
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
    
    @Column(name = "email",length=30)
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Column(name = "lob_id",length=16)
    public String getLobId() {
        return lobId;
    }
    public void setLobId(String lobId) {
        this.lobId = lobId;
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
    
    @Column(name = "delete_flag",nullable = false)
    public Integer getDeleteFlag() {
        return deleteFlag;
    }
    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

}
