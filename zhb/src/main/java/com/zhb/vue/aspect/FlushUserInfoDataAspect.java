package com.zhb.vue.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zhb.forever.nosql.redis.client.RedisClient;
import com.zhb.forever.nosql.redis.client.RedisClientFactory;
import com.zhb.vue.Constant;
import com.zhb.vue.pojo.UserInfoData;

@Aspect
@Component
public class FlushUserInfoDataAspect {
    
    private Logger logger = LoggerFactory.getLogger(FlushUserInfoDataAspect.class);
    
    private RedisClient redisClient = RedisClientFactory.getRedisClientBean();

    @Pointcut("execution(public * com.zhb.vue.service.impl.UserInfoServiceImpl.saveOrUpdate(..))")
    public void flush(){
    }
    
    @Before("flush()")
    public void doBefore(JoinPoint joinPoint){
    }
    
    @After("flush()")
    public void doAfter(){
    }
    
    @AfterReturning(returning="object",pointcut="flush()")
    public void doAfterReturning(Object object){
        if (null != object) {
            redisClient.del(Constant.USER_INFO_DATAS.getBytes());
            UserInfoData data = (UserInfoData)object;
            redisClient.del(data.getId().getBytes());
            logger.info("delete userInfoDatas and " + data.getRealName() + " redis cache...");
        }
    }

}
