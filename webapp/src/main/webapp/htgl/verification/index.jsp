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
            <breadcrumb-item>验证码管理</breadcrumb-item> 
            <breadcrumb-item>验证码信息</breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', background: '#fff'}">
          <i-form inline method="post" action="" ref="formValidate">
        	
                <form-item prop="email">
                  		<i-input type="text" name="email" clearable  v-model="formParm.email" :maxlength="50" placeholder="请输入邮箱"></i-input>
                </form-item >
                
                <form-item prop="code">
                  		<i-input type="text" name="code" clearable  v-model="formParm.code" :maxlength="10" placeholder="请输入验证码"></i-input>
                </form-item >
                	
        		<form-item prop="type">
        			<i-select name="type" clearable v-model="formParm.type" style="width: 150px;" placeholder="请选择类别">
                        	<i-option v-bind:value="item.value" :key="item.value" v-for="item in typeParm">
                        		{{item.key}}
                        	</i-option>
                	</i-select>
                </form-item>
                
                <form-item>
                    	<i-button type="primary" @click="search()" > 查   询 </i-button>
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
    	typeParm:[] ,
    	formParm:{
    		email:'',
	        code:'',
	        type:''
	  	},
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
                title: '邮箱',
                key: 'email',
                minWidth: 100,
                sortable:true
            },
            {
                title: '电话',
                key: 'mobilePhone',
                minWidth: 100
            },
            {
                title: '验证码',
                key: 'code',
                minWidth: 100
            },
            {
                title: '类别',
                key: 'type',
                minWidth: 100
            },{
                title: '描述',
                key: 'remark',
                minWidth: 100
            },
            {
                title: '创建时间',
                key: 'createTime',
                minWidth: 100
            },
            {
                title: '修改时间',
                key: 'updateTime',
                minWidth: 100
            },
            {
                title: '状态',
                key: 'deleteFlagName',
                minWidth: 100
            }
        ]
    },
    created: function () {
    	axios.all([
    	    axios.get('<%=ctxPath %>/htgl/verificationcodeinfocontroller/getverificationcodeinfopage/api'),
    	    axios.get('<%=ctxPath %>/htgl/verificationcodeinfocontroller/getverificationcodetype/api')
    	  ]).then(axios.spread(function (verificationcodeinfoResp,typeResp) {
    		  myVue.tableDatas = verificationcodeinfoResp.data.data.result;
    		  flushPage(verificationcodeinfoResp.data.data);
    		  myVue.typeParm = typeResp.data.data;
    	  }));
    },
    methods: {
       //查询按钮
       search:function () {
    	  let param = new URLSearchParams(); 
    	  param.append("email",myVue.formParm.email); 
    	  param.append("code",myVue.formParm.code); 
    	  if(undefined == myVue.formParm.type){
    		  myVue.formParm.type = "";
    	  }
    	  param.append("type",myVue.formParm.type); 
    	  param.append("pageSize",myVue.pageParm.pageCount); 
   	   	  param.append("currentPage",1); 
    	  axios.post('<%=ctxPath %>/htgl/verificationcodeinfocontroller/getverificationcodeinfopage/api', param)
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
      //点击页码查询
      searchPage:function (page) {
    	  let param = new URLSearchParams(); 
    	  param.append("email",myVue.formParm.email); 
    	  param.append("code",myVue.formParm.code); 
    	  param.append("type",myVue.formParm.type); 
    	  param.append("pageSize",myVue.pageParm.pageCount); 
    	  param.append("currentPage",page); 
    	  axios.post('<%=ctxPath %>/htgl/verificationcodeinfocontroller/getverificationcodeinfopage/api', param)
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
      //改变每页大小 
      changePageSize:function(pageSize){
    	  let param = new URLSearchParams(); 
    	  param.append("email",myVue.formParm.email); 
    	  param.append("code",myVue.formParm.code); 
    	  param.append("type",myVue.formParm.type); 
    	  param.append("pageSize",pageSize); 
    	  param.append("currentPage",1); 
    	  axios.post('<%=ctxPath %>/htgl/verificationcodeinfocontroller/getverificationcodeinfopage/api', param)
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
