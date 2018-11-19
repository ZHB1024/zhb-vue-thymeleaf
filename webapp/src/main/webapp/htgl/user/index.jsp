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
            <breadcrumb-item>用户管理</breadcrumb-item> 
            <breadcrumb-item>用户信息</breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', background: '#fff'}">
        	
        	<div class="filter-box clearfix">
                <i-button type="success" class="add-btn" to="/htgl/userinfocontroller/toadd">新增用户</i-button>
            </div>
        	
        	<i-table border :columns="columns1" :data="tableDatas" ></i-table> 
        </i-content>
        
    </Layout>
</div>


<script>
var myVue = new Vue({
    el: '#app_content',
    data:{
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
                render: (h, params) => {
                	return h('div', [
                		h('a', {
                            style: {
                                marginRight: '5px'
                            },
                            on: {
                                click: () => {
                                	  myVue.showinfo(params)
                                }
                            }
                        }, params.row.userName)
                    ]);
                }
            },
            {
                title: '姓名',
                key: 'realName',
                minWidth: 100,
                sortable:true
            },
            {
                title: '性别',
                key: 'sex',
                minWidth: 100
            },
            {
                title: '邮箱',
                key: 'email',
                minWidth: 100
            },
            {
                title: '状态',
                key: 'deleteFlagName',
                minWidth: 100
            },
            {
                title: '操作',
                key: 'action',
                width: 150,
                align: 'center',
                render: (h, params) => {
                	var status = myVue.tableDatas[params.index].deleteFlag,openFlag,delFlag;
                	if(status == 0 ){
                		openFlag = 'disabled';
                	}
                	if(status == 1 ){
                		delFlag = 'disabled';
                	}
                	return h('div', [
                		h('i-button', {
                            props: {
                                type: 'primary',
                                size: 'small',
                                disabled:openFlag
                            },
                            style: {
                                marginRight: '5px'
                            },
                            on: {
                                click: () => {
                                	  myVue.open(params.index)
                                }
                            }
                        }, '开通'),
                        h('i-button', {
                            props: {
                                type: 'error',
                                size: 'small',
                                disabled:delFlag
                            },
                            on: {
                                click: () => {
                                	  myVue.remove(params.index)
                                }
                            }
                        }, '注销')
                    ]);
                }
            } 
        ]
    },
    created: function () {
    	axios.all([
    	    axios.get('<%=ctxPath %>/htgl/userinfocontroller/getuserinfo/api')
    	  ]).then(axios.spread(function (userinfoResp) {
    		  myVue.tableDatas = userinfoResp.data.data;
    	  }));
    },
    methods: {
    	//重新开通账号
    	open: function (index) {
        	this.$Modal.confirm({
                title: '提示',
                content: '您确定要重新开通这个账号么？',
                onOk:function(){
                	let param = new URLSearchParams(); 
              	    param.append("id",myVue.tableDatas[index].id); 
              	    param.append("deleteFlag",0); 
              	    axios.post('<%=ctxPath %>/htgl/userinfocontroller/deloropenaccount/api', param)
        		         .then(function (response) {
        			  		if(response.data.flag){
        			  			myVue.$Message.success({
                                    content: "开通成功",
                                    duration: 3,
                                    closable: true
                                });
        				 		 myVue.tableDatas = response.data.data;
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
      	//注销账号
       remove: function (index) {
        this.$Modal.confirm({
            title: '提示',
            content: '您确定要注销这个账号么？',
            onOk:function(){
            	let param = new URLSearchParams(); 
          	    param.append("id",myVue.tableDatas[index].id); 
          	    param.append("deleteFlag",1); 
          	    axios.post('<%=ctxPath %>/htgl/userinfocontroller/deloropenaccount/api', param)
    		         .then(function (response) {
    			  		if(response.data.flag){
    			  			myVue.$Message.success({
                                content: "注销成功",
                                duration: 3,
                                closable: true
                            });
    				 		 myVue.tableDatas = response.data.data;
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
       /*0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）*/
       /*弹出层*/
       showinfo:function(data){
    	   var showContent = '<table align="center" style="border-collapse:separate; border-spacing:10px;">';

    	   showContent += generatorValue("用户名",data.row.userName);
    	   showContent += generatorValue("姓名",data.row.realName);
    	   showContent += generatorValue("性别",data.row.sex);
    	   showContent += generatorValue("身份证号",data.row.identityCard);
    	   showContent += generatorValue("出生日期",data.row.birthday);
    	   showContent += generatorValue("国家/地区",data.row.country);
    	   showContent += generatorValue("民族",data.row.nation);
    	   showContent += generatorValue("毕业院校",data.row.byyx);
    	   showContent += generatorValue("手机号",data.row.mobilePhone);
    	   showContent += generatorValue("电子邮箱",data.row.email);
    	   
    	   showContent += '</table>';
    	   
    	   layer.open({
    	        title: data.row.userName,
    	        type: 1,
    	        //skin: 'layui-layer-rim', //加上边框
    	        area: ['500px', '500px'], //宽高
    	        content: showContent, 
    	        btn: ['确定'],
    	        success: function(layero, index){
    	        },
    	        yes: function(index, layero){
    	            layer.closeAll();
    	        }
    	    });
       }
    }
});
function generatorValue(name,value){
    return '<tr><th>' + name + '：</th><td>' + value + '</td></tr>'; 
}
</script>
