package com.zhb.vue.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.PasswordUtil;
import com.zhb.forever.framework.util.RandomUtil;
import com.zhb.forever.framework.util.StringUtil;
import com.zhb.forever.framework.vo.OrderVO;
import com.zhb.forever.framework.vo.UserInfoVO;
import com.zhb.vue.params.UserInfoParam;
import com.zhb.vue.pojo.UserFunctionInfoData;
import com.zhb.vue.pojo.UserInfoData;
import com.zhb.vue.service.FunctionInfoService;
import com.zhb.vue.service.UserInfoService;
import com.zhb.vue.util.Data2VO;
import com.zhb.vue.web.controller.base.BaseController;
import com.zhb.vue.web.util.CheckUtil;
import com.zhb.vue.web.util.Data2JSONUtil;
import com.zhb.vue.web.util.FlushSessionUtil;
import com.zhb.vue.web.util.Param2DataUtil;
import com.zhb.vue.web.util.WebAppUtil;

@Controller
@RequestMapping("/htgl/userinfocontroller")
public class UserInfoController extends BaseController{
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private FunctionInfoService functionInfoService;
    
    @RequestMapping(value="/toindex",method=RequestMethod.GET)
    @Transactional
    public String toIndex(HttpServletRequest request,HttpServletResponse response) {
        return "htgl/user/index";
    }
    
    
    //查询用户信息
    @RequestMapping(value="/getuserinfo/api")
    @ResponseBody
    @Transactional
    public AjaxData getUserInfo(HttpServletRequest request,HttpServletResponse response,UserInfoParam param) {
        AjaxData ajaxData = searchUserInfo2AjaxData(param,request);
        return ajaxData;
    }
    
    //获取所有的用户
    @RequestMapping(value="/getalluserinfo/api")
    @ResponseBody
    @Transactional
    public AjaxData getAllUserInfo(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        //排序字段
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo = new OrderVO("deleteFlag",true);
        orderVos.add(vo);
        OrderVO vo2 = new OrderVO("createTime",false);
        orderVos.add(vo2);
        
        List<UserInfoData> datas = userInfoService.getAllUserInfos(orderVos);
        if (null != datas) {
            ajaxData.setData(Data2JSONUtil.userInfoDatas2JSONArray(datas));
        }
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //to用户个人信息
    @RequestMapping(value="/toselfinfo",method=RequestMethod.GET)
    @Transactional
    public String toSelfInfo(HttpServletRequest request,HttpServletResponse response) {
        UserInfoVO vo = WebAppUtil.getLoginInfoVO(request).getUserInfoVO();
        if (null == vo) {
            return "login/login";
        }
        
        return "htgl/user/info";
    }
    
    //获取个人信息
    @RequestMapping(value="/getselfinfo/api")
    @ResponseBody
    @Transactional
    public AjaxData getSelfInfo(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        UserInfoVO vo = WebAppUtil.getLoginInfoVO(request).getUserInfoVO();
        if (null == vo) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        ajaxData.setFlag(true);
        ajaxData.setData(Data2JSONUtil.userInfoVO2JSONObject(vo));
        return ajaxData;
    }
    
    //to新增一个用户
    @RequestMapping(value="/toadd",method=RequestMethod.GET)
    @Transactional
    public String toAdd(HttpServletRequest request,HttpServletResponse response) {
        UserInfoVO vo = WebAppUtil.getLoginInfoVO(request).getUserInfoVO();
        if (null == vo) {
            return "login/login";
        }
        return "htgl/user/add";
    }
    
    //新增一个用户
    @RequestMapping(value="/adduserinfo/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData addUserInfo(HttpServletRequest request,HttpServletResponse response,@Validated UserInfoParam param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errors(bindingResult);
        }
        AjaxData ajaxData = new AjaxData();
        UserInfoVO vo = WebAppUtil.getLoginInfoVO(request).getUserInfoVO();
        if (null == vo) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        if (StringUtil.isBlank(param.getUserName())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请输入用户名");
            return ajaxData;
        }
        if (StringUtil.isBlank(param.getEmail())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请输入邮箱");
            return ajaxData;
        }
        
        if (StringUtil.isBlank(param.getPassword())|| StringUtil.isBlank(param.getConfirmPassword())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请输入密码或确认密码");
            return ajaxData;
        }
        if (!param.getPassword().equals(param.getConfirmPassword())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("密码与确认密码必须相同");
            return ajaxData;
        }
        
        List<UserInfoData> datas = userInfoService.getAllUserInfos(null);
        if (null != datas) {
            for (UserInfoData userInfoData : datas) {
                if (userInfoData.getUserName().equals(param.getUserName())) {
                    ajaxData.setFlag(false);
                    ajaxData.addMessage("此用户名已存在，请更换用户名");
                    return ajaxData;
                }
                if (userInfoData.getEmail().equals(param.getEmail())) {
                    ajaxData.setFlag(false);
                    ajaxData.addMessage("此邮箱已被别人使用，请更换邮箱");
                    return ajaxData;
                }
            }
        }
        
        UserInfoData userInfoData = new UserInfoData();
        userInfoData.setUserName(param.getUserName());
        userInfoData.setEmail(param.getEmail());
        String salt = RandomUtil.getRandomString(8);
        userInfoData.setSalt(salt);
        userInfoData.setPassword(PasswordUtil.encrypt(param.getUserName(), param.getPassword(), PasswordUtil.generateSalt(salt)));
        userInfoService.saveOrUpdate(userInfoData);
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //to修改用户个人信息
    @RequestMapping(value="/toupdate",method=RequestMethod.GET)
    @Transactional
    public String toUpdate(HttpServletRequest request,HttpServletResponse response, Model model) {
        UserInfoVO vo = WebAppUtil.getLoginInfoVO(request).getUserInfoVO();
        if (null == vo) {
            return "login/login";
        }
        model.addAttribute("userInfoJson", Data2JSONUtil.userInfoVO2JSONObject(vo));
        return "htgl/user/update";
    }
    
    //修改个人信息
    @RequestMapping(value="/updateselfinfo/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData updateSelfInfo(HttpServletRequest request,HttpServletResponse response,UserInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        UserInfoVO vo = WebAppUtil.getLoginInfoVO(request).getUserInfoVO();
        if (null == vo) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        String msg = CheckUtil.userInfoParamCheck(param);
        if (StringUtil.isNotBlank(msg)) {
            ajaxData.setFlag(false);
            ajaxData.addMessage(msg);
            return ajaxData;
        }
        
        if (!vo.getId().equals(param.getId()) || !vo.getUserName().equals(param.getUserName())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("您无权修改这条数据");
            return ajaxData;
        }
        
        UserInfoParam userInfoParam = new UserInfoParam();
        userInfoParam.setId(param.getId());
        List<UserInfoData> datas = userInfoService.getUserInfos(userInfoParam,null);
        UserInfoData userInfoData = null;
        if (null != datas && datas.size() > 0 ) {
            userInfoData = datas.get(0);
            Param2DataUtil.userInfoParam2Data(param, userInfoData);
            userInfoService.saveOrUpdate(userInfoData);
            
            //刷新用户缓存
            FlushSessionUtil.flushWebAppUserInfo(request,Data2VO.userInfoDat2VO(userInfoData));
            
            ajaxData.setFlag(true);
            return ajaxData;
        }
        
        ajaxData.setFlag(false);
        ajaxData.addMessage("没有这条数据");
        return ajaxData;
    }
    
    //to修改密码
    @RequestMapping(value="/tomodifypassword",method=RequestMethod.GET)
    @Transactional
    public String toModifyPassword(HttpServletRequest request,HttpServletResponse response) {
        UserInfoVO vo = WebAppUtil.getLoginInfoVO(request).getUserInfoVO();
        if (null == vo) {
            return "login/login";
        }
        return "htgl/user/modify_password";
    }
    
    //修改密码
    @RequestMapping(value="/updatepassword/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData updatePassword(HttpServletRequest request,HttpServletResponse response,UserInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        String userId = WebAppUtil.getUserId(request);
        if (StringUtil.isBlank(userId)) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请先登录");
            return ajaxData;
        }
        
        if (StringUtil.isBlank(param.getUserName())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请填写用户名");
            return ajaxData;
        }
        if (StringUtil.isBlank(param.getPassword())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请填写原密码");
            return ajaxData;
        }
        if (StringUtil.isBlank(param.getNewPassword()) || StringUtil.isBlank(param.getConfirmPassword())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("请填写新密码或确认密码");
            return ajaxData;
        }
        
        if (!param.getNewPassword().equals(param.getConfirmPassword())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("新密码与确认密码不一致，请重新输入");
            return ajaxData;
        }
        
        UserInfoData data = userInfoService.getUserInfoById(userId);
        if (null == data) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("不存在这个用户");
            return ajaxData;
        }
        
        if (!data.getUserName().equals(param.getUserName())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("越权操作");
            return ajaxData;
        }
        
        String oldPassword = PasswordUtil.encrypt(param.getUserName(), param.getPassword(), PasswordUtil.generateSalt(data.getSalt()));
        if (!oldPassword.equals(data.getPassword())) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("您输入的原密码不正确");
            return ajaxData;
        }
        
        String newPassword = PasswordUtil.encrypt(data.getUserName(), param.getNewPassword(), PasswordUtil.generateSalt(data.getSalt()));
        data.setPassword(newPassword);
        userInfoService.saveOrUpdate(data);
        
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    
    //获取真实姓名
    @RequestMapping(value="/getrealname/api")
    @ResponseBody
    @Transactional
    public AjaxData getRealName(HttpServletRequest request,HttpServletResponse response) {
        AjaxData ajaxData = new AjaxData();
        UserInfoVO vo = WebAppUtil.getLoginInfoVO(request).getUserInfoVO();
        if (null == vo || StringUtil.isBlank(vo.getRealName())) {
            ajaxData.setData("崩溃了");
        }else {
            ajaxData.setData(vo.getRealName());
        }
        ajaxData.setFlag(true);
        return ajaxData;
    }
    
    //注销或开通账号
    @RequestMapping(value="/deloropenaccount/api",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxData delOrOpenAccount(HttpServletRequest request,HttpServletResponse response,UserInfoParam param) {
        AjaxData ajaxData = new AjaxData();
        if (StringUtil.isBlank(param.getId()) || null == param.getDeleteFlag()) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("非法操作");
            return ajaxData;
        }
        UserInfoData data = userInfoService.getUserInfoById(param.getId());
        if (null ==data ) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("非法操作");
            return ajaxData;
        }
        if (data.getUserName().equals("root")) {
            ajaxData.setFlag(false);
            ajaxData.addMessage("root账号不能注销");
            return ajaxData;
        }
        data.setDeleteFlag(param.getDeleteFlag());
        userInfoService.saveOrUpdate(data);
        
        //注销账号时，需要删除授权信息
        if (param.getDeleteFlag() == 1) {
            List<UserFunctionInfoData> datas = functionInfoService.getDataByUser(data);
            if (null != datas && datas.size() > 0) {
                for (UserFunctionInfoData userFunctionInfoData : datas) {
                    functionInfoService.delUserFunctionInfoData(userFunctionInfoData);
                }
            }
        }
        ajaxData = searchUserInfo2AjaxData(null, request);
        return ajaxData;
    }
    
    //获取用户
    @RequestMapping(value="/getusers/api")
    @ResponseBody
    @Transactional
    public AjaxData getUsers(HttpServletRequest request,HttpServletResponse response,UserInfoParam param) {
        AjaxData ajaxData = searchUserInfo2AjaxData(param,request);
        return ajaxData;
    }
    
    //退出系统
    @RequestMapping("/exit")
    @Transactional
    public void exit(HttpServletRequest request,HttpServletResponse response) {
        WebAppUtil.exit(request);
        try{
            response.sendRedirect("/logincontroller/tologin");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
    //共用查询
    private AjaxData searchUserInfo2AjaxData(UserInfoParam param,HttpServletRequest request) {
        AjaxData ajaxData = new AjaxData();
        if (null == param) {
            param = new UserInfoParam();
        }
        
        //排序字段
        List<OrderVO> orderVos = new ArrayList<>();
        OrderVO vo = new OrderVO("deleteFlag",true);
        orderVos.add(vo);
        OrderVO vo2 = new OrderVO("createTime",false);
        orderVos.add(vo2);
        
        List<UserInfoData> userInfos = userInfoService.getUserInfos(param,orderVos);
        if (null != userInfos) {
            ajaxData.setData(Data2JSONUtil.userInfoDatas2JSONArray(userInfos));
        }
        ajaxData.setFlag(true);
        return ajaxData;
    }

}
