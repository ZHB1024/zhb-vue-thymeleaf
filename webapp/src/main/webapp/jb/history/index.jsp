<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<%
    String ctxPath = request.getContextPath();
%>
<div id="app_content" v-cloak>
    <Layout :style="{padding: '0 24px 24px', height: '100%'}">
        <Breadcrumb :style="{margin: '24px 0'}">
            <breadcrumb-item>加班管理</breadcrumb-item>
        </Breadcrumb>
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
            <i-form inline method="post">
                
                <form-item prop="workdate">
                  		<Date-picker  type="daterange"  name="workdate"  v-model="formParm.workdate"  format="yyyy-MM-dd"  placeholder="请选择日期段" style="width: 200px">
                    	</Date-picker>  
                </form-item >
        		
        		<form-item prop="organizationId">
        			<i-select name="organizationId" v-if="orgsParm.length == 1 " v-model="formParm.organizationId" @on-change="getEmployees" style="width: 150px;" placeholder="请选择组织">
                        	<i-option v-bind:value="org.id" v-for="org in orgsParm">
                        		{{org.name}}
                        	</i-option>
                	</i-select>
                	
        			<i-select name="organizationId" v-if="orgsParm.length > 1 " v-model="formParm.organizationId" @on-change="getEmployees" style="width: 150px;" placeholder="请选择组织">
        					<i-option value="" >
                        		全部
                        	</i-option>
                        	<i-option v-bind:value="org.id" v-for="org in orgsParm">
                        		{{org.name}}
                        	</i-option>
                	</i-select>
                </form-item>
                
                <form-item prop="creator">
        			<i-select name="creator" v-model="formParm.creator" style="width: 150px;" placeholder="请选择人员">
        					<i-option value="" >
                        		全部
                        	</i-option>
                        	<i-option v-bind:value="user.id" v-for="user in empParm" :key="user.id">
                        		{{user.name}}
                        	</i-option>
                	</i-select>
                </form-item>
                
                <form-item prop="status">
        			<i-select name="status" v-model="formParm.status" style="width: 150px;" placeholder="请选择状态">
                        	<i-option v-bind:value="stat.index" v-for="stat in statusParm" :key="stat.index">
                        		{{stat.name}}
                        	</i-option>
                	</i-select>
                </form-item>
                
                <form-item>
                    	<i-button type="primary" @click="search()" > 查   询 </i-button>
                </form-item>
                
            </i-form>
            
            <i-table border :columns="columns10" :data="tableDatas" class="expand-table"></i-table> 
            <div style="margin-top:6px;">
        		<Page :total="pageParm.totalCount" :current="pageParm.currentPage" :page-size="pageParm.pageCount" @on-change="searchPage" @on-page-size-change="changePageSize" show-total show-sizer show-elevator/>
        	</div>
        	
        </i-content>
    </Layout>
</div>

<script type="x-template" id="demo4">
<div>
  <Row v-for="record in row.records" class="expand-row">
    <i-col span="6">
      <span class="expand-key">时间: </span>
      <span class="expand-value">{{ record.times }}</span>
    </i-col>
    <i-col span="2">
      <span class="expand-key">时长: </span>
      <span class="expand-value">{{ record.hours }}</span>
    </i-col>
    <i-col span="4">
      <span class="expand-key">内容: </span>
      <span class="expand-value">{{ record.content }}</span>
    </i-col>
    <i-col span="12">
      <span class="expand-key">详情: </span>
      <span class="expand-value">{{ record.detail }}</span>
    </i-col>
  </Row>
  <Row v-if="row.pmeals!='' || row.smeals!=''" class="expand-row">
    <i-col v-if="row.smeals!=''" span="2">
      <span class="expand-key">系统计算: </span>
      <span class="expand-value">{{ row.smeals }}</span>
    </i-col>
    <i-col v-if="row.pmeals!=''" span="2">
      <span class="expand-key">建议: </span>
      <span class="expand-value">{{ row.pmeals }}</span>
    </i-col>
  </Row>
</div>
</script>
<script>
Vue.component('newRow', {
  props: ['row'],
  template: '#demo4'
});

myVue = new Vue({
    el: '#app_content',
    data:{
    	formParm:{
            creator:'',
            organizationId:'',
            workdate:'',
            status:''
        },
    	columns10: [
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
                key: 'realname'
            },
            {
                title: '组织',
                key: 'organization'
            },
            {
                title: '加班日期',
                key: 'workdate',
                sortable:true
            },
            {
                title: '工作小时数',
                key: 'workhours'
            },
            {
                title: '审核状态',
                key: 'status'
            }
        ],
        empParm:[],
        orgsParm:[],
        tableDatas: [],
	  	pageParm:{
	  		currentPage:1,
	        pageCount:20,
	        totalCount:0
	  	}
    },
    created: function () {
    	axios.all([
            axios.get('<%=ctxPath %>/jb/history/searchhistorys/api'),
            axios.get('<%=ctxPath %>/jb/history/searchemployees/api'),
            axios.get('<%=ctxPath %>/jb/history/searchorganizations/api'),
            axios.get('<%=ctxPath %>/jb/history/getstatus/api')
          ]).then(axios.spread(function (historyResp,empResp,orgResp,statusResp) {
        	  myVue.tableDatas = historyResp.data.data.result;
        	  flushPage(historyResp.data.data);
              myVue.empParm = empResp.data.data;
              myVue.orgsParm = orgResp.data.data;
              if(orgResp.data.data.length==1){
    			  myVue.formParm.organizationId = orgResp.data.data[0].id;
    		  }
              myVue.statusParm = statusResp.data.data;
    		  myVue.formParm.status = '3';
          }));
    },
    methods:{
    	//按条件查询
        search:function () {
      	  let param = new URLSearchParams(); 
      	  param.append("creator",myVue.formParm.creator); 
      	  param.append("organizationId",myVue.formParm.organizationId); 
      	  param.append("workdate",myVue.formParm.workdate); 
      	  param.append("status",myVue.formParm.status); 
      	  param.append("pageSize",myVue.pageParm.pageCount); 
       	  param.append("currentPage",1); 
      	  axios.post('<%=ctxPath %>/jb/history/searchhistorys/api', param)
      		  .then(function (response) {
      			  if(response.data.flag){
      				  myVue.tableDatas=response.data.data.result;
      				  flushPage(response.data.data);
      				  myVue.$forceUpdate();
                    }else{
                  	  myVue.$Message.error({
                            content: response.data.errorMessages,
                            duration: 3,
                            closable: true
                        });
                    }
      		  })
        },
    	//分页查询，点击上一页、下一页、数字
        searchPage:function (page) {
           let param = new URLSearchParams(); 
           param.append("creator",myVue.formParm.creator); 
       	   param.append("organizationId",myVue.formParm.organizationId); 
       	   param.append("workdate",myVue.formParm.workdate); 
       	   param.append("status",myVue.formParm.status); 
           param.append("pageSize",myVue.pageParm.pageCount); 
           param.append("currentPage",page); 
           axios.post('<%=ctxPath %>/jb/history/searchhistorys/api', param)
               .then(function (response) {
                   if(response.data.flag){
                       myVue.tableDatas=response.data.data.result;
                       flushPage(response.data.data);
                       myVue.$forceUpdate();
                    }else{
                       myVue.$Message.info({
                            content: response.data.errorMessages,
                            duration: 3,
                            closable: true
                        });
                    }
               })
        },
        changePageSize:function(pageSize){
      	  let param = new URLSearchParams(); 
      	  param.append("creator",myVue.formParm.creator); 
      	  param.append("organizationId",myVue.formParm.organizationId); 
      	  param.append("workdate",myVue.formParm.workdate);
      	  param.append("status",myVue.formParm.status); 
      	  param.append("pageSize",pageSize); 
       	  param.append("currentPage",1); 
      	  axios.post('<%=ctxPath %>/jb/history/searchhistorys/api', param)
      		  .then(function (response) {
      			  if(response.data.flag){
      				  myVue.tableDatas=response.data.data.result;
       				  flushPage(response.data.data);
      				  myVue.$forceUpdate();
                    }else{
                  	  myVue.$Message.error({
                            content: response.data.errorMessages,
                            duration: 3,
                            closable: true
                        });
                    }
      		  })
        },
    	//组织 员工级联
        getEmployees:function(orgId){
            let param = new URLSearchParams(); 
            param.append("orgId",orgId); 
            axios.post('<%=ctxPath %>/jb/history/getempsbyorgid/api', param)
                .then(function (response) {
                    if(response.data.flag){
                        myVue.empParm=response.data.data;
                        myVue.formParm.creator='';
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

//刷新分页信息
function flushPage(page){
	myVue.pageParm.totalCount = page.totalCount;
	myVue.pageParm.pageCount = page.pageCount;
	myVue.pageParm.currentPage = page.currentPage;
}
</script>