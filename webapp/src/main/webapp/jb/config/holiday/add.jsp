<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
String ctxPath = request.getContextPath();
%>

<style>
li {list-style-type:none;}
</style>

<div id="app_content" style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item>加班配置</breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
          <div style="width:100%;">
            <div style="width: 600px;margin-left:  auto;margin-right:  auto;">
            	<i-form method="post" action="<%=ctxPath %>/jb/config/holiday/addholiday/api" ref="formValidate" :model="formParm" :rules="ruleValidate" :label-width="80">
            		
            		<form-item label="假期日期" prop="holidayDate">
                  		<Date-picker @on-change="selectTime" @on-clear="clearTime" name="holidayDate" type="daterange" :value="formParm.holidayDate"  format="yyyy-MM-dd"  placeholder="请选择假期日期" >
                    	</Date-picker>  
                	</form-item >
                	
                	<form-item label="假期" prop="content">
                    	<i-select name="content" v-model="formParm.content">
                        	<i-option v-bind:value="holiday.value" v-for="holiday in contentParm">
                        		{{holiday.value}}
                        	</i-option>
                    	</i-select>
                	</form-item>
                	
                	<form-item label="类型" prop="type">
                    	<i-select name="type" v-model="formParm.type">
                        	<i-option v-bind:value="holidayType.value" v-for="holidayType in typeParm">
                        		{{holidayType.key}}
                        	</i-option>
                    	</i-select>
                	</form-item>
                	
                	<form-item label="倍数" prop="multiple">
                  		<Input-number name="multiple" :max="10" :min="1" :step="1" v-model="formParm.multiple"></Input-number>
                	</form-item >
                  
                	<form-item>
                    	<i-button type="primary" @click="handleSubmit('formValidate')">确 定</i-button>
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
		  formParm:{
			    content:'',
			    type:'',
			    holidayDate:'',
			    multiple:1
		  },
		  contentParm:[] ,
		  typeParm:[],
	      ruleValidate: {
	    	      content: [
	    		    { required: true, message: '请选择假日', trigger: 'change' }
	    		  ] ,
	    		  type: [
	    		    { required: true, message: '请选择类型', trigger: 'change' }
	    	      ] ,   
	    	      multiple: [
	    		    { required: true, type:'number',message: '请输入倍数', trigger: 'change' }
	    		  ] ,
	    		  holidayDate: [
	    		    { required: true, type: 'array',message: '请选择假期日期' },
	    		  ]  
	      } 
	    
	  },
	  created: function () {
		  axios.all([
			  axios.get('<%=ctxPath %>/jb/config/holiday/getholidayparams/api'),
			  axios.get('<%=ctxPath %>/jb/config/holiday/getholidaytypes/api')
	    	  ]).then(axios.spread(function (contentResp,holidayTypeResp) {
	    		  myVue.contentParm = contentResp.data.data;
	    		  myVue.typeParm = holidayTypeResp.data.data;
	    	  }));
	 },
	  methods:{
		 handleSubmit: function (name) {
	            this.$refs[name].validate((valid) => {
	                if (valid) {
	                	let param = new URLSearchParams(); 
	                	param.append("content",myVue.formParm.content); 
	              	  	param.append("type",myVue.formParm.type); 
	              	  	param.append("multiple",myVue.formParm.multiple); 
	              	  	param.append("holidayDate",myVue.formParm.holidayDate); 
	              	  	axios.post('<%=ctxPath %>/jb/config/holiday/addholiday/api', param)
	              		  	.then(function (response) {
	              			  if(response.data.flag){
	              					window.location.href='<%=ctxPath%>/jb/config/holiday/index';
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
	        clearTime:function () {
	            myVue.formParm.holidayDate = [];
	        },
	        selectTime:function (event) {
	        	myVue.formParm.holidayDate = event;
	        }
		  
	  }
});
</script>