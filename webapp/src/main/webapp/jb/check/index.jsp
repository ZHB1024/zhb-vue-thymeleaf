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
        
        	<div style="margin-bottom: 10px;">
        		<Date-picker  type="daterange"  name="workdate"  v-model="formParm.workdate"  format="yyyy-MM-dd"  placeholder="请选择审核日期范围时间段" style="width: 200px">
            	</Date-picker>  
        		<i-button @click="batchCheckPass()" style="color: #fff;background-color: #2d8cf0;">批量审核通过</i-button>
        	</div>
        	
        	<i-table border ref='selections' @on-selection-change="selectionChange" :columns="columns1" :data="tableDatas"></i-table>
        	
        	<div style="margin-top: 10px;">
        		<Date-picker  type="daterange"  name="workdate"  v-model="formParm.workdate"  format="yyyy-MM-dd"  placeholder="请选择审核日期范围时间段" style="width: 200px">
            	</Date-picker>  
        		<i-button @click="batchCheckPass()" style="color: #fff;background-color: #2d8cf0;">批量审核通过</i-button>
        	</div>
        	<div style="margin-top:15px;">
        		<Page :total="pageParm.totalCount" :current="pageParm.currentPage" :page-size="pageParm.pageCount" @on-change="searchPage" @on-page-size-change="changePageSize" show-total show-sizer show-elevator/>
        	</div>
        	
        </i-content> 
    </Layout>
</div>
<script>
var myVue = new Vue({
    el: '#app_content',
    data:{
    	tableDatas:[],
    	formParm:{
	        creator:'',
	        organizationId:'',
	        workdate:'',
	        status:''
	  	},
	  	pageParm:{
	  		currentPage:1,
	        pageCount:20,
	        totalCount:0
	  	},
	  	ids:[],
	  	workDates:[],
	  	empParm:[] ,
	  	orgsParm:[] ,
	  	statusParm:[],
    	columns1:[
    		{
    			type: 'selection',
                width: 50,
                align: 'center'
            },
            {
                title: '序号',
                type:"index",
                width: 70
            },
            {
                title: '姓名',
                key: 'creator',
                minWidth: 80,
                sortable:true
            },
            {
                title: '加班日期',
                key: 'workdate',
                minWidth: 100,
                sortable:true
            },
            
            {
                title: '加班明细',
                key: 'worRecords',
                width:500
            },
            {
                title: '加班时长（小时）',
                key: 'hours',
                minWidth: 100
            },
            {
                title: '组织',
                key: 'organization',
                minWidth: 100
            },
            {
                title: '审核状态',
                key: 'statusName',
                minWidth: 100
            } ,
            {
                title: '操作',
                key: 'action',
                width: 150,
                align: 'center',
                render: (h, params) => {
                	var status = myVue.tableDatas[params.index].status,passFlag,unpassFlag;
                	if(status != 0 && status != 2){
                		passFlag = 'disabled';
                    }
                	if(status != 2 && status != 3){
                		unpassFlag = 'disabled';
                    }
                	return h('div', [
                        h('i-button', {
                            props: {
                                type: 'primary',
                                size: 'small',
                                disabled:passFlag
                            },
                            style: {
                                marginRight: '5px'
                            },
                            on: {
                                click: () => {
                                	myVue.checkPass(params.index)
                                }
                            }
                        }, '通过'),
                        h('i-button', {
                            props: {
                                type: 'primary',
                                size: 'small',
                                disabled:unpassFlag
                            },
                            style: {
                                marginRight: '5px'
                            },
                            on: {
                                click: () => {
                                	myVue.checkUnPass(params.index)
                                }
                            }
                        }, '不通过')
                    ]);
                }
            } 
        ]
    },
    created: function () {
    	axios.all([
    	    axios.get('<%=ctxPath %>/jb/check/searchworkday/api'),
    	    axios.get('<%=ctxPath %>/jb/check/searchemployees/api'),
    	    axios.get('<%=ctxPath %>/jb/check/searchorganizations/api'),
    	    axios.get('<%=ctxPath %>/jb/check/getstatus/api')
    	  ]).then(axios.spread(function (workRecordResp,empResp,orgResp,statusResp) {
    		  myVue.tableDatas = workRecordResp.data.data.result;
    		  flushPage(workRecordResp.data.data);
    		  myVue.empParm = empResp.data.data;
    		  myVue.orgsParm = orgResp.data.data;
    		  if(orgResp.data.data.length==1){
    			  myVue.formParm.organizationId = orgResp.data.data[0].id;
    		  }
    		  myVue.statusParm = statusResp.data.data;
    		  myVue.formParm.status = '2';
    	  }));
    },
    methods: {
        //查询
    	search:function () {
        	  let param = new URLSearchParams(); 
        	  param.append("creator",myVue.formParm.creator); 
        	  param.append("organizationId",myVue.formParm.organizationId); 
        	  param.append("status",myVue.formParm.status); 
        	  param.append("pageSize",myVue.pageParm.pageCount); 
         	  param.append("currentPage",1); 
        	  axios.post('<%=ctxPath %>/jb/check/searchworkday/api', param)
        		  .then(function (response) {
        			  if(response.data.flag){
        				  myVue.tableDatas=response.data.data.result;
        				  flushPage(response.data.data);
        				  flushIds();
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
       //分页查询，点击上一页、下一页、数字
       searchPage:function (page) {
     	  let param = new URLSearchParams(); 
     	  param.append("creator",myVue.formParm.creator); 
     	  param.append("organizationId",myVue.formParm.organizationId); 
     	  param.append("status",myVue.formParm.status); 
     	  param.append("pageSize",myVue.pageParm.pageCount); 
     	  param.append("currentPage",page); 
     	  axios.post('<%=ctxPath %>/jb/check/searchworkday/api', param)
     		  .then(function (response) {
     			  if(response.data.flag){
     				  myVue.tableDatas=response.data.data.result;
     				  flushPage(response.data.data);
     				  flushIds();
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
       //不通过
       checkUnPass: function (index) {
    		this.$Modal.confirm({
                title: '提示',
                content: "确定审核不通过？",
                onOk:function(){
                	let param = new URLSearchParams(); 
              	    param.append("creator",myVue.formParm.creator); 
              	    param.append("organizationId",myVue.formParm.organizationId);
              	    param.append("status",myVue.formParm.status);
              	    param.append("workDayId",myVue.tableDatas[index].id); 
              	    param.append("pageSize",myVue.pageParm.pageCount); 
           	        param.append("currentPage",myVue.pageParm.currentPage);
              	  	axios.post('<%=ctxPath %>/jb/check/checkunpass/api', param)
        		         .then(function (response) {
        			  		if(response.data.flag){
        			  			myVue.$Message.success({
                                  	content: '审核不通过成功',
                                  	duration: 3,
                                  	closable: true
                              	  });
        				  		myVue.tableDatas=response.data.data.result;
        				  		flushPage(response.data.data);
        				  		flushIds();
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
       //单条审核通过
       checkPass: function (index) {
    		this.$Modal.confirm({
                title: '提示',
                content: "确定审核通过？",
                onOk:function(){
                	let param = new URLSearchParams(); 
              	    param.append("creator",myVue.formParm.creator); 
              	    param.append("organizationId",myVue.formParm.organizationId);
              	    param.append("status",myVue.formParm.status);
              	    param.append("workDayId",myVue.tableDatas[index].id); 
              	    param.append("pageSize",myVue.pageParm.pageCount); 
           	        param.append("currentPage",myVue.pageParm.currentPage);
              	  	axios.post('<%=ctxPath %>/jb/check/checkpass/api', param)
        		         .then(function (response) {
        			  		if(response.data.flag){
        			  			myVue.$Message.success({
                                  	content: '审核通过成功',
                                  	duration: 3,
                                  	closable: true
                              	  });
        				  		myVue.tableDatas=response.data.data.result;
        				  		flushPage(response.data.data);
        				  		flushIds();
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
      //批量审核通过
      batchCheckPass:function() {
    	  if(myVue.ids.length == 0){
    		  myVue.$Message.warning({
                  content: '请选择数据',
                  duration: 3,
                  closable: true
              });
    		  return;
    	  }
    	  var passDate = myVue.formParm.workdate;
    	  if(passDate[0]){
    		  /* myVue.$Message.warning({
                  content: '请选择审核日期范围时间段',
                  duration: 3,
                  closable: true
              });
    		  return; */
    		//验证勾选的记录是否在审核时间范围内
        	  var valitalResult = '';
        	  for (var i=0;i<myVue.workDates.length;i++){
            	  if(!compareDate(passDate[0],passDate[1],myVue.workDates[i])){
            		  valitalResult += (i+1) + '、';
            	  }
              }
        	  //这是验证的结果
        	  if('' != valitalResult){
        		  valitalResult = valitalResult.substring(0,valitalResult.lastIndexOf('、'));
        		  myVue.$Message.warning({
                      content: '您勾选的第' + valitalResult +"条数据不在审核日期范围时间段内",
                      duration: 5,
                      closable: true
                  });
        		  return;
        	  }
    	  }
    	  
    	  var count = myVue.ids.length;
    	  this.$Modal.confirm({
              title: '提示',
              content: "共 "+ count+" 条数据，确定批量审核通过？",
              onOk:function(){
            	  let param = new URLSearchParams(); 
            	  param.append("creator",myVue.formParm.creator); 
            	  param.append("organizationId",myVue.formParm.organizationId);
            	  param.append("status",myVue.formParm.status);
            	  param.append("pageSize",myVue.pageParm.pageCount); 
           	      param.append("currentPage",myVue.pageParm.currentPage);
            	  param.append("workDayIds",myVue.ids); 
            	  axios.post('<%=ctxPath %>/jb/check/batchcheckpass/api', param)
            		  .then(function (response) {
            			  if(response.data.flag){
            				  if(response.data.flag){
                				  myVue.$Message.success({
                                  	content: '批量审核通过成功',
                                  	duration: 3,
                                  	closable: true
                              	  });
            				  	  myVue.tableDatas=response.data.data.result;
            				  	  flushPage(response.data.data);
            				  	  flushIds();
            				  	  myVue.$forceUpdate();
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
    	  });
      },
      //勾选多选框
      selectionChange:function(selections){
          myVue.ids = [];
          myVue.workDates = [];
          for (var i=0;i<selections.length;i++){
        	  myVue.ids.push(selections[i].id);
        	  myVue.workDates.push(selections[i].workdate);
          }
      },
      //调整每页大小时
      changePageSize:function(pageSize){
    	  let param = new URLSearchParams(); 
     	  param.append("creator",myVue.formParm.creator); 
     	  param.append("organizationId",myVue.formParm.organizationId);
     	  param.append("status",myVue.formParm.status);
     	  param.append("pageSize",pageSize); 
     	  param.append("currentPage",1); 
     	  axios.post('<%=ctxPath %>/jb/check/searchworkday/api', param)
     		  .then(function (response) {
     			  if(response.data.flag){
     				  myVue.tableDatas=response.data.data.result;
     				  flushPage(response.data.data);
     				  flushIds();
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
      //组织 员工级联
      getEmployees:function(orgId){
    	  let param = new URLSearchParams(); 
     	  param.append("orgId",orgId); 
     	  axios.post('<%=ctxPath %>/jb/check/getempsbyorgid/api', param)
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

//目标日期 是否在两个日期之间
function compareDate(start,end,target){
	//把字符串格式转化为日期类
	var starttime = new Date(start);
	var endtime = new Date(end);
	// 转换日期格式
	target = target.replace(/-/g, '/'); // "2010/08/01";
	// 创建日期对象
	var targettime = new Date(target);
	//进行比较
	return (targettime>=starttime && targettime<=endtime);
}

//刷新分页信息
function flushPage(page){
	myVue.pageParm.totalCount = page.totalCount;
	myVue.pageParm.pageCount = page.pageCount;
	myVue.pageParm.currentPage = page.currentPage;
};
//置空被选择的checkbox
function flushIds(){
	myVue.ids =[];
	myVue.workDates=[];
}
</script>
