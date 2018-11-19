package com.zhb.vue.web.controller;

import java.util.concurrent.ExecutionException;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhb.forever.framework.proto.ProtobufUtil;
import com.zhb.forever.framework.proto.protobuf.KeyValueProtobuf;
import com.zhb.forever.framework.proto.protobuf.KeyValueProtobuf.KeyValue;
import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.mq.Constants;
import com.zhb.forever.mq.activemq.ActiveMQClientFactory;
import com.zhb.forever.mq.activemq.client.ActiveMQClient;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月23日上午11:30:06
*/

@Controller
@RequestMapping("/htgl/mqcontroller")
public class MQController {
    
    private Logger logger = LoggerFactory.getLogger(MQController.class);
    
    private ActiveMQClient activeMqClient = ActiveMQClientFactory.getRedisClientBean();
    private Destination activeMqDestination = ActiveMQClientFactory.getMQDestinationBean();
    
    //private KafkaTemplate kafkaProducerTemplate = KafkaFactory.getKafkaProducerTemplateBean();
    
    @RequestMapping(value = "/toindex",method = RequestMethod.GET)
    @Transactional
    public String toSpider(HttpServletRequest request,HttpServletResponse response){
        return "htgl/mq/index";
    }
    
    //sendMessage
    @RequestMapping(value="/sendmessage",method=RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData sendMessage(HttpServletRequest request,HttpServletResponse response){
        AjaxData ajaxData = new AjaxData();
        
        /*activeMqClient.sendQueueDestinationMsg(activeMqDestination, "hello world");
        
        TextMessage textMessage = activeMqClient.receiveQueueMessage(activeMqDestination);
        if (null != textMessage) {
            try {
                logger.info(textMessage.getText());
                ajaxData.setData(textMessage.getText());
                ajaxData.setFlag(true);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }*/
        
        KeyValueProtobuf.KeyValue.Builder newsBuilder = KeyValueProtobuf.KeyValue.newBuilder(); 
        newsBuilder.setId("123");
        newsBuilder.setKey("测试");
        newsBuilder.setValue("测试一下不行呀");
        newsBuilder.setCount(10);
        KeyValue news = newsBuilder.build();
        byte[] newsByte = news.toByteArray();
        activeMqClient.sendQueueRemoteMsg(Constants.ACTIVE_MQ_KEYVALUE_DESTINATION_NAME, newsByte);
        ajaxData.setData("发送成功");
        ajaxData.setFlag(true);
        
        return ajaxData;
    }
    
    @RequestMapping(value ="/receivemessage", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData receiveQueueMes(HttpServletRequest request, HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        try {
            com.google.protobuf.Message mes = activeMqClient.receiveQueueRemoteMsgByDesNamePath(Constants.ACTIVE_MQ_KEYVALUE_DESTINATION_NAME, ProtobufUtil.KEY_VALUE_PROTOBUF_CLASS_PATH);
            if (null != mes) {
                KeyValueProtobuf.KeyValue news2 = (KeyValueProtobuf.KeyValue)mes;
                logger.info("从队列 " + Constants.ACTIVE_MQ_KEYVALUE_DESTINATION_NAME + " 收到了消息：");
                logger.info(news2.getId());
                logger.info(news2.getKey());
                logger.info(news2.getValue());
                logger.info(news2.getCount()+"");
                ajaxData.setData(news2.toString());
                ajaxData.setFlag(true);
            }else {
                ajaxData.setData("没有收到消息");
                ajaxData.setFlag(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ajaxData;
    }

}


