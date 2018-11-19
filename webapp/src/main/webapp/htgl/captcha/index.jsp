<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
String ctxPath = request.getContextPath();
%>
<style> 
p{
margin-top:20px;
margin-left:30px;
}
</style>

<div id="app_content" style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item>首页</breadcrumb-item> 
            <breadcrumb-item>验证码管理</breadcrumb-item> 
            <breadcrumb-item>验证码信息</breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
            <Row style="background:#eee;padding:20px">
              <i-col span="20">
                  <Card :bordered="false">
                      <Row>
                        <i-col span="5">
                          <i-form method="post" action="" ref="formValidate" :model="captchaInfo" :rules="ruleValidate" :label-width="80">
		                	
		                  	<form-item label="验证码" prop="captcha">
                  				<i-input type="text" name="captcha" v-model="captchaInfo.captcha" :maxlength="15" placeholder="请输入验证码"></i-input>
                			</form-item >
                	
                  			<form-item align="center">
                				<i-button type="primary" @click="handleSubmit('formValidate')">确 定</i-button>
                  			</form-item>
                  
                			</i-form>
                        </i-col>
                        
                        <i-col span="15">
                          <p style="margin-top:50px;margin-left: 50px;">
                          	<img :src="captchaInfo.imageUrl"  @click="flushImage" width="100" height="50"/>
                          </p>
                        </i-col>
                      </Row>
                  </Card>
              </i-col>
          </Row>
        </i-content>
        
    </Layout>
</div>

<script>
var myVue =  new Vue({
	  el: '#app_content',
	  data:{
		  captchaInfo:{
			  imageUrlConstant:'<%=ctxPath %>/htgl/captchaimagecontroller/getcaptchaimage/api',
			  imageUrl:'',
			  captcha:''
		  },
		  ruleValidate: {
			     captcha: [
	    		    { required: true, message: '请填写验证码', trigger: 'change' }
	    		  ] 
	      } 
	  },
	  created: function () {
		  this.captchaInfo.imageUrl = this.captchaInfo.imageUrlConstant;
		  <%-- axios.all([
	    	    axios.get('<%=ctxPath %>/htgl/userinfocontroller/getselfinfo/api')
	    	  ]).then(axios.spread(function (userInfoResp) {
	    		  myVue.userInfo = userInfoResp.data.data;
	    		  myVue.userInfo.headSrc = '<%=ctxPath%>/htgl/attachmentinfocontroller/getoriginalattachmentinfo?id=' + userInfoResp.data.data.lobId;
	    	  })); --%>
	 },
	  methods:{
		  	handleSubmit:function (name) {
			   myVue.$refs[name].validate((valid) => {
				   if (valid){
					    let param = new URLSearchParams(); 
		          	  	param.append("captcha",myVue.captchaInfo.captcha); 
		          	  	axios.post('<%=ctxPath%>/htgl/captchaimagecontroller/validatecaptchaimage/api', param)
		          		  	 .then(function (response) {
		          			  	 if(response.data.flag){
		          			  		myVue.$Message.success({
		                                content: response.data.data,
		                                duration: 3,
		                                closable: true
		                            });
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
	        flushImage:function(){
	        	var now = new Date()
	        	myVue.captchaInfo.imageUrl = myVue.captchaInfo.imageUrlConstant + "?" +now.getTime();
	            this.$forceUpdate();
	        }
	  }
});

</script>