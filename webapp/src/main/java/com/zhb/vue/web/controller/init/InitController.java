package com.zhb.vue.web.controller.init;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.forever.framework.vo.UserInfoVO;
import com.zhb.vue.params.AttachmentInfoParam;
import com.zhb.vue.pojo.AttachmentInfoData;
import com.zhb.vue.service.AttachmentInfoService;
import com.zhb.vue.thread.solr.AttachmentInfo2SolrIndexThread;
import com.zhb.vue.web.util.WebAppUtil;
import com.zhb.vue.web.vo.LoginInfoVO;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月26日上午11:09:11
*/

@Controller
@RequestMapping("/htgl/init")
public class InitController {

    private Logger logger = LoggerFactory.getLogger(InitController.class);
    
    @Autowired
    private AttachmentInfoService attachmentInfoService;
    
    @RequestMapping(value = "/toindex",method = RequestMethod.GET)
    @Transactional
    public String toIndex(HttpServletRequest request,HttpServletResponse response){
        return "htgl.init.index";
    }
    
    //初始化附件索引
    @RequestMapping(value = "/initattachmentsolrIndex/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData initAttachmentSolrIndex(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        if (!isRoot(request)) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("越权操作");
            return ajaxData;
        }
        
        AttachmentInfo2SolrIndexThread.deleteAllAttachmentSolrIndex();
        
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo1 = new OrderVO("type",true);
        orderVos.add(vo1);
        OrderVO vo2 = new OrderVO("fileName",true);
        orderVos.add(vo2);
        
        List<AttachmentInfoData> infoDatas = attachmentInfoService.getAttachmentInfos(new AttachmentInfoParam(), orderVos);
        if (null != infoDatas && infoDatas.size() > 0) {
            AttachmentInfo2SolrIndexThread.createAttachmentSolrIndexs(infoDatas);
        }
        
        ajaxData.setFlag(true);
        ajaxData.setData("初始化附件索引成功，共 " + infoDatas.size() + " 个");
        return ajaxData;
    }
    
    private boolean isRoot(HttpServletRequest request) {
        LoginInfoVO vo = WebAppUtil.getLoginInfoVO(request);
        if (null == vo) {
            return false;
        }
        UserInfoVO userInfoVO = vo.getUserInfoVO();
        if (null == userInfoVO) {
            return false;
        }
        if ("root".equals(userInfoVO.getUserName())) {
            return true;
        }
        return false;
    }

}


