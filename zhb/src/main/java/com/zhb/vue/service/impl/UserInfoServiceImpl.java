package com.zhb.vue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhb.forever.framework.serialize.impl.ListTranscoder;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.forever.nosql.redis.client.RedisClient;
import com.zhb.forever.nosql.redis.client.RedisClientFactory;
import com.zhb.vue.Constant;
import com.zhb.vue.dao.UserInfoDao;
import com.zhb.vue.params.UserInfoParam;
import com.zhb.vue.pojo.UserInfoData;
import com.zhb.vue.service.UserInfoService;
import com.zhb.vue.service.base.impl.CommonServiceImpl;

@Service
public class UserInfoServiceImpl extends CommonServiceImpl  implements UserInfoService {
    
    @Autowired
    private UserInfoDao userInfoDao;
    
    private RedisClient redisClient = RedisClientFactory.getRedisClientBean();

    @Override
    public UserInfoData saveOrUpdate(UserInfoData data) {
        if (null == data) {
            return null;
        }
        userInfoDao.saveOrUpdate(data);
        return data;
    }

    @Override
    public List<UserInfoData> getUserInfos(UserInfoParam userInfoParam,List<OrderVO> orderVos) {
        return userInfoDao.getUserInfos(userInfoParam,orderVos);
    }
    
    @Override
    public List<UserInfoData> getAllUserInfos(List<OrderVO> orderVos){
        byte[] bytes = redisClient.get(Constant.USER_INFO_DATAS.getBytes());
        if (null != bytes) {
            ListTranscoder<UserInfoData> listTranscoder = new ListTranscoder<>();
            List<UserInfoData> datas = listTranscoder.deserialize(bytes);
            return datas;
        }
        List<UserInfoData> datas = userInfoDao.getAllUserInfos(orderVos);
        if (null != datas && datas.size() > 0) {
            ListTranscoder<UserInfoData> listTranscoder = new ListTranscoder<>();
            bytes = listTranscoder.serialize(datas);
            redisClient.set(Constant.USER_INFO_DATAS.getBytes(), bytes);
        }
        return datas;
    }

    @Override
    public UserInfoData getUserInfoById(String id) {
        return userInfoDao.getUserInfoById(id);
    }

}
