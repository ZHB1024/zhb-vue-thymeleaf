package com.zhb.vue.params;

import com.zhb.vue.pojo.FunctionInfoData;
import com.zhb.vue.pojo.UserInfoData;

public class UserFunctionInfoParam {

    private String id;
    private UserInfoData userInfoData;
    private FunctionInfoData functionInfoData;
    
    private String userId;
    private String functionId;
    
    private Integer start;
    private Integer pageSize;
    private Integer currentPage;
    
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public UserInfoData getUserInfoData() {
        return userInfoData;
    }
    public void setUserInfoData(UserInfoData userInfoData) {
        this.userInfoData = userInfoData;
    }
    public FunctionInfoData getFunctionInfoData() {
        return functionInfoData;
    }
    public void setFunctionInfoData(FunctionInfoData functionInfoData) {
        this.functionInfoData = functionInfoData;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getFunctionId() {
        return functionId;
    }
    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }
    public Integer getStart() {
        return start;
    }
    public void setStart(Integer start) {
        this.start = start;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public Integer getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    
}
