<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
String ctxPath = request.getContextPath();
%>
<div id="app_content" style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item>加班配置</breadcrumb-item> 
        </Breadcrumb> 
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
        	<i-form inline method="post" action="" ref="formValidate">
        	
        		<form-item prop="year">
                  		<Date-picker  type="year"  name="year"  v-model="formParm.year"  format="yyyy"  placeholder="年份" style="width: 200px">
                    	</Date-picker>  
                </form-item >
        	    
        		<form-item prop="content">
                    	<i-select name="content" v-model="formParm.content" style="width: 150px;" placeholder="假期">
                    		<i-option value="" >
                        		全部
                        	</i-option>
                        	<i-option v-bind:value="holiday.value" v-for="holiday in contentParm">
                        		{{holiday.value}}
                        	</i-option>
                    	</i-select>
                	</form-item>
                	
                	<form-item prop="type">
                    	<i-select name="type" v-model="formParm.type" style="width: 150px;" placeholder="类型">
                    		<i-option value="" >
                        		全部
                        	</i-option>
                        	<i-option v-bind:value="holidayType.value" v-for="holidayType in typeParm">
                        		{{holidayType.key}}
                        	</i-option>
                    	</i-select>
                	</form-item>
                
                <form-item>
                    	<i-button type="primary" @click="search()" > 查   询 </i-button>
                </form-item>
                
                <form-item>
                    	<i-button type="primary" to="/jb/config/holiday/toaddholiday">新增假日</i-button> 
                	</form-item>
                
        	</i-form>
        	
        	
        	<i-table border :columns="columns1" :data="tableDatas"></i-table> 
        	<div style="margin-top:6px;">
        		<Page :total="pageParm.totalCount" :current="pageParm.currentPage" :page-size="pageParm.pageCount" @on-change="searchPage" @on-page-size-change="changePageSize" show-total show-sizer show-elevator/>
        	</div>
        </i-content> 
    </Layout>
</div>
<script>
var myVue = new Vue({
    el: '#app_content',
    data:{
    	contentParm:[] ,
		typeParm:[],
    	formParm:{
	        year:'',
	        content:'',
	        multiple:'',
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
                title: '假日名称',
                key: 'content'
            },
            {
                title: '假日年份',
                key: 'year'
            },
            {
                title: '假日',
                key: 'holidayDate'
            },
            {
                title: '加班倍数',
                key: 'multiple'
            },
            /* {
                title: '假期类型',
                key: 'type'
            } , */
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
                                size: 'small',
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
    	    axios.get('<%=ctxPath %>/jb/config/holiday/getholidays/api'),
    	    axios.get('<%=ctxPath %>/jb/config/holiday/getholidayparams/api'),
			axios.get('<%=ctxPath %>/jb/config/holiday/getholidaytypes/api')
    	  ]).then(axios.spread(function (holidaysResp,contentResp,holidayTypeResp) {
    		  myVue.tableDatas = holidaysResp.data.data.result;
    		  flushPage(holidaysResp.data.data);
    		  myVue.contentParm = contentResp.data.data;
    		  myVue.typeParm = holidayTypeResp.data.data;
    	  }));
    },
    methods: {
    	//删除
      remove: function (index) {
        this.$Modal.confirm({
            title: '提示',
            content: '您确定要删除么？',
            onOk:function(){
            	let param = new URLSearchParams(); 
          	  	param.append("id",myVue.tableDatas[index].id); 
          	    param.append("pageSize",myVue.pageParm.pageCount); 
         	    param.append("currentPage",myVue.pageParm.currentPage); 
          	  	axios.post('<%=ctxPath %>/jb/config/holiday/delholiday/api', param)
          		  .then(function (response) {
          			  if(response.data.flag){
          				  myVue.$Message.success({
                            content: '删除成功',
                            duration: 3,
                            closable: true
                          });
          				  myVue.tableDatas=response.data.data.result;
          				  flushPage(response.data.data);
          				  myVue.$forceUpdate();
                        }else{
                      	  myVue.$Message.warning({
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
      //查询
      search:function () {
    	  let param = new URLSearchParams(); 
    	  param.append("content",myVue.formParm.content); 
    	  param.append("type",myVue.formParm.type); 
    	  param.append("year",myVue.formParm.year); 
    	  param.append("pageSize",myVue.pageParm.pageCount); 
     	  param.append("currentPage",1); 
    	  axios.post('<%=ctxPath %>/jb/config/holiday/getholidays/api', param)
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
       //分页查询
      searchPage:function (page) {
    	  let param = new URLSearchParams(); 
    	  param.append("content",myVue.formParm.content); 
    	  param.append("type",myVue.formParm.type); 
    	  param.append("year",myVue.formParm.year); 
    	  param.append("pageSize",myVue.pageParm.pageCount); 
     	  param.append("currentPage",page); 
    	  axios.post('<%=ctxPath %>/jb/config/holiday/getholidays/api', param)
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
      //改变页面数量
      changePageSize:function(pageSize){
    	  let param = new URLSearchParams(); 
    	  param.append("content",myVue.formParm.content); 
    	  param.append("type",myVue.formParm.type); 
    	  param.append("year",myVue.formParm.year); 
    	  param.append("pageSize",pageSize); 
     	  param.append("currentPage",1); 
    	  axios.post('<%=ctxPath %>/jb/config/holiday/getholidays/api', param)
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
