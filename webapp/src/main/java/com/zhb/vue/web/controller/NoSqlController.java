package com.zhb.vue.web.controller;

import java.util.List;

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
import com.zhb.forever.nosql.mongodb.client.MongoDBClient;
import com.zhb.forever.nosql.mongodb.client.MongoDBClientFactory;
import com.zhb.forever.nosql.mongodb.collection.UserModel;
import com.zhb.vue.params.AttachmentInfoParam;
import com.zhb.vue.web.util.WebAppUtil;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年11月1日下午4:35:09
*/

@Controller
@RequestMapping("/htgl/nosqlController")
public class NoSqlController {
    
    private Logger logger = LoggerFactory.getLogger(NoSqlController.class);
    
    private MongoDBClient mongoDBClient = MongoDBClientFactory.getMongoDBClientBean();
    

    //toindex
    @RequestMapping(value = "/toindex",method = RequestMethod.GET)
    @Transactional
    public String toIndex(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return "login.index";
        }
        return "htgl.nosql.index";
    }
    
    //查询
    @RequestMapping(value = "/querymongodb/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData queryMongoDB(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        
        /*UserModel userModels = new UserModel();
        userModels.setId("1234567");
        userModels.setName("王凌霄");
        userModels.setSex("女");
        mongoDBClient.save(userModels);*/
        
        List<UserModel> users = mongoDBClient.findByProp("sex", "女", UserModel.class);
        if (null != users) {
            for (UserModel userModel : users) {
                logger.info(userModel.getId() + "--" + userModel.getName() + "--" + userModel.getSex());
            }
            ajaxData.setFlag(true);
            ajaxData.setData("测试成功");
            return ajaxData;
        }
        
        ajaxData.setFlag(false);
        ajaxData.addMessage("测试失败");
        return ajaxData;
    }

}


