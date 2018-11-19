package com.zhb.vue.web.util;

import java.text.ParseException;

import com.zhb.forever.framework.util.DateTimeUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.vue.params.UserInfoParam;

public class CheckUtil {
    
    
    public static String userInfoParamCheck(UserInfoParam param) {
        if (null == param) {
            return "请输入信息";
        }
        if (StringUtil.isBlank(param.getId())) {
            return "请输入信息";
        }
        if (StringUtil.isBlank(param.getUserName())) {
            return "请输入用户名";
        }
        if (StringUtil.isBlank(param.getRealName())) {
            return "请输入姓名";
        }
        if (StringUtil.isBlank(param.getSex())) {
            return "请输入性别";
        }
        if (StringUtil.isBlank(param.getIdentityCard())) {
            return "请输入身份证号";
        }
        if (StringUtil.isBlank(param.getCountry())) {
            return "请输入国籍";
        }
        if (StringUtil.isBlank(param.getNation())) {
            return "请输入民族";
        }
        if (StringUtil.isBlank(param.getByyx())) {
            return "请输入毕业院校";
        }
        if (StringUtil.isBlank(param.getMobilePhone())) {
            return "请输入电话";
        }
        if (StringUtil.isBlank(param.getEmail())) {
            return "请输入邮箱";
        }
        if (StringUtil.isBlank(param.getBirthdayString())) {
            return "请输入出生日期";
        }else {
            try {
                param.setBirthday(DateTimeUtil.formatGMT(param.getBirthdayString(), "yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
