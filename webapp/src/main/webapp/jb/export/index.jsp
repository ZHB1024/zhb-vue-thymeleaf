<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
    String ctxPath = request.getContextPath();
%>
<div id="app_content" v-cloak style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item>加班管理</breadcrumb-item> 
        </Breadcrumb> 
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
        	<i-form inline action="<%=ctxPath %>/jb/export/exportorgworkday/api" method="post" :rules="ruleValidate" ref="formValidate" :model="formParm" >
                
        		<form-item prop="organizationId">
        			<i-select name="organizationId" v-model="formParm.organizationId" style="width: 150px;" placeholder="请选择部门">
                        	<i-option v-bind:value="org.id" v-for="org in orgsParm">
                        		{{org.name}}
                        	</i-option>
                	</i-select>
                </form-item>
                
                <form-item prop="workdate">
                  		<Date-picker  type="daterange"  name="workdate"  v-model="formParm.workdate"  format="yyyy-MM-dd"  placeholder="请选择日期段" style="width: 200px">
                    	</Date-picker>  
                </form-item >
                
                <form-item>
                    	<i-button type="primary" @click="querySubmit('formValidate')" > 查   询 </i-button>
                </form-item>
                <form-item>
                    	<i-button type="primary" @click="exportSubmit('formValidate')" ><Icon type="ios-download-outline"></Icon> 导   出 </i-button>
                </form-item>
                
        	</i-form>
        
        	<i-table border :columns="columns1" :data="tableDatas" :no-data-text="message" class="expand-table"></i-table> 
        </i-content> 
    </Layout>
</div>
<script type="x-template" id="detail">
<div>
  <Row class="expand-row" v-for="(item, index) in row.recordDetail">
    <i-col span="24">
      <span class="expand-value" >
		<div>{{item}}</div>
	  </span>
    </i-col>
  </Row>
</div>
</script>
<script>
Vue.component('newRow', {
	  props: ['row'],
	  template: '#detail'
	});
var myVue = new Vue({
    el: '#app_content',
    data:{
    	message:'请先选择查询条件，再进行查询',
    	tableDatas:[],
    	formParm:{
    		workdate:'',
	        organizationId:''
	  	},
	  	empParm:[] ,
	  	orgsParm:[] ,
    	columns1:[
    		{
                type: 'expand',
                width: 50,
                render: (h, params) => {
                  return h("new-row", {
                    props: {
                      row: params.row
                    }
                  })
                }
             },
             {
                title: '序号',
                type:"index",
                width: 70
            },
            {
                title: '姓名',
                key: 'userName',
                minWidth: 100
            },
            {
                title: '部门',
                key: 'orgName',
                minWidth: 100
            },
            {
                title: '加班时长（小时）',
                key: 'hours',
                minWidth: 100
            }/* ,
            {
                title: '加班明细',
                key: 'recordDetail'
            } */,
            {
                title: '餐补次数',
                key: 'sysmeans',
                minWidth: 100
            },
            {
                title: '审核状态',
                key: 'statusName',
                minWidth: 100
            }
        ],
        ruleValidate: {
  			 /* workdate: [
  		    { required: true,type:'date', message: '请选择日期段', trigger: 'change' }
  		  	]  ,
  			organizationId: [
  		    { required: true, message: '请选择部门', trigger: 'change' }
  		  	] */
    	} 
    },
    created: function () {
    	axios.all([
    	    <%-- axios.get('<%=ctxPath %>/jb/export/getorgworkday/api'), --%>
    	    axios.get('<%=ctxPath %>/jb/export/searchorganizations/api')
    	  ]).then(axios.spread(function (orgResp) {
    		  /* myVue.tableDatas = workRecordResp.data.data; */
    		  myVue.orgsParm = orgResp.data.data;
    	  }));
    },
    methods: {
      querySubmit:function (name) {
    	  if(!myVue.formParm.organizationId){
    		  myVue.$Message.warning({
                  content: '请选择部门',
                  duration: 3,
                  closable: true
              });
    		  return;
    	  }
    	  
    	  if(!myVue.formParm.workdate[0]){
    		  myVue.$Message.warning({
                  content: '请选择日期段',
                  duration: 3,
                  closable: true
              });
    		  return;
    	  }
    	  let param = new URLSearchParams(); 
    	  param.append("organizationId",myVue.formParm.organizationId); 
    	  param.append("workdate",myVue.formParm.workdate); 
    	  console.log(myVue.formParm.status);
    	  axios.post('<%=ctxPath %>/jb/export/getorgworkday/api', param)
    		  .then(function (response) {
    			  if(response.data.flag){
    				  if(response.data.data.length>0){
    					  myVue.tableDatas=response.data.data;
        				  myVue.$forceUpdate();
        				  return ;
    				  }
    				  myVue.tableDatas=response.data.data;
    				  myVue.message = "暂无数据";
                  }else{
                	  myVue.$Message.warning({
                          content: response.data.errorMessages,
                          duration: 3,
                          closable: true
                      });
                  }
    		  })
      },
      exportSubmit:function (name) {
    	  if(!myVue.formParm.organizationId){
    		  myVue.$Message.warning({
                  content: '请选择部门',
                  duration: 3,
                  closable: true
              });
    		  return;
    	  }
    	  
    	  if(!myVue.formParm.workdate[0]){
    		  myVue.$Message.warning({
                  content: '请选择日期段',
                  duration: 3,
                  closable: true
              });
    		  return;
    	  }
    	  let param = new URLSearchParams(); 
    	  param.append("organizationId",myVue.formParm.organizationId); 
    	  param.append("workdate",myVue.formParm.workdate); 
    	  console.log(myVue.formParm.status);
    	  axios.post('<%=ctxPath %>/jb/export/isreportdata/api', param)
    		  .then(function (response) {
    			  if(response.data.flag){
    				  if(response.data.data){//有未上报的数据
    					  myVue.$Modal.confirm({
                              title: '提示',
                              content: '此日期段内还有未上报的加班记录，您确定还要继续导出么？',
                              closable: true,
                              okText: '继续导出',
                              cancelText: '去上报',
                              onOk:function(){
                            	  myVue.$refs[name].$el.submit();
                              },
                              onCancel:function(){
                            	  window.location.href='<%=ctxPath%>/jb/mealallowance/index';
                              }
                          });
    				  }else{//没有待上报的数据
    					  myVue.$refs[name].$el.submit();
    				  }
    				  
                  }else{
                	  myVue.$Message.warning({
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
