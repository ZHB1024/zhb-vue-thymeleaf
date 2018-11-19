package com.zhb.vue.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zhb.forever.nosql.redis.client.RedisClient;
import com.zhb.forever.nosql.redis.client.RedisClientFactory;
import com.zhb.vue.Constant;
import com.zhb.vue.pojo.FunctionInfoData;

@Aspect
@Component
public class FlushFunctionInfoDataAspect {

    private Logger logger = LoggerFactory.getLogger(FlushFunctionInfoDataAspect.class);
    
    private RedisClient redisClient = RedisClientFactory.getRedisClientBean();

    @Pointcut("execution(public * com.zhb.vue.service.impl.FunctionInfoServiceImpl.saveOrUpdateFunctionInfoData(..))")
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
            redisClient.del(Constant.FUNCTION_INFO_DATAS.getBytes());
            FunctionInfoData data = (FunctionInfoData)object;
            redisClient.del(data.getId().getBytes());
            logger.info("delete functionIndoDatas and " + data.getName() + " redis cache...");
        }
    }
    
    @AfterThrowing(value = "flush()")
    public void afterThrowException() {
        logger.error("异常。。。。");
    }

}
