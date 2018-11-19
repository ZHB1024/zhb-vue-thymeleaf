package com.zhb.vue.web.util;

import com.zhb.vue.params.FunctionInfoParam;
import com.zhb.vue.params.UserFunctionInfoParam;
import com.zhb.vue.params.UserInfoParam;
import com.zhb.vue.pojo.FunctionInfoData;
import com.zhb.vue.pojo.UserFunctionInfoData;
import com.zhb.vue.pojo.UserInfoData;

public class Param2DataUtil {
    
    
    public static void userInfoParam2Data(UserInfoParam param,UserInfoData data) {
        if (null == param || null == data) {
            return ;
        }
        data.setUserName(param.getUserName());
        data.setRealName(param.getRealName());
        data.setSex(param.getSex());
        data.setBirthday(param.getBirthday());
        data.setIdentityCard(param.getIdentityCard());
        data.setCountry(param.getCountry());
        data.setNation(param.getNation());
        data.setByyx(param.getByyx());
        data.setMobilePhone(param.getMobilePhone());
        data.setEmail(param.getEmail());
    }
    
    public static void functionParam2Data(FunctionInfoParam param,FunctionInfoData data) {
        if (null == param || null == data) {
            return ;
        }
        data.setId(param.getId());
        data.setName(param.getName());
        data.setPath(param.getPath());
        data.setType(param.getType());
        data.setIconInfoData(param.getIconInfoData());
        data.setParentFunctionInfo(param.getParentFunctionInfo());
        data.setDeleteFlag(param.getDeleteFlag());
        data.setOrder(param.getOrder());
    }
    
    public static void userFunctionParam2Data(UserFunctionInfoParam param,UserFunctionInfoData data) {
        if (null == param || null == data) {
            return ;
        }
        data.setId(param.getId());
        data.setUserInfoData(param.getUserInfoData());
        data.setFunctionInfoData(param.getFunctionInfoData());
    }

}
