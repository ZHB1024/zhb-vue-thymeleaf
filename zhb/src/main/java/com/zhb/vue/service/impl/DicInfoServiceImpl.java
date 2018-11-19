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
import com.zhb.vue.dao.DicInfoDao;
import com.zhb.vue.params.DicInfoParam;
import com.zhb.vue.pojo.DicInfoData;
import com.zhb.vue.service.DicInfoService;
import com.zhb.vue.service.base.impl.CommonServiceImpl;

@Service
public class DicInfoServiceImpl extends CommonServiceImpl implements DicInfoService {
    
    @Autowired
    private DicInfoDao dicInfoDao;

    @Override
    public void saveOrUpdate(DicInfoData data) {
        dicInfoDao.saveOrUpdate(data);
    }
    
    @Override
    public void saveOrUpdate(List<DicInfoData> datas) {
        if (null == datas || datas.size() == 0) {
            return;
        }
        
        for (DicInfoData dicInfoData : datas) {
            dicInfoDao.saveOrUpdate(dicInfoData);
        }
    }

    @Override
    public List<DicInfoData> getDicInfos(DicInfoParam param,List<OrderVO> orderVos) {
        RedisClient redisClient = RedisClientFactory.getRedisClientBean();
        byte[] bytes = redisClient.get(param.getCategory().getBytes());
        if (null != bytes) {
            ListTranscoder<DicInfoData> listTranscoder = new ListTranscoder<>();
            List<DicInfoData> datas = listTranscoder.deserialize(bytes);
            return datas;
        }
        List<DicInfoData> datas = dicInfoDao.getDicInfos(param,orderVos);
        if (null != datas && datas.size() > 0) {
            ListTranscoder<DicInfoData> listTranscoder = new ListTranscoder<>();
            bytes = listTranscoder.serialize(datas);
            redisClient.set(param.getCategory().getBytes(), bytes);
        }
        return datas;
    }

    @Override
    public Page<DicInfoData> getDicInfosPage(DicInfoParam param, List<OrderVO> orderVos) {
        long total = dicInfoDao.getDicInfosPageCount(param);
        if (total > 0 ) {
            List<DicInfoData> dicInfoDatas = dicInfoDao.getDicInfosPage(param,orderVos);
            //上一页可能有数据
            if ((null == dicInfoDatas || dicInfoDatas.size() == 0) && param.getCurrentPage() > 1) {
                param.setStart(param.getPageSize() * (param.getCurrentPage()-2));
                dicInfoDatas = dicInfoDao.getDicInfosPage(param,orderVos);
            }
            Page<DicInfoData> page = PageUtil.getPage(dicInfoDatas.iterator(), param.getStart(), param.getPageSize(), total);
            return page;
        }
        return null;
    }

    @Override
    public List<String> getDicCategory() {
        return dicInfoDao.getDicCategory();
    }

    @Override
    public List<String> getDicTypeByCategory(DicInfoParam param) {
        return dicInfoDao.getDicTypeByCategory(param);
    }

    @Override
    public List<Object[]> statisticDic() {
        return dicInfoDao.statisticDic();
    }
    
}
