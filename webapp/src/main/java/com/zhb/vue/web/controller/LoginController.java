package com.zhb.vue.web.controller;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhb.forever.framework.dic.DeleteFlagEnum;
import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.EmailUtil;
import com.zhb.forever.framework.util.PasswordUtil;
import com.zhb.forever.framework.util.PropertyUtil;
import com.zhb.forever.framework.util.RandomUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.MailVO;
import com.zhb.vue.dic.FunctionTypeEnum;
import com.zhb.vue.dic.VerificationCodeTypeEnum;
import com.zhb.vue.params.UserInfoParam;
import com.zhb.vue.params.VerificationCodeInfoParam;
import com.zhb.vue.pojo.FunctionInfoData;
import com.zhb.vue.pojo.IconInfoData;
import com.zhb.vue.pojo.UserFunctionInfoData;
import com.zhb.vue.pojo.UserInfoData;
import com.zhb.vue.pojo.VerificationCodeInfoData;
import com.zhb.vue.service.FunctionInfoService;
import com.zhb.vue.service.IconInfoService;
import com.zhb.vue.service.UserInfoService;
import com.zhb.vue.service.VerificationCodeInfoService;
import com.zhb.vue.util.Data2VO;
import com.zhb.vue.web.util.WebAppUtil;
import com.zhb.vue.web.util.WriteJSUtil;
import com.zhb.vue.web.vo.LoginInfoVO;

@Controller
@RequestMapping("/logincontroller")
public class LoginController {
    
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private VerificationCodeInfoService verificationCodeInfoService;
    
    @Autowired
    private FunctionInfoService functionInfoService;
    
    @Autowired
    private IconInfoService iconInfoService;
    
    //tologin
    @RequestMapping(value = "/tologin",method = RequestMethod.GET)
    @Transactional
    public String toLogin(HttpServletRequest request,HttpServletResponse response, Model model) {
        logger.info(String.valueOf(request.getAttribute("redirectUrl")));
        model.addAttribute("redirectUrl", request.getAttribute("redirectUrl"));
        return "login/login";
    }
    
    //登录
    @RequestMapping(value = "/login/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData login(HttpServletRequest request,HttpServletResponse response,UserInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(param.getUserName())|| StringUtil.isBlank(param.getPassword())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请填写用户名或密码");
            return ajaxData;
        }
        List<UserInfoData> userInfoDatas = userInfoService.getUserInfos(param,null);
        
        if (null == userInfoDatas || userInfoDatas.size() == 0 ) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("没有这个用户");
            return ajaxData;
        }
        UserInfoData userInfoData = userInfoDatas.get(0);
        String password = PasswordUtil.encrypt(param.getUserName(), param.getPassword(), PasswordUtil.generateSalt(userInfoData.getSalt()));
        if (!userInfoData.getPassword().equals(password)) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("密码错误");
            return ajaxData;
        }
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        
        loginInfoVO.setUserInfoVO(Data2VO.userInfoDat2VO(userInfoData));
        WebAppUtil.setLogInfoVO(request, loginInfoVO);
        WebAppUtil.setUserId(request, userInfoData.getId());
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //发送验证码，邮箱
    @RequestMapping(value = "/sendverificationcode/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData sendVerificationCode(HttpServletRequest request,HttpServletResponse response,UserInfoParam param,Integer type) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(param.getEmail())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请填写邮箱");
            return ajaxData;
        }
        
        if (null == type) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("非法访问");
            return ajaxData;
        }
        String remark = "";
        if (type == 0 ) {
            UserInfoParam userInfoParam2 = new UserInfoParam();
            userInfoParam2.setEmail(param.getEmail());
            List<UserInfoData> userInfoDatas2 = userInfoService.getUserInfos(userInfoParam2,null);
            if (null != userInfoDatas2 && userInfoDatas2.size() > 0) {
                ajaxData.setFlag(false);
                ajaxData.addMessage("邮箱已被别人使用，请重新输入新的邮箱");
                return ajaxData;
            }
            remark = "注册love系统的验证码";
        }else if(type == 1){
            if (StringUtil.isBlank(param.getUserName())) {
                ajaxData.setFlag(false);
                ajaxData.addMessage("请填写用户名");
                return ajaxData;
            }
            
            UserInfoParam userInfoParam2 = new UserInfoParam();
            userInfoParam2.setEmail(param.getEmail());
            userInfoParam2.setUserName(param.getUserName());
            List<UserInfoData> userInfoDatas2 = userInfoService.getUserInfos(userInfoParam2,null);
            if (null == userInfoDatas2 || userInfoDatas2.size() == 0) {
                ajaxData.setFlag(false);
                ajaxData.addMessage("用户名与邮箱不对应，请修改后重新发送");
                return ajaxData;
            }
            remark = "修改love系统的验证码";
        }
        
        String code = RandomUtil.getRandomString(6);
        MailVO mailVo = new MailVO(param.getEmail(),remark,code,PropertyUtil.getMailUserName(),PropertyUtil.getMailPassword(),PropertyUtil.getMailHost());
        String result = EmailUtil.sendMail(mailVo);
        if (StringUtil.isNotBlank(result)) {
            ajaxData.setFlag(false);
            ajaxData.addMessage(result);
            return ajaxData;
        }
        
        VerificationCodeInfoData verificationCodeInfoData = new VerificationCodeInfoData();
        verificationCodeInfoData.setEmail(param.getEmail());
        verificationCodeInfoData.setCode(code);
        if (type == 0) {
            verificationCodeInfoData.setType(VerificationCodeTypeEnum.REGISTER.getIndex());
        }else if (type == 1) {
            verificationCodeInfoData.setType(VerificationCodeTypeEnum.UPDATE_PASSWORD.getIndex());
        }
        verificationCodeInfoData.setDeleteFlag(DeleteFlagEnum.UDEL.getIndex());
        verificationCodeInfoData.setRemark(remark);
        verificationCodeInfoData.setCreateTime(Calendar.getInstance());
        verificationCodeInfoService.saveOrUpdate(verificationCodeInfoData);
        
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //注册
    @RequestMapping(value = "/register/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData register(HttpServletRequest request,HttpServletResponse response,UserInfoParam param,String code) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(param.getUserName()) || StringUtil.isBlank(param.getPassword()) || StringUtil.isBlank(param.getConfirmPassword())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请填写用户名、密码或确认密码");
            return ajaxData;
        }
        if (!param.getPassword().equals(param.getConfirmPassword())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("您两次输入的密码不一致，请重新输入");
            return ajaxData;
        }
        if (StringUtil.isBlank(param.getEmail())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请填写邮箱");
            return ajaxData;
        }
        
        UserInfoParam userInfoParam = new UserInfoParam();
        userInfoParam.setUserName(param.getUserName());
        List<UserInfoData> userInfoDatas = userInfoService.getUserInfos(userInfoParam,null);
        if (null != userInfoDatas && userInfoDatas.size() > 0) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("用户名已被别人使用，请重新输入新的用户名");
            return ajaxData;
        }
        
        UserInfoParam userInfoParam2 = new UserInfoParam();
        userInfoParam2.setEmail(param.getEmail());
        List<UserInfoData> userInfoDatas2 = userInfoService.getUserInfos(userInfoParam2,null);
        if (null != userInfoDatas2 && userInfoDatas2.size() > 0) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("邮箱已被别人使用，请重新输入新的邮箱");
            return ajaxData;
        }
        
        if (StringUtil.isBlank(code)) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请输入验证码");
            return ajaxData;
        }
        
        VerificationCodeInfoParam verificationCodeInfoParam = new VerificationCodeInfoParam();
        verificationCodeInfoParam.setType(VerificationCodeTypeEnum.REGISTER.getIndex());
        verificationCodeInfoParam.setEmail(param.getEmail());
        verificationCodeInfoParam.setDeleteFlag(DeleteFlagEnum.UDEL.getIndex());
        List<VerificationCodeInfoData> verificationCodeInfoDatas = verificationCodeInfoService.getVerificationCodes(verificationCodeInfoParam, null);
        if (null == verificationCodeInfoDatas || verificationCodeInfoDatas.size() == 0) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("系统没有收到验证码，请重新发送");
            return ajaxData;
        }
        VerificationCodeInfoData verificationCodeInfoData = verificationCodeInfoDatas.get(0);
        if (!code.equals(verificationCodeInfoData.getCode())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("您输入的验证码不正确，请修改后重新输入");
            return ajaxData;
        }
        
        UserInfoData userInfoData = new UserInfoData();
        userInfoData.setUserName(param.getUserName());
        userInfoData.setEmail(param.getEmail());
        String salt = RandomUtil.getRandomString(8);
        userInfoData.setSalt(salt);
        userInfoData.setPassword(PasswordUtil.encrypt(param.getUserName(), param.getPassword(), PasswordUtil.generateSalt(salt)));
        userInfoService.saveOrUpdate(userInfoData);
        
        verificationCodeInfoData.setDeleteFlag(DeleteFlagEnum.FINISH.getIndex());
        verificationCodeInfoData.setUpdateTime(Calendar.getInstance());
        verificationCodeInfoService.saveOrUpdate(verificationCodeInfoData);
        
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //修改密码
    @RequestMapping(value = "/updatepassword/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData updatePassword(HttpServletRequest request,HttpServletResponse response,UserInfoParam param,String code) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(param.getUserName()) || StringUtil.isBlank(param.getPassword()) || StringUtil.isBlank(param.getConfirmPassword())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请填写用户名、密码或确认密码");
            return ajaxData;
        }
        if (!param.getPassword().equals(param.getConfirmPassword())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("您两次输入的密码不一致，请重新输入");
            return ajaxData;
        }
        if (StringUtil.isBlank(param.getEmail())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请填写邮箱");
            return ajaxData;
        }
        
        UserInfoParam userInfoParam = new UserInfoParam();
        userInfoParam.setUserName(param.getUserName());
        userInfoParam.setEmail(param.getEmail());
        List<UserInfoData> userInfoDatas = userInfoService.getUserInfos(userInfoParam,null);
        if (null == userInfoDatas || userInfoDatas.size() == 0) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("用户名与邮箱不对应，请修改后重新提交");
            return ajaxData;
        }
        
        if (StringUtil.isBlank(code)) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请输入验证码");
            return ajaxData;
        }
        
        VerificationCodeInfoParam verificationCodeInfoParam = new VerificationCodeInfoParam();
        verificationCodeInfoParam.setType(VerificationCodeTypeEnum.UPDATE_PASSWORD.getIndex());
        verificationCodeInfoParam.setEmail(param.getEmail());
        verificationCodeInfoParam.setDeleteFlag(DeleteFlagEnum.UDEL.getIndex());
        List<VerificationCodeInfoData> verificationCodeInfoDatas = verificationCodeInfoService.getVerificationCodes(verificationCodeInfoParam, null);
        if (null == verificationCodeInfoDatas || verificationCodeInfoDatas.size() == 0) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("系统没有收到验证码，请重新发送");
            return ajaxData;
        }
        VerificationCodeInfoData verificationCodeInfoData = verificationCodeInfoDatas.get(0);
        if (!code.equals(verificationCodeInfoData.getCode())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("您输入的验证码不正确，请修改后重新输入");
            return ajaxData;
        }
        
        UserInfoData userInfoData = userInfoDatas.get(0);
        userInfoData.setPassword(PasswordUtil.encrypt(userInfoData.getUserName(), param.getPassword(), PasswordUtil.generateSalt(userInfoData.getSalt())));
        userInfoData.setUpdateTime(Calendar.getInstance());
        userInfoService.saveOrUpdate(userInfoData);
        
        verificationCodeInfoData.setDeleteFlag(DeleteFlagEnum.FINISH.getIndex());
        verificationCodeInfoData.setUpdateTime(Calendar.getInstance());
        verificationCodeInfoService.saveOrUpdate(verificationCodeInfoData);
        
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //初始化root账号
    @RequestMapping(value = "/initroot/api",method = RequestMethod.GET)
    @Transactional
    public void initRoot(HttpServletRequest request,HttpServletResponse response) {
        
        UserInfoParam param = new UserInfoParam();
        param.setUserName("root");
        List<UserInfoData> userInfoDatas = userInfoService.getUserInfos(param,null);
        if (null == userInfoDatas || userInfoDatas.size() == 0 ) {
            //用户信息
            UserInfoData data = new UserInfoData();
            data.setUserName("root");
            String salt = RandomUtil.getRandomString(8);
            data.setPassword(PasswordUtil.encrypt("root", PasswordUtil.DEFAULT_PASSWORD, PasswordUtil.generateSalt(salt)));
            data.setSalt(salt);
            userInfoService.saveOrUpdate(data);
            
            //图标
            IconInfoData iconInfoData = new IconInfoData();
            iconInfoData.setName("用户管理");
            iconInfoData.setValue("ios-person");
            iconInfoService.saveOrUpdate(iconInfoData);
            
            //功能信息
            FunctionInfoData root = new FunctionInfoData();
            root.setName("用户管理");
            root.setType(FunctionTypeEnum.ROOT.getIndex());
            root.setPath("userinfocontroller");
            root.setIconInfoData(iconInfoData);
            root.setOrder(1);
            functionInfoService.saveOrUpdateFunctionInfoData(root);
            
            FunctionInfoData children = new FunctionInfoData();
            children.setName("个人信息");
            children.setType(FunctionTypeEnum.ONE_LEVEL.getIndex());
            children.setPath("/htgl/userinfocontroller/searchuserinfo");
            children.setOrder(2);
            children.setParentFunctionInfo(root);
            functionInfoService.saveOrUpdateFunctionInfoData(children);
            
            //人员功能关系
            UserFunctionInfoData userFunctionInfoData = new UserFunctionInfoData();
            userFunctionInfoData.setUserInfoData(data);
            userFunctionInfoData.setFunctionInfoData(children);
            functionInfoService.saveOrUpdateUserFunctionInfoData(userFunctionInfoData);
            
            
            WriteJSUtil.writeJS("init root success", response);
        }
        WriteJSUtil.writeJS("root 已存在", response);
    }
    
}
