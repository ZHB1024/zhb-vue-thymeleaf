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

/*import com.zhb.forever.framework.ftp.FTPUtil;
import com.zhb.forever.framework.ftp.FtpClient;*/
import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.vue.web.util.WebAppUtil;

/** 
 * @ClassName: FtpController
 * @description: 
 * @author: 张会彬
 * @Date: 2019年6月29日 下午5:44:50
 */

@Controller
@RequestMapping("/htgl/ftpcontroller")
public class FtpController {
    
    private Logger logger = LoggerFactory.getLogger(FtpController.class);
    
    //toindex
    @RequestMapping(value = "/toindex",method = RequestMethod.GET)
    @Transactional
    public String toIndex(HttpServletRequest request,HttpServletResponse response) {
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            return "login/login";
        }
        return "htgl/ftp/index";
    }
    
    //查询
    @RequestMapping(value = "/listftpfiles/api")
    @ResponseBody
    @Transactional
    public AjaxData listFtpFiles(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(WebAppUtil.getUserId(request))) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        //FtpClient ftpClient = new FtpClient();
        //FTPUtil.listFiles(ftpClient);
        ajaxData.setFlag(true);
        return ajaxData;
    }

}
