<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
String ctxPath = request.getContextPath();
%>

<style>
li {list-style-type:none;}
</style>

<div id="app_content" >
    <Layout :style="{padding: '0 20px 20px'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item><a href="<%=ctxPath%>/"><Icon type="md-home"></Icon></a></breadcrumb-item>
            <breadcrumb-item>授权管理</breadcrumb-item> 
            <breadcrumb-item>授权信息</breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', background: '#fff'}">
          <i-form inline method="post" action="" >
        	
        		<form-item prop="userId">
        			<i-select name="userId" clearable v-model="formParm.userId" style="width: 150px;" placeholder="请选择用户">
                        	<i-option v-bind:value="item.id" v-for="item in userParm">
                        		{{item.userName}}
                        	</i-option>
                	</i-select>
                </form-item>
                
                <form-item prop="functionId">
        			<i-select name="functionId" clearable v-model="formParm.functionId" style="width: 150px;" placeholder="请选择功能">
                        	<i-option v-bind:value="item.id" v-for="item in functionParm">
                        		{{item.name}}
                        	</i-option>
                	</i-select>
                </form-item>
                
                <form-item>
                    	<i-button type="primary" @click="search()" > 查   询 </i-button>
                </form-item>
                	
                <form-item>
                    	<i-button type="primary" to="/htgl/authoritycontroller/toadd">新增授权</i-button> 
                </form-item>
                
        	</i-form>
        	
        	
        	<i-table border :columns="columns1" :data="tableDatas"></i-table> 
        	<Page :total="pageParm.totalCount" :current="pageParm.currentPage" :page-size="pageParm.pageCount" @on-change="searchPage" @on-page-size-change="changePageSize" show-total show-sizer show-elevator/>
        </i-content>
        
    </Layout>
</div>


<script>
var myVue = new Vue({
    el: '#app_content',
    data:{
    	formParm:{
	        userId:'',
	        functionId:''
	  	},
	  	userParm:[] ,
    	functionParm:[] ,
	  	pageParm:{
	  		currentPage:1,
	        pageCount:20,
	        totalCount:0
	  	},
    	tableDatas:[],
    	columns1:[
    		{
                title: '序号',
                type:"index",
                width: 70
            },
            {
                title: '用户名',
                key: 'userName',
                minWidth: 100,
                sortable:true
            },
            {
                title: '功能名称',
                key: 'functionName',
                minWidth: 100
            },
            {
                title: '用户真实姓名',
                key: 'realName',
                minWidth: 200
            },
            {
                title: '操作',
                key: 'action',
                width: 150,
                align: 'center',
                render: (h, params) => {
                	return h('div', [
                        h('i-button', {
                            props: {
                                type: 'error',
                                size: 'small'
                            },
                            on: {
                                click: () => {
                                	  myVue.remove(params.index)
                                }
                            }
                        }, '删除')
                    ]);
                }
            }
        ]
    },
    created: function () {
    	axios.all([
    	    axios.get('<%=ctxPath %>/htgl/authoritycontroller/getauthoritypage/api'),
    	    axios.get('<%=ctxPath %>/htgl/userinfocontroller/getusers/api'),
    	    axios.get('<%=ctxPath %>/htgl/functioninfocontroller/getchildfunctions/api')
    	  ]).then(axios.spread(function (authorityResp,userinfoResp,functionResp) {
    		  myVue.tableDatas = authorityResp.data.data.result;
    		  flushPage(authorityResp.data.data);
    		  myVue.userParm = userinfoResp.data.data;
    		  myVue.functionParm = functionResp.data.data;
    		  
    	  }));
    },
    methods: {
       remove: function (index) {
    	   myVue.$Modal.confirm({
   	        title: '提示',
   	        content: '您确定要删除么？',
   	        onOk:function(){
   	        	let param = new URLSearchParams(); 
   	      	    param.append("id",myVue.tableDatas[index].id); 
   	       	    axios.post('<%=ctxPath %>/htgl/authoritycontroller/delauthority/api', param)
   	      		  .then(function (response) {
   	      			  if(response.data.flag){
   	      				  myVue.$Message.success({
   	                          content: "删除成功",
   	                          duration: 3,
   	                          closable: true
   	                      });
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
   	        onCancel:function(){
   	        }
   	     });
       },
       //查询按钮
       search:function () {
    	  let param = new URLSearchParams(); 
    	  param.append("userId",myVue.formParm.userId); 
    	  param.append("functionId",myVue.formParm.functionId);
    	  param.append("pageSize",myVue.pageParm.pageCount); 
   	   	  param.append("currentPage",1); 
    	  axios.post('<%=ctxPath %>/htgl/authoritycontroller/getauthoritypage/api', param)
    		  .then(function (response) {
    			  if(response.data.flag){
    				  myVue.tableDatas = response.data.data.result;
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
      //分页查询
      searchPage:function (page) {
    	  let param = new URLSearchParams(); 
    	  param.append("userId",myVue.formParm.userId); 
    	  param.append("functionId",myVue.formParm.functionId); 
    	  param.append("pageSize",myVue.pageParm.pageCount); 
    	  param.append("currentPage",page); 
    	  axios.post('<%=ctxPath %>/htgl/authoritycontroller/getauthoritypage/api', param)
    		  .then(function (response) {
    			  if(response.data.flag){
    				  myVue.tableDatas = response.data.data.result;
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
      changePageSize:function(pageSize){
    	  let param = new URLSearchParams(); 
    	  param.append("userId",myVue.formParm.userId); 
    	  param.append("functionId",myVue.formParm.functionId); 
    	  param.append("pageSize",pageSize); 
    	  param.append("currentPage",1); 
    	  axios.post('<%=ctxPath %>/htgl/authoritycontroller/getauthoritypage/api', param)
    		  .then(function (response) {
    			  if(response.data.flag){
    				  myVue.tableDatas = response.data.data.result;
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
      }
      
    }
});
//刷新分页信息
function flushPage(page){
	myVue.pageParm.totalCount = page.totalCount;
	myVue.pageParm.pageCount = page.pageCount;
	myVue.pageParm.currentPage = page.currentPage;
};
</script>
