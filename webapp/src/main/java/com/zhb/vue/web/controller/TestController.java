package com.zhb.vue.web.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhb.file.client.service.FileServiceClient;
import com.zhb.file.client.service.FileServiceClientFactory;
import com.zhb.forever.framework.design.pattern.Singleton;
import com.zhb.forever.framework.util.CheckAgentUtil;
import com.zhb.forever.framework.util.IPUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.KeyValueVO;
import com.zhb.forever.nosql.redis.client.RedisClient;
import com.zhb.forever.nosql.redis.client.RedisClientFactory;
import com.zhb.vue.pojo.UserInfoData;
import com.zhb.vue.service.UserInfoService;
import com.zhb.vue.web.controller.base.BaseController;
import com.zhb.vue.web.util.WebAppUtil;

@Controller
@RequestMapping("/htgl/testcontroller")
public class TestController extends BaseController{
    
    private Logger logger = LoggerFactory.getLogger(TestController.class);
    
    //private RedisClient redisClient = RedisClientFactory.getRedisClientBean();
    
    @Autowired
    private UserInfoService userInfoService;
    
    private RedisClient redisClient = RedisClientFactory.getRedisClientBean();
    
    private FileServiceClient fileClient = FileServiceClientFactory.getFileServiceClientBean();
    
    @RequestMapping(value = "/testredis")
    @Transactional
    public void testRedis(HttpServletRequest request,HttpServletResponse response) {
        
        /*UserInfoData data = userInfoService.getUserInfoById(WebAppUtil.getUserId(request));
        
        RedisImplUtil.del("hello".getBytes());
        
        List<UserInfoData> lists = new ArrayList<>();
        lists.add(data);
        
        ListTranscoder<UserInfoData> listTranscoder = new ListTranscoder<UserInfoData>();
        byte[] bytes = listTranscoder.serialize(lists);
        
        RedisImplUtil.set("hello".getBytes(), bytes);
        byte[] ress = RedisImplUtil.get("hello".getBytes());
        List<UserInfoData> res = listTranscoder.deserialize(ress);
        if (null != res) {
            for (UserInfoData userInfoData : res) {
                System.out.println(userInfoData.getRealName());
            }
        }*/
        
        /*List<String> countries = new ArrayList<String>();
        countries.add("China");
        countries.add("America");
        
        redisClient.set("hello", "hello world!123012");
        Object value =redisClient.get("hello");
        if (null != value) {
            logger.info(value.toString());
        }*/
        
        /*redisClient.set("hello", "hellosss");
        value =redisClient.get("hello");
        if (null != value) {
            logger.info(value.toString());
        }*/
        
        /*Set<?> setTemps2 = redisClient.getSet("number");
        if (null != setTemps2) {
            for (Object object : setTemps2) {
                logger.info(object.toString());
            }
        }*/
        
        
        /*redisClient.addList("zhb-vue-list", countries);
        List<?> result = redisClient.getList("zhb-vue-list");
        if (null != result) {
            for (Object object : result) {
                logger.info(object.toString());
            }
        }
        
        Set<String> sets = new TreeSet<String>();
        sets.add("11");
        sets.add("33");
        sets.add("22");
        sets.add("22");
        redisClient.addSet("number", sets);
        */
    }
    
    @RequestMapping(value = "/checkagent")
    @Transactional
    public void checkIsMobile(HttpServletRequest request,HttpServletResponse response) {
        String userAgent = request.getHeader("user-agent");
        boolean flag = CheckAgentUtil.checkAgentIsMobile(userAgent);
        logger.info(userAgent);
        logger.info(flag + "");
        
    }
    
    @RequestMapping(value = "/ip")
    @Transactional
    public void ip(HttpServletRequest request,HttpServletResponse response) {
        String ip = IPUtil.getIp(request);
        logger.info(ip);
    }
    
    
    @RequestMapping("index")
    @Transactional
    public String index() {
        System.out.println(Singleton.test);
        Singleton.getInstance();
        
        return "htgl/index_thymeleaf";
    }
    
    @RequestMapping("testremote")
    @Transactional
    public String testRemote() {
        String result = fileClient.getFileNameById("");
        logger.info(result);
        
        List<KeyValueVO> fileList = fileClient.getFileList("", "");
        if (null != fileList) {
            for (KeyValueVO keyValueVO : fileList) {
                logger.info(keyValueVO.getId() + " || " + keyValueVO.getKey() + " || " + keyValueVO.getValue());
            }
        }
        return "index";
    }
    
    



}
