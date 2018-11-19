package com.zhb.vue.params;

import java.util.List;

import com.zhb.vue.pojo.FunctionInfoData;
import com.zhb.vue.pojo.IconInfoData;

public class FunctionInfoParam {
    
    private String id;
    private String name;
    private Integer type;
    private String path;
    private Integer order;
    private IconInfoData iconInfoData;
    private FunctionInfoData parentFunctionInfo;
    private Integer deleteFlag;
    
    private List<FunctionInfoData> childFunctionInfos;
    private String iconId;
    private String parentId;
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public IconInfoData getIconInfoData() {
        return iconInfoData;
    }

    public void setIconInfoData(IconInfoData iconInfoData) {
        this.iconInfoData = iconInfoData;
    }

    public FunctionInfoData getParentFunctionInfo() {
        return parentFunctionInfo;
    }

    public void setParentFunctionInfo(FunctionInfoData parentFunctionInfo) {
        this.parentFunctionInfo = parentFunctionInfo;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public List<FunctionInfoData> getChildFunctionInfos() {
        return childFunctionInfos;
    }

    public void setChildFunctionInfos(List<FunctionInfoData> childFunctionInfos) {
        this.childFunctionInfos = childFunctionInfos;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
