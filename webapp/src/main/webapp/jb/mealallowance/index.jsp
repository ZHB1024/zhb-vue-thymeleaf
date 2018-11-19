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
        	<i-form inline method="post">
                <form-item prop="workdate">
                  		<Date-picker  type="daterange"  name="workdate"  v-model="formParm.workdate"  format="yyyy-MM-dd"  placeholder="请选择日期段" style="width: 200px">
                    	</Date-picker>  
                </form-item >
        		
        		<form-item prop="organizationId">
        			<i-select name="organizationId" v-model="formParm.organizationId" @on-change="getEmployees" style="width: 150px;" placeholder="请选择组织">
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
                <form-item>
                    	<i-button type="primary" @click="search()" > 查   询 </i-button>
                </form-item>
        	</i-form>
        
            <div style="margin-bottom:10px;">
        		<i-button @click="batchReportPass()" style="color: #fff;background-color: #2d8cf0;">全部上报</i-button>
        	</div>
        	<i-table border :columns="columns1" :data="tableDatas" class="expand-table"></i-table> 
        	<div style="margin-top: 10px;">
        		<i-button @click="batchReportPass()" style="color: #fff;background-color: #2d8cf0;">全部上报</i-button>
        	</div>
        	
        	<div style="margin-top:15px;">
        		<Page :total="pageParm.totalCount" :current="pageParm.currentPage" :page-size="pageParm.pageCount" @on-change="searchPage" @on-page-size-change="changePageSize" show-total show-sizer show-elevator/>
        	</div>
        	
        </i-content> 
    </Layout>
</div>
<script type="x-template" id="demo4">
<div>
  <Row v-for="record in row.worRecords" class="expand-row">
    <i-col span="6">
      <span class="expand-key">时间: </span>
      <span class="expand-value">{{ record.worktimes }}</span>
    </i-col>
    <i-col span="2">
      <span class="expand-key">时长: </span>
      <span class="expand-value">{{ record.workHours }}</span>
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
</div>
</script>
<script>
Vue.component('newRow', {
	  props: ['row'],
	  template: '#demo4'
	});

var myVue = new Vue({
    el: '#app_content',
    data:{
    	tableDatas:[],
    	formParm:{
	        creator:'',
	        organizationId:'',
	        workdate:'',
	        reportDateRange:''
	  	},
	  	pageParm:{
	  		currentPage:1,
	        pageCount:20,
	        totalCount:0
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
                key: 'creator',
                sortable: true,
                minWidth: 100
            },
            {
                title: '组织',
                key: 'organization',
                minWidth: 100
            } ,
            {
                title: '加班日期',
                key: 'workdate',
                minWidth: 100,
                sortable: true
            },
            {
                title: '加班时长（小时）',
                key: 'hours',
                width: 150,
                minWidth: 100
            },
            {
                title: '系统餐补次数',
                key: 'sysmeans',
                width: 150,
                minWidth: 100
            },
            {
                title: '建议餐补次数',
                key: 'porposalmeans',
                width: 150,
                minWidth: 100
            },
            {
                title: '修改建议次数',
                key: 'sysmeans',
                width: 150,
               /*  className: 'demo-table-info-column', */
                render: (h, params) => {
                	return h('div', [
                        h('Icon', {
                            props: {
                            	type: 'md-add-circle'
                            },
                            style: {
                                marginRight: '5px',
                                fontSize:'25px',
                                color: 'green'
                            },
                            on: {
                            	click: () => {
                                this.myVue.upPorposalMeans(params.index,1);
                              }
                            }
                        }),
                        h('Icon', {
                            props: {
                              type: 'md-remove-circle'
                            },
                            style: {
                                marginRight: '5px',
                                fontSize:'25px',
                                color: 'red'
                            },
                            on: {
                              click: () => {
                                this.myVue.upPorposalMeans(params.index,-1);
                              }
                            }
                        }),
                    ]);
                }
            } ,
            {
                title: '操作',
                key: 'action',
                width: 150,
                align: 'center',
                render: (h, params) => {
                	return h('div', [
                        h('i-button', {
                            props: {
                                type: 'primary',
                                size: 'small'
                            },
                            style: {
                                marginRight: '5px'
                            },
                            on: {
                                click: () => {
                                	myVue.report(params.index)
                                }
                            }
                        }, '上报')
                    ]);
                }
            } 
        ]
    },
    created: function () {
    	axios.all([
    	    axios.get('<%=ctxPath %>/jb/mealallowance/searchworkday/api'),
    	    axios.get('<%=ctxPath %>/jb/mealallowance/searchemployees/api'),
    	    axios.get('<%=ctxPath %>/jb/mealallowance/searchorganizations/api')
    	  ]).then(axios.spread(function (workRecordResp,empResp,orgResp) {
    		  myVue.tableDatas = workRecordResp.data.data.result;
    		  flushPage(workRecordResp.data.data);
    		  myVue.empParm = empResp.data.data;
    		  myVue.orgsParm = orgResp.data.data;
    	  }));
    },
    methods: {
      //按条件查询
      search:function () {
    	  let param = new URLSearchParams(); 
    	  param.append("creator",myVue.formParm.creator); 
    	  param.append("organizationId",myVue.formParm.organizationId); 
    	  param.append("workdate",myVue.formParm.workdate); 
    	  param.append("pageSize",myVue.pageParm.pageCount); 
     	  param.append("currentPage",1); 
    	  axios.post('<%=ctxPath %>/jb/mealallowance/searchworkday/api', param)
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
     	  param.append("pageSize",myVue.pageParm.pageCount); 
     	  param.append("currentPage",page); 
     	  axios.post('<%=ctxPath %>/jb/mealallowance/searchworkday/api', param)
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
      //修改餐补次数
      upPorposalMeans: function(index,value) {
    	  let param = new URLSearchParams(); 
        param.append("id",this.tableDatas[index].id); 
        var porposalMeans = this.tableDatas[index].porposalmeans + value;
        if(porposalMeans>2||porposalMeans<0){
        	myVue.$Message.error({
                content: "请正确处理餐补次数！",
                duration: 3,
                closable: true
            });
        	return ;
        }
        param.append("porposalMeans",porposalMeans); 
        axios.post('<%=ctxPath %>/jb/mealallowance/upporposalmeans/api', param)
        .then(function (response) {
          if(!response.data.flag){
        	  myVue.$Modal.error({
                  content: "设置餐补次数失败",
                  closable: true
              });
          }else{
        	  this.myVue.tableDatas[index].porposalmeans=porposalMeans;
          }
        })
      },
      //批量上报
      batchReportPass:function() {
    	  let param = new URLSearchParams(); 
    	  axios.post('<%=ctxPath %>/jb/mealallowance/countreport/api', param)
    		  .then(function (response) {
    			  var count = response.data.data;
    			  if(count != 0){
    				  myVue.$Modal.confirm({
        	              title: '提示',
        	              content: "共"+ count + "条数据，上报后不能修改餐补次数了，您确定要全部上报么？",
        	              onOk:function(){
        	            	  let param = new URLSearchParams(); 
        	            	  param.append("creator",myVue.formParm.creator); 
        	            	  param.append("organizationId",myVue.formParm.organizationId); 
        	            	  param.append("workdate",myVue.formParm.workdate); 
        	            	  param.append("pageSize",myVue.pageParm.pageCount); 
        	         	      param.append("currentPage",1);
        	            	  axios.post('<%=ctxPath %>/jb/mealallowance/batchreportpass/api', param)
        	            		  .then(function (response) {
        	            			  if(response.data.flag){
        	            				  myVue.$Message.success({
        	                              	content: '全部上报成功',
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
        	              }
        	          });
    			  }else{
    				  myVue.$Message.warning({
                          content: "没有待上报的数据",
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
    	  param.append("pageSize",pageSize); 
     	  param.append("currentPage",1); 
    	  axios.post('<%=ctxPath %>/jb/mealallowance/searchworkday/api', param)
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
     	  axios.post('<%=ctxPath %>/jb/mealallowance/getempsbyorgid/api', param)
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
      },
      report:function(index){
    	  this.$Modal.confirm({
              title: '提示',
              content: "确定上报这条数据？",
              onOk:function(){
            	  let param = new URLSearchParams(); 
            	  param.append("id",myVue.tableDatas[index].id); 
            	  param.append("creator",myVue.formParm.creator); 
            	  param.append("organizationId",myVue.formParm.organizationId); 
            	  param.append("workdate",myVue.formParm.workdate); 
            	  param.append("pageSize",myVue.pageParm.pageCount); 
         	      param.append("currentPage",myVue.pageParm.currentPage);
            	  axios.post('<%=ctxPath %>/jb/mealallowance/reportpass/api', param)
            		  .then(function (response) {
            			  if(response.data.flag){
            				  myVue.$Message.success({
                              	content: '上报成功',
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
              }
          });
      }
    }
});

function compareDate(start,end,target){
	//把字符串格式转化为日期类
	var starttime = new Date(start);
	var endtime = new Date(end);
	// 转换日期格式
	target = target.replace(/-/g, '/'); // "2010/08/01";
	// 创建日期对象
	var targettime = new Date(target);
	//进行比较
	//进行比较
	return (targettime>=starttime && targettime<=endtime);
}
//刷新分页信息
function flushPage(page){
	myVue.pageParm.totalCount = page.totalCount;
	myVue.pageParm.pageCount = page.pageCount;
	myVue.pageParm.currentPage = page.currentPage;
};
</script>
