package com.zhb.vue.util;

import com.zhb.forever.framework.vo.UserInfoVO;
import com.zhb.vue.pojo.UserInfoData;

public class Data2VO {
    
    
    public static UserInfoVO userInfoDat2VO(UserInfoData data) {
        UserInfoVO vo = null;
        if (null != data) {
            vo = new UserInfoVO();
            vo.setId(data.getId());
            vo.setUserName(data.getUserName());
            vo.setRealName(data.getRealName());
            vo.setSex(data.getSex());
            vo.setBirthday(data.getBirthday());
            vo.setIdentityCard(data.getIdentityCard());
            vo.setLobId(data.getLobId());
            vo.setMobilePhone(data.getMobilePhone());
            vo.setEmail(data.getEmail());
            vo.setCountry(data.getCountry());
            vo.setNation(data.getNation());
            vo.setByyx(data.getByyx());
            vo.setCreateTime(data.getCreateTime());
            vo.setDeleteFlag(data.getDeleteFlag());
            vo.setUpdateTime(data.getUpdateTime());
        }
        return vo;
    }

}
