package com.zhb.vue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhb.forever.framework.page.Page;
import com.zhb.forever.framework.page.PageUtil;
import com.zhb.forever.framework.serialize.impl.ListTranscoder;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.forever.nosql.redis.client.RedisClient;
import com.zhb.forever.nosql.redis.client.RedisClientFactory;
import com.zhb.vue.Constant;
import com.zhb.vue.dao.FunctionInfoDao;
import com.zhb.vue.dao.UserFunctionInfoDao;
import com.zhb.vue.params.FunctionInfoParam;
import com.zhb.vue.params.UserFunctionInfoParam;
import com.zhb.vue.pojo.FunctionInfoData;
import com.zhb.vue.pojo.UserFunctionInfoData;
import com.zhb.vue.pojo.UserInfoData;
import com.zhb.vue.service.FunctionInfoService;
import com.zhb.vue.service.base.impl.CommonServiceImpl;

@Service
public class FunctionInfoServiceImpl  extends CommonServiceImpl implements FunctionInfoService {
    
    @Autowired
    private FunctionInfoDao functionInfoDao;
    
    @Autowired
    private UserFunctionInfoDao userFunctionInfoDao;
    
    private RedisClient redisClient = RedisClientFactory.getRedisClientBean();

    @Override
    public FunctionInfoData saveOrUpdateFunctionInfoData(FunctionInfoData data) {
        if (null == data) {
            return null;
        }
        functionInfoDao.saveOrUpdate(data);
        return data;
    }

    @Override
    public UserFunctionInfoData saveOrUpdateUserFunctionInfoData(UserFunctionInfoData data) {
        if (null == data) {
            return null;
        }
        userFunctionInfoDao.saveOrUpdate(data);
        return data;
    }
    
    @Override
    public void delFunctionInfoData(FunctionInfoData data) {
        functionInfoDao.delFunctionInfoData(data);
    }

    @Override
    public void delUserFunctionInfoData(UserFunctionInfoData data) {
        userFunctionInfoDao.delUserFunctionInfoData(data);
    }

    @Override
    public List<FunctionInfoData> getFunctions(FunctionInfoParam param,List<OrderVO> orderVos) {
        return functionInfoDao.getFunctions(param,orderVos);
    }
    
    @Override
    public List<FunctionInfoData> getAllFunctions(List<OrderVO> orderVos) {
        byte[] bytes = redisClient.get(Constant.FUNCTION_INFO_DATAS.getBytes());
        if (null != bytes) {
            ListTranscoder<FunctionInfoData> listTranscoder = new ListTranscoder<>();
            List<FunctionInfoData> datas = listTranscoder.deserialize(bytes);
            return datas;
        }
        
        List<FunctionInfoData> datas = functionInfoDao.getAllFunctions(orderVos);
        if (null != datas && datas.size() > 0) {
            ListTranscoder<FunctionInfoData> listTranscoder = new ListTranscoder<>();
            bytes = listTranscoder.serialize(datas);
            redisClient.set(Constant.FUNCTION_INFO_DATAS.getBytes(), bytes);
        }
        return datas;
    }
    
    @Override
    public FunctionInfoData getFunctionById(String id) {
        return functionInfoDao.getFunctionById(id);
    }
    
    @Override
    public int getMaxOrder(FunctionInfoParam param) {
        return functionInfoDao.getMaxOrder(param);
    }

    @Override
    public List<UserFunctionInfoData> getDataByUser(UserInfoData data) {
        return userFunctionInfoDao.getDataByUser(data);
    }
    
    @Override
    public List<UserFunctionInfoData> getUserFunctionInfoDatas(UserFunctionInfoParam param) {
        return userFunctionInfoDao.getUserFunctionInfoDatas(param);
    }

    @Override
    public Page<UserFunctionInfoData> getUserFunctionInfoDatasPage(UserFunctionInfoParam param) {
        long total = userFunctionInfoDao.countUserFunctionInfos(param);
        if (total > 0 ) {
            List<UserFunctionInfoData> userFunctionInfoDatas = userFunctionInfoDao.getUserFunctionInfoDatasPage(param);
            //上一页可能有数据
            if ((null == userFunctionInfoDatas || userFunctionInfoDatas.size() == 0) && param.getCurrentPage() > 1) {
                param.setStart(param.getPageSize() * (param.getCurrentPage()-2));
                userFunctionInfoDatas = userFunctionInfoDao.getUserFunctionInfoDatasPage(param);
            }
            Page<UserFunctionInfoData> page = PageUtil.getPage(userFunctionInfoDatas.iterator(), param.getStart(), param.getPageSize(), total);
            return page;
        }
        return null;
    }

}
