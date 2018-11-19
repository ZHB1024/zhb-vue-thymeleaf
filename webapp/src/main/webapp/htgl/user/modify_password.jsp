<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
String ctxPath = request.getContextPath();
%>

<div id="app_content" style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item><a href="">首页</a></breadcrumb-item> 
            <breadcrumb-item>用户管理</breadcrumb-item> 
            <breadcrumb-item>修改密码</breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
          <div style="width:100%;">
            <div style="width: 600px;margin-left:  auto;margin-right:  auto;">
            	<i-form method="post" action="" ref="formValidate" :model="userInfo" :rules="ruleValidate" :label-width="80">
                	<input type="hidden" name="id" v-model="userInfo.id"/>
                	
                	
                	<form-item label="用户名" prop="detail">
                  		<i-input type="text" disabled name="userName" v-model="userInfo.userName" :maxlength="15" ></i-input>
                	</form-item >
                	
                	<form-item label="原密码" prop="password">
                		<i-input type="password" name="password" v-model="userInfo.password" :maxlength="15" placeholder="请输入原密码"></i-input>
                	</form-item >
                	
                	<form-item label="新密码" prop="newPassword">
                		<i-input type="password" name="newPassword" v-model="userInfo.newPassword" :maxlength="15" placeholder="请输入新密码"></i-input>
                	</form-item >
                	
                	<form-item label="确认密码" prop="confirmPassword">
                  		<i-input type="password" name="confirmPassword" v-model="userInfo.confirmPassword" :maxlength="15" placeholder="请确认新密码"></i-input>
                	</form-item >
                	
                  <form-item align="center">
                		<i-button type="primary" @click="handleSubmit('formValidate')">确 定</i-button>
                        <i-button @click="handleReset('formValidate')" style="margin-left: 8px">重置</i-button>
                  </form-item>
                  
                </i-form>
            </div>
          </div>
        </i-content>
        
    </Layout>
</div>


<script type="text/javascript">
var myVue =  new Vue({
	  el: '#app_content',
	  data:{
		  userInfo:{
			  id:'',
			  userName:'',
			  password:'',
			  newPassword:'',
			  confirmPassword:''
		  },
	      ruleValidate: {
	    	      password: [
		    		    { required: true, message: '请填写原密码', trigger: 'change' }
		    	  ],
		    	  newPassword: [
		    		    { required: true, message: '请填写新密码', trigger: 'change' }
		    	  ],
		    	  confirmPassword: [
		    		    { required: true, message: '请确认新密码', trigger: 'change' }
		    	  ]
	      } 
	  },
	  created: function () {
		  axios.all([
	    	    axios.get('<%=ctxPath %>/htgl/userinfocontroller/getselfinfo/api')
	    	  ]).then(axios.spread(function (userInfoResp) {
	    		  if(null != userInfoResp.data.data){
	    			  myVue.userInfo.id = userInfoResp.data.data.id;
	    			  myVue.userInfo.userName = userInfoResp.data.data.userName;
	    		  }
	    	  }));
	 },
	  methods:{
		   handleSubmit:function (name) {
			   myVue.$refs[name].validate((valid) => {
				   if (valid){
					    //等待动画
		   			    myVue.$Spin.show();
					   let param = new URLSearchParams(); 
		          	  	param.append("id",myVue.userInfo.id); 
		          	  	param.append("userName",myVue.userInfo.userName); 
		          	  	param.append("password",myVue.userInfo.password); 
		          	  	param.append("newPassword",myVue.userInfo.newPassword); 
		          	  	param.append("confirmPassword",myVue.userInfo.confirmPassword); 
		          	  	axios.post('<%=ctxPath%>/htgl/userinfocontroller/updatepassword/api', param)
		          		  	 .then(function (response) {
		          			  	 if(response.data.flag){
		          			  		myVue.$Message.success({
		                                content: "修改成功,请重新登录",
		                                duration: 1,
		                                closable: true
		                            });
		          			  	    myVue.$Spin.hide();
		          			  	    window.location.href='<%=ctxPath%>/htgl/userinfocontroller/exit';
		                        }else{
		                      	  myVue.$Message.error({
		                                content: response.data.errorMessages,
		                                duration: 3,
		                                closable: true
		                            });
		                        }
		          		  })
				   }
	            })
	                
	        },
	        handleReset:function (name) {
	        	myVue.$refs[name].resetFields();
	        }
	  }
});
</script>