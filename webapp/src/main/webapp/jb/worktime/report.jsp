<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
    String ctxPath = request.getContextPath();
%>
<div id="app_content" v-cloak style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item>加班记录</breadcrumb-item> 
        </Breadcrumb> 
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
        	<i-form inline method="post">
        	
        		<form-item prop="workdate">
                  		<Date-picker  type="daterange"  name="workdate"  v-model="formParm.workdate"  format="yyyy-MM-dd"  placeholder="加班日期" style="width: 200px">
                    	</Date-picker>  
                </form-item >
        	    
        		<form-item prop="status">
        			<i-select name="status" v-model="formParm.status" style="width: 150px;" placeholder="请选择审核状态">
        					<i-option value="" >
                        		全部
                        	</i-option>
                        	<i-option v-bind:value="checkStatus.index" v-for="checkStatus in statusParm">
                        		{{checkStatus.name}}
                        	</i-option>
                	</i-select>
                </form-item>
                
                <form-item>
                    	<i-button type="primary" @click="search()" > 查   询 </i-button>
                </form-item>
                
                <form-item>
                    	<i-button type="primary" to="/jb/worktime/toaddrecord">新增加班记录</i-button> 
                </form-item>
                	
        	</i-form>
        
        	<i-table border :columns="columns1" :data="tableDatas" @on-row-click="onRowClick" class="expand-table"></i-table> 
        	<div style="margin-top:6px;">
        		<Page :total="pageParm.totalCount" :current="pageParm.currentPage" :page-size="pageParm.pageCount" @on-change="searchPage" @on-page-size-change="changePageSize" show-total show-sizer show-elevator/>
        	</div>
        	
        </i-content> 
    </Layout>
</div>

<script type="x-template" id="workrecord_detail">
<div>
  <Row v-for="record in row.worRecords" class="expand-row">
    <i-col span="4">
      <span class="expand-key">时间段: </span>
      <span class="expand-value">{{ record.worktimes }}</span>
    </i-col>
    <i-col span="3">
      <span class="expand-key">加班时长（小时）: </span>
      <span class="expand-value">{{ record.workHours }}</span>
    </i-col>
    <i-col span="4">
      <span class="expand-key">加班内容: </span>
      <span class="expand-value">{{ record.content }}</span>
    </i-col>
    <i-col span="10">
      <span class="expand-key">加班详情: </span>
      <span class="expand-value">{{ record.detail }}</span>
    </i-col>

	<i-col span="2" align="center">
      <span class="expand-key"><i-button type="primary" v-if="record.status==0||record.status==1" v-bind:update-id="record.id" size="small"  onclick="toUpdate(this)" > 修改 </i-button></span>
      <span class="expand-key"><i-button type="error" v-if="record.status==0||record.status==1" v-bind:del-id="record.id" size="small" onclick="delRecord(this)" > 删除 </i-button></span>

	  <span class="expand-key"><i-button type="primary" v-if="record.status!=0&&record.status!=1" disabled="disabled" v-bind:update-id="record.id" size="small"  onclick="toUpdate(this)" > 修改 </i-button></span>
      <span class="expand-key"><i-button type="error" v-if="record.status!=0&&record.status!=1" disabled="disabled" v-bind:del-id="record.id" size="small" onclick="delRecord(this)" > 删除 </i-button></span>
    </i-col>


  </Row>
</div>
</script>
<script>
Vue.component('newRow', {
	  props: ['row'],
	  template: '#workrecord_detail'
	});
var myVue = new Vue({
    el: '#app_content',
    data:{
    	tableDatas:[],
    	formParm:{
	        workdate:'',
	        status:''
	  	},
	  	pageParm:{
	  		currentPage:1,
	        pageCount:20,
	        totalCount:0
	  	},
	  	statusParm:[] ,
    	columns1:[
            {
            	type: 'expand',
      			width: 100,
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
                title: '加班日期',
                key: 'workdate',
                minWidth: 100,
                sortable:true
            },
            {
                title: '加班时长（小时）',
                key: 'hours',
                minWidth: 100
            },
            {
                title: '审核状态',
                key: 'statusName',
                minWidth: 100
            },
            {
                title: '操作',
                key: 'action',
                width: 150,
                align: 'center',
                render: (h, params) => {
                	var status = myVue.tableDatas[params.index].status,reportFlag,delFlag;
                	if(status != 1 && status != 0){
                		reportFlag = 'disabled';
                	}
                	if(status != 1 && status != 2 && status != 0){
                		delFlag = 'disabled';
                	}
                	return h('div', [
                        h('i-button', {
                            props: {
                                type: 'primary',
                                size: 'small',
                                disabled:reportFlag
                            },
                            style: {
                                marginRight: '5px'
                            },
                            on: {
                                click: () => {
                                	myVue.report(params.index)
                                }
                            }
                        }, '提交')
                    ]);
                }
            }
        ]
    },
    created: function () {
    	axios.all([
    	    axios.get('<%=ctxPath %>/jb/worktime/searchworkday/api'),
    	    axios.get('<%=ctxPath %>/jb/worktime/getstatus/api')
    	  ]).then(axios.spread(function (workRecordResp,statusResp) {
    		  myVue.tableDatas = workRecordResp.data.data.result;
    		  flushPage(workRecordResp.data.data);
    		  myVue.statusParm = statusResp.data.data;
    	  }));
    },
    methods: {
    	//查询
    	search:function () {
      	    let param = new URLSearchParams(); 
      	    param.append("workdate",myVue.formParm.workdate); 
      	    param.append("status",myVue.formParm.status); 
      	    param.append("pageSize",myVue.pageParm.pageCount); 
     	    param.append("currentPage",1); 
      	    axios.post('<%=ctxPath %>/jb/worktime/searchworkday/api', param)
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
        //分页
        searchPage:function (page) {
      	    let param = new URLSearchParams(); 
      	    param.append("workdate",myVue.formParm.workdate); 
      	    param.append("status",myVue.formParm.status); 
      	    param.append("pageSize",myVue.pageParm.pageCount); 
     	    param.append("currentPage",page); 
      	    axios.post('<%=ctxPath %>/jb/worktime/searchworkday/api', param)
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
        //提交
        report: function (index) {
    		this.$Modal.confirm({
                title: '提示',
                content: '提交后数据不能被编辑了，您确定要提交么？',
                onOk:function(){
                	let param = new URLSearchParams(); 
              	    param.append("workdate",myVue.formParm.workdate); 
              	    param.append("status",myVue.formParm.status); 
              	    param.append("id",myVue.tableDatas[index].id); 
              	    param.append("pageSize",myVue.pageParm.pageCount); 
               	    param.append("currentPage",myVue.pageParm.currentPage); 
               	    axios.post('<%=ctxPath %>/jb/worktime/submitworkday/api', param)
              		  .then(function (response) {
              			  if(response.data.flag){
              				  myVue.$Message.success({
                                  content: "提交成功",
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
      //删除
      remove: function (index) {
      	 this.$Modal.confirm({
            title: '提示',
            content: '该日期下的所有记录都将被删除，您确定要删除么？',
            onOk:function(){
            	let param = new URLSearchParams(); 
          	    param.append("workdate",myVue.formParm.workdate); 
          	    param.append("status",myVue.formParm.status); 
          	    param.append("id",myVue.tableDatas[index].id); 
          	    param.append("pageSize",myVue.pageParm.pageCount); 
           	    param.append("currentPage",myVue.pageParm.currentPage); 
           	    axios.post('<%=ctxPath %>/jb/worktime/delworkday/api', param)
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
      changePageSize:function(pageSize){
    	  let param = new URLSearchParams(); 
    	  param.append("workdate",myVue.formParm.workdate); 
    	  param.append("status",myVue.formParm.status); 
    	  param.append("pageSize",pageSize); 
     	  param.append("currentPage",1); 
     	  axios.post('<%=ctxPath %>/jb/worktime/searchworkday/api', param)
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
      onRowClick:function(data,index){
    	  //console.log(data);
    	  //console.log(index);
      }
    }
});

//刷新分页信息
function flushPage(page){
	myVue.pageParm.totalCount = page.totalCount;
	myVue.pageParm.pageCount = page.pageCount;
	myVue.pageParm.currentPage = page.currentPage;
};
//修改单条加班记录
function toUpdate(data){
	var id = data.getAttribute("update-id");
	window.location.href='<%=ctxPath%>/jb/worktime/touprecord?workRecordId='+ id;
};
//删除单条加班记录
function delRecord(data){
	var id = data.getAttribute("del-id");
	myVue.$Modal.confirm({
        title: '提示',
        content: '您确定要删除么？',
        onOk:function(){
        	let param = new URLSearchParams(); 
      	    param.append("workdate",myVue.formParm.workdate); 
      	    param.append("status",myVue.formParm.status); 
      	    param.append("id",id); 
      	    param.append("pageSize",myVue.pageParm.pageCount); 
       	    param.append("currentPage",myVue.pageParm.currentPage); 
       	    axios.post('<%=ctxPath %>/jb/worktime/delrecord/api', param)
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
}
</script>
