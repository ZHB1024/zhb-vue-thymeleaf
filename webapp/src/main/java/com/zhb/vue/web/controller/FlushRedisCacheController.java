package com.zhb.vue.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.UserInfoVO;
import com.zhb.forever.nosql.redis.client.RedisClient;
import com.zhb.forever.nosql.redis.client.RedisClientFactory;
import com.zhb.vue.Constant;
import com.zhb.vue.web.util.WebAppUtil;
import com.zhb.vue.web.util.WriteJSUtil;

@Controller
@RequestMapping("/htgl/flushRedisCacheController")
public class FlushRedisCacheController {
    
    private Logger logger = LoggerFactory.getLogger(FlushRedisCacheController.class);
    
    private RedisClient redisClient = RedisClientFactory.getRedisClientBean();
    
    //toindex
    @RequestMapping(value = "/toindex",method = RequestMethod.GET)
    @Transactional
    public String toIndex(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return "login/login";
        }
        UserInfoVO vo = WebAppUtil.getLoginInfoVO(request).getUserInfoVO();
        if (null == vo || !"root".equals(vo.getUserName())) {
            return WriteJSUtil.writeJS("越权操作", response);
        }
        return "htgl/flushredis/index";
    }
    
    //刷新
    @RequestMapping(value = "/flushredis/api")
    @ResponseBody
    @Transactional
    public AjaxData flushRedis(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        UserInfoVO vo = WebAppUtil.getLoginInfoVO(request).getUserInfoVO();
        if (null == vo || !"root".equals(vo.getUserName())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("越权操作");
            return ajaxData;
        }
        
        redisClient.del(Constant.USER_INFO_DATAS.getBytes());
        redisClient.del(Constant.FUNCTION_INFO_DATAS.getBytes());
        
        ajaxData.setFlag(true);
        return ajaxData;
    }

}
