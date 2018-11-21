<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ page import="com.chsi.kaoqin.web.util.RemoteCallUtil"%>
<%
String ctxPath = request.getContextPath();
%>

<style>
li {list-style-type:none;}
</style>

<div id="app_content" style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item><a href="/">首页</a></breadcrumb-item> 
            <breadcrumb-item>记录管理</breadcrumb-item> 
            <breadcrumb-item><a href="<%=ctxPath%>/jb/worktime/toaddrecord">添加记录</a></breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
          <div style="width:100%;">
            <div style="width: 600px;margin-left:  auto;margin-right:  auto;">
            	<i-form method="post" action="<%=ctxPath %>/jb/worktime/addrecord" ref="formValidate" :model="formParm" :rules="ruleValidate" :label-width="80">
                	<form-item label="组织" prop="organizationId">
                    	<i-select name="organizationId" v-model="formParm.organizationId">
                        	<i-option v-bind:value="org.id" v-for="org in orgParm">
                        		{{org.name}}
                        	</i-option>
                    	</i-select>
                	</form-item>
                	
                	<form-item label="加班内容" prop="contentId">
                    	<i-select name="contentId" v-model="formParm.contentId">
                        	<i-option v-bind:value="optContent.id" v-for="optContent in contentParm">
                        		{{optContent.content}}
                        	</i-option>
                    	</i-select>
                	</form-item>
                	
                	<form-item label="加班详情" prop="detail">
                  		<i-input type="textarea" name="detail" v-model="formParm.detail" :maxlength="100" placeholder="请输入加班详情"></i-input>
                	</form-item >
                	
                	<form-item label="加班时间" prop="workdate">
                  		<Date-picker name="workDate" type="date" :options="limitDate" v-model="formParm.workdate"  format="yyyy-MM-dd" placeholder="加班日期"  >
                    	</Date-picker>  
                	</form-item >
                	
                  <form-item v-for="(item, index) in items" :key="index"  >
                    <Row>
                      <i-col span="8">
                        <time-picker confirm name="startTime" :value="item.start"  v-model="item.start" :steps="[1, 30]" format="HH:mm" :editable="false" placeholder="开始时间段" style="width: 150px"></time-picker>
                      </i-col>
                      <i-col span="1" >
                        至
                      </i-col>
                      <i-col span="8">
                        <Tooltip content="结束时间为24:00时，请选择00:00" placement="right-start">
                        	<time-picker confirm name="endTime" :value="item.end"  v-model="item.end" :steps="[1, 30]" format="HH:mm" :editable="false" placeholder="结束时间段" style="width: 150px"></time-picker>
            			</Tooltip>
                      </i-col>
                      <i-col span="2" offset="1">
                        <i-button @click="handleRemove(index)">删除</i-button>
                      </i-col>
                    </Row>
                  </form-item>
                  <form-item>
                    <Row>
                      <i-col span="4" offset="6">
                        <i-button type="dashed" @click="handleAdd" icon="i-button">添加时间段</i-button>
                      </i-col>
                    </Row>
                  </form-item>
                  
                  <form-item align="center">
                  </form-item>
                  
                  <form-item align="center">
                		<Row>
                      		<i-col span="4" offset="6">
                        		<i-button type="primary" @click="handleSubmit('formValidate')">确 定</i-button>
                      		</i-col>
                    	</Row>
                  </form-item>
                </i-form>
            </div>
          </div>
        </i-content>
        
    </Layout>
</div>


<script type="text/javascript">
var startTimeJson = '<%=request.getAttribute("startTime") %>' ;
var endTimeJson = '<%=request.getAttribute("endTime") %>' ;
var myVue =  new Vue({
	  el: '#app_content',
	  data:{
		  formParm:{
		        contentId:'',
		        detail:'',
		        workdate:'',
		        worktimes:'',
		        organizationId:'',
		  },
		  index: 0,
		  items: [
        	{
          		start: startTimeJson,
          		end: endTimeJson,
          		index: 0,
          		status: 1
        	}
      	  ],
		  contentParm:[] ,
		  orgParm:[],
		  limitDate:{
			  disabledDate (date) {
		             return date.valueOf() > Date.now() ;
		         }
		  },
	      ruleValidate: {
	    	      organizationId: [
	    		    { required: true, message: '请选择组织', trigger: 'change' }
	    	      ] ,   
	    	     contentId: [
	    		    { required: true, message: '请选择加班内容', trigger: 'change' }
	    		  ] ,
	    		  detail: [
	    		    { required: true, message: '请输入加班详情', trigger: 'change' }
	    		  ] ,
	    		  workdate: [
	    		    { required: true,type:'date', message: '请选择加班日期', trigger: 'change' }
	    		  ]  
	      } 
	    
	  },
	  methods:{
		   handleSubmit:function (name) {
	            this.$refs[name].validate((valid) => {
	                if (valid) {
	       			   myVue.$Spin.show();
	                	var flag = true;
	                   if(!$("input[name='startTime']").val()){
	                	    flag = false;
	                	}
	                	if(!$("input[name='endTime']").val()){
	                		flag = false;
	                	} 
	                	var startTimes=new Array()
	                	$("input[name='startTime']").each(function(j,item){
	                		if(!item.value){
	                			flag = false;
	                			myVue.$Spin.hide();
		                		return false;
	                		}
	                		startTimes[j] = item.value;
	                	  });
	                	var endTimes=new Array()
	                	$("input[name='endTime']").each(function(j,item){
	                		if(!item.value){
	                			flag = false;
	                			myVue.$Spin.hide();
		                		return false;
	                		}
	                		endTimes[j] = item.value;
	                	  });
	                	
	                	if(!flag){
	                		myVue.$Spin.hide();
	                		myVue.$Message.warning({
                                content: "请输入加班时间段",
                                duration: 3,
                                closable: true
                            });
	                		return false;
	                	}
	                	let param = new URLSearchParams(); 
	                	param.append("organizationId",myVue.formParm.organizationId); 
	              	  	param.append("contentId",myVue.formParm.contentId); 
	              	  	param.append("detail",myVue.formParm.detail); 
	              	  	param.append("workdate",myVue.formParm.workdate); 
	              	  	
	              	  	param.append("startTimes",startTimes); 
	              	  	param.append("endTimes",endTimes); 
	              	  	axios.post('<%=ctxPath %>/jb/worktime/addrecord/api', param)
	              		  	.then(function (response) {
	              		  	  myVue.$Spin.hide();
	              			  if(response.data.flag){
	              					window.location.href='<%=ctxPath%>/jb/worktime/indexreport';
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
		 //添加加班时间
		 handleAdd:function () {
			 this.index++;
	          if(0 == this.index){
	        	  this.items.push({
	        		  start: startTimeJson,
		          	  end: endTimeJson,
		              index: this.index,
		              status: 1
	             });
           }else{
        	  this.items.push({
          	  	  start: this.items[this.index-1].end,
          		  end: this.items[this.index-1].end,
                  index: this.index,
                  status: 1
            });
          }
        },
        //删除加班时间
        handleRemove:function (index) {
        	this.items.splice(index,1);
        	this.index--;
        }
	  },
	  created: function () {
		  axios.all([
	    	    axios.get('<%=ctxPath %>/jb/worktime/getorgcontents/api'),
	    	    axios.get('<%=ctxPath %>/jb/worktime/getorgs/api')
	    	  ]).then(axios.spread(function (contentResp,orgResp) {
	    		  myVue.contentParm = contentResp.data.data;
	    		  myVue.orgParm = orgResp.data.data;
	    		  myVue.formParm.organizationId = myVue.orgParm[0].id;
	    	  }));
	 }
});
</script>