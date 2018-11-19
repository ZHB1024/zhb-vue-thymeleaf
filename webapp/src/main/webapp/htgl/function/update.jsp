<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
String ctxPath = request.getContextPath();
%>

<div id="app_content" style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item><a href="">首页</a></breadcrumb-item> 
            <breadcrumb-item>功能管理</breadcrumb-item> 
            <breadcrumb-item>修改功能</breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
          <div style="width:100%;">
            <div style="width: 600px;margin-left:  auto;margin-right:  auto;">
            	<i-form method="post" action="" ref="formValidate" :model="functionInfo" :rules="ruleValidate" :label-width="80">
                	<input type="hidden" name="id" v-model="functionInfo.id"/>
                	
                	
                	<form-item label="父节点" prop="parentId">
                	  <i-select name="parentId" v-model="functionInfo.parentId" @on-change="getMaxOrder" placeholder="请选择父节点">
                        	<i-option v-bind:value="item.id" v-for="item in parentFunctions">
                        		{{item.name}}
                        	</i-option>
                    	</i-select>
                	</form-item>
                	
                	<form-item label="功能名称" prop="name">
                  		<i-input type="text" name="name" v-model="functionInfo.name" :maxlength="15" placeholder="请输入功能名称"></i-input>
                	</form-item >
                	
                	<form-item label="访问路径" prop="path">
                		   <i-input type="text" name="path" v-model="functionInfo.path" :maxlength="50" placeholder="请输入访问路径"></i-input>
                	</form-item >
                	
                	<form-item label="功能图标" prop="iconId">
                	  <i-select name="iconId" clearable v-model="functionInfo.iconId" placeholder="请选择功能图标">
                        	<i-option v-bind:value="item.id" v-for="item in icons">
                        		{{item.name}}
                        	</i-option>
                    	</i-select>
                	</form-item>
                	
                	<form-item label="排序号" prop="order">
                  		<Input-number disabled name="order" v-model="functionInfo.order"></Input-number>
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
var functionInfoJson = <%=request.getAttribute("functionInfoJson") %> ;
var myVue =  new Vue({
	  el: '#app_content',
	  data:{
		  functionInfo:{
			  id:functionInfoJson.id,
			  name:functionInfoJson.name,
			  path:functionInfoJson.path,
			  iconId:functionInfoJson.iconId,
			  order:functionInfoJson.order,
			  parentId:functionInfoJson.parentId
		  },
		  parentFunctions:[],
		  icons:[],
	      ruleValidate: {
	    	     name: [
	    		    { required: true, message: '请填写功能名称', trigger: 'change' }
	    		  ] ,
	    		  path: [
		    		    { required: true, message: '请填写访问路径', trigger: 'change' }
		    	  ],
		    	  order: [
		    		    { required: true, type:'number', message: '请填写顺序号', trigger: 'change' }
		    	  ]
	    		  
	      } 
	    
	  },
	  created: function () {
		  axios.all([
	    	    axios.get('<%=ctxPath %>/htgl/functioninfocontroller/getparentfunctions/api'),
	    	    axios.get('<%=ctxPath %>/htgl/iconinfocontroller/geticoninfo/api')
	    	  ]).then(axios.spread(function (funinfoResp,iconResp) {
	    		  myVue.parentFunctions = funinfoResp.data.data;
	    		  myVue.icons = iconResp.data.data;
	    	  }));
	 },
	  methods:{
		   handleSubmit:function (name) {
			   myVue.$refs[name].validate((valid) => {
				   if (valid){
					    let param = new URLSearchParams(); 
		          	  	param.append("id",myVue.functionInfo.id); 
		          	  	param.append("parentId",myVue.functionInfo.parentId); 
		          	  	param.append("name",myVue.functionInfo.name); 
		          	  	param.append("path",myVue.functionInfo.path); 
		          	  	param.append("iconId",myVue.functionInfo.iconId); 
		          	  	param.append("order",myVue.functionInfo.order); 
		          	  	axios.post('<%=ctxPath%>/htgl/functioninfocontroller/updatefunctioninfo/api', param)
		          		  	 .then(function (response) {
		          			  	 if(response.data.flag){
		          			  		myVue.$Message.success({
		                                content: "修改成功",
		                                duration: 3,
		                                closable: true
		                            });
		          			  	    window.location.href='<%=ctxPath%>/htgl/functioninfocontroller/toindex';
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
	        },
	        getMaxOrder:function(parentId){
	        	let param = new URLSearchParams(); 
	       	  	param.append("parentId",parentId); 
	       	  	axios.post('<%=ctxPath %>/htgl/functioninfocontroller/getmaxorder/api', param)
	       		  	.then(function (response) {
	       			  	if(response.data.flag){
	       			  		myVue.functionInfo.order=response.data.data;
	       				  	myVue.$forceUpdate();
	                     }else{
	                   	  myVue.$Message.info({
	                             content: response.data.errorMessages,
	                             duration: 3,
	                             closable: true
	                         });
	                     }
	       		  })
	        }
	  }
});
</script>