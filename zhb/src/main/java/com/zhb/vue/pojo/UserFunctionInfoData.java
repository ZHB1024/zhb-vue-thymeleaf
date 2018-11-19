package com.zhb.vue.pojo;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="user_function_info")
public class UserFunctionInfoData implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -300563950912009408L;
    
    private String id;
    private UserInfoData userInfoData;
    private FunctionInfoData functionInfoData;
    private Calendar createTime;
    
    public UserFunctionInfoData() {
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
    
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    public UserInfoData getUserInfoData() {
        return userInfoData;
    }
    public void setUserInfoData(UserInfoData userInfoData) {
        this.userInfoData = userInfoData;
    }
    
    @JoinColumn(name = "function_id")
    @ManyToOne(fetch = FetchType.LAZY)
    public FunctionInfoData getFunctionInfoData() {
        return functionInfoData;
    }
    public void setFunctionInfoData(FunctionInfoData functionInfoData) {
        this.functionInfoData = functionInfoData;
    }
    
    @Column(name="create_time")
    public Calendar getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }
    
}
