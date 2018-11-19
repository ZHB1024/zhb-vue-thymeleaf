<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
String ctxPath = request.getContextPath();
%>

<div id="app_content" style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item><a href="">首页</a></breadcrumb-item> 
            <breadcrumb-item>用户管理</breadcrumb-item> 
            <breadcrumb-item>修改信息</breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
          <div style="width:100%;">
            <div style="width: 600px;margin-left:  auto;margin-right:  auto;">
            	<i-form method="post" action="" ref="formValidate" :model="userInfo" :rules="ruleValidate" :label-width="80">
                	<input type="hidden" name="id" v-model="userInfo.id"/>
                	
                	
                	<form-item label="用户名" prop="detail">
                  		<i-input type="text" disabled name="userName" v-model="userInfo.userName" :maxlength="15" placeholder="请输入用户名"></i-input>
                	</form-item >
                	
                	<form-item label="姓名" prop="realName">
                  		<i-input type="text" name="realName" v-model="userInfo.realName" :maxlength="10" placeholder="请输入姓名"></i-input>
                	</form-item >
                	
                	<form-item label="国家/地区" prop="country">
                	  <i-select name="country" clearable filterable v-model="userInfo.country" placeholder="请选择国家/地区">
                        	<i-option v-bind:value="item.name" v-for="item in countryParm">
                        		{{item.name}}
                        	</i-option>
                    	</i-select>
                	</form-item>
                	
                	<form-item label="性别" prop="sex">
                  		<Radio-group v-model="userInfo.sex">
        					<Radio label="男">
            					<Icon type="md-male"></Icon>
            					<span>男</span>
        					</Radio>
        					<Radio label="女">
            					<Icon type="md-female"></Icon>
            					<span>女</span>
        					</Radio>
    					</Radio-group>
                	</form-item >
                	
                	
                	<form-item label="出生日期" prop="birthday">
                  		<Date-picker  name="birthday" type="date" :options="limitDate" v-model="userInfo.birthday"  format="yyyy-MM-dd"  placeholder="出生日期"  >
                    	</Date-picker>  
                	</form-item >
                	
                	<form-item label="身份证号" prop="identityCard">
                  		<i-input type="text" name="identityCard" v-model="userInfo.identityCard" :maxlength="18" placeholder="请输入身份证号"></i-input>
                	</form-item >
                	
                	<form-item label="民族" prop="nation">
                	  <i-select name="nation" clearable filterable v-model="userInfo.nation" placeholder="请选择民族">
                        	<i-option v-bind:value="item.name" v-for="item in mzParm">
                        		{{item.name}}
                        	</i-option>
                    	</i-select>
                	</form-item>
                	
                	<form-item label="毕业院校" prop="byyx">
        				<i-select name="byyx" clearable filterable v-model="userInfo.byyx" placeholder="请输入毕业院校">
                        	<i-option v-bind:value="item.name" v-for="item in byyxParm">
                        		{{item.name}}
                        	</i-option>
                		</i-select>
                	</form-item>
                	
                	<form-item label="电话" prop="mobilePhone">
                  		<i-input type="text" name="mobilePhone" v-model="userInfo.mobilePhone" :maxlength="11" placeholder="请输入电话"></i-input>
                	</form-item >
                	
                	<form-item label="邮箱" prop="email">
                  		<i-input type="text" disabled name="email" v-model="userInfo.email" :maxlength="30" placeholder="请输入邮箱"></i-input>
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
var userInfoJson = <%=request.getAttribute("userInfoJson") %> ;
var myVue =  new Vue({
	  el: '#app_content',
	  data:{
		  userInfo:{
			  id:userInfoJson.id,
			  userName:userInfoJson.userName,
			  realName:userInfoJson.realName,
			  birthday:userInfoJson.birthday,
			  sex:userInfoJson.sex,
			  identityCard:userInfoJson.identityCard,
			  country:userInfoJson.country,
			  nation:userInfoJson.nation,
			  byyx:userInfoJson.byyx,
			  mobilePhone:userInfoJson.mobilePhone,
			  email:userInfoJson.email
		  },
		  countryParm:[],
		  mzParm:[],
		  byyxParm:[],
		  limitDate:{
			  disabledDate (date) {
		             return date.valueOf() > Date.now() ;
		         }
		  },
	      ruleValidate: {
	    	     realName: [
	    		    { required: true, message: '请填写姓名', trigger: 'change' }
	    		  ] ,
	    		  birthday: [
	    		    { required: true,type:'date', message: '请选择出生日期', trigger: 'change' }
	    		  ]  ,
	    		  sex: [
		    		    { required: true, message: '请选择性别', trigger: 'change' }
		    	  ], 
		    	  identityCard: [
		    		    { required: true, message: '请填写身份证号', trigger: 'change' }
		    	  ],
		    	  country: [
		    		    { required: true, message: '请填写国籍', trigger: 'change' }
		    	  ],
		    	  nation: [
		    		    { required: true, message: '请选择民族', trigger: 'change' }
		    	  ],
		    	  byyx: [
		    		    { required: true, message: '请选择毕业院校', trigger: 'change' }
		    	  ],
		    	  mobilePhone: [
		    		    { required: true, message: '请填写手机号', trigger: 'change' }
		    	  ],
		    	  email: [
		    		    { required: true, message: '请填写邮箱', trigger: 'change' }
		    	  ]
	    		  
	      } 
	    
	  },
	  created: function () {
		  axios.all([
	    	    axios.get('<%=ctxPath %>/htgl/dicinfocontroller/getdicinfo/api', {
	    	    	params : { 
	    	    		category : 'gjmc'
	    	    	}
	    	    }),
	    	    axios.get('<%=ctxPath %>/htgl/dicinfocontroller/getdicinfo/api', {
	    	    	params : { 
	    	    		category : 'mz'
	    	    	}
	    	    }),
	    	    axios.get('<%=ctxPath %>/htgl/dicinfocontroller/getdicinfo/api', {
	    	    	params : { 
	    	    		category : 'yx'
	    	    	}
	    	    })
	    	  ]).then(axios.spread(function (dicinfoResp,mzResp,yxResp) {
	    		  myVue.countryParm = dicinfoResp.data.data;
	    		  myVue.mzParm = mzResp.data.data;
	    		  myVue.byyxParm = yxResp.data.data;
	    	  }));
	 },
	  methods:{
		   handleSubmit:function (name) {
			   myVue.$refs[name].validate((valid) => {
				   if (valid){
					   let param = new URLSearchParams(); 
		          	  	param.append("id",myVue.userInfo.id); 
		          	  	param.append("userName",myVue.userInfo.userName); 
		          	  	param.append("realName",myVue.userInfo.realName); 
		          	  	param.append("sex",myVue.userInfo.sex); 
		          	  	param.append("identityCard",myVue.userInfo.identityCard); 
		          	  	param.append("birthdayString",myVue.userInfo.birthday); 
		          	  	param.append("country",myVue.userInfo.country); 
		          	  	param.append("nation",myVue.userInfo.nation); 
		          	  	param.append("byyx",myVue.userInfo.byyx); 
		          	  	param.append("mobilePhone",myVue.userInfo.mobilePhone); 
		          	  	param.append("email",myVue.userInfo.email); 
		          	  	debugger;
		          	  	axios.post('<%=ctxPath%>/htgl/userinfocontroller/updateselfinfo/api', param)
		          		  	 .then(function (response) {
		          			  	 if(response.data.flag){
		          			  		myVue.$Message.success({
		                                content: "修改成功",
		                                duration: 3,
		                                closable: true
		                            });
		          			  	    window.location.href='<%=ctxPath%>/htgl/userinfocontroller/toselfinfo';
		                        }else{
		                      	  myVue.$Message.warning({
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