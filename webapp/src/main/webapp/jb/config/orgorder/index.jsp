<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String ctxPath = request.getContextPath();
%>
<div id="app_content" v-cloak style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item>首页</breadcrumb-item> 
            <breadcrumb-item>加班管理</breadcrumb-item> 
            <breadcrumb-item>排序设置</breadcrumb-item> 
        </Breadcrumb>
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}"> 
          <i-table stripe border :columns="columns1" :data="data1"></i-table> 
        </i-content> 
    </Layout>
</div>
<script>
var myVue = new Vue({
    el: '#app_content',
    data:{
      orderNum:'',
      data1:[],
      columns1:[
            {
                title: '姓名',
                key: 'realname'
            },
            {
                title: '用户名',
                key: 'username'
            },
            {
                title: '所属部门',
                sortable: true,
                key: 'department'
            },
            {
                title: '导出顺序',
                sortable: true,
                key: 'innerOrder'
            },
            {
                title: '职务',
                key: 'duty'
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
                                type: 'primary',
                                size: 'small'
                            },
                            style: {
                                marginRight: '5px'
                            },
                            on: {
                                click: () => {
                                  myVue.modify(params.index)
                                }
                            }
                        }, '修改')
                    ]);
                }
            }
        ]
    },
    methods: {
      modify: function (index) {
    	  this.$Modal.confirm({
          render: (h) => {
            return h('input-number', {
              props: {
                value: this.data1[index].innerOrder,
                min:1,
                max:100,
                size:'large',
                placeholder: '请输入导出人员序号整数'
              },
              on: {
                input: (val) => {
                	if(isNaN(val)){
                		console.log("不是数字");
                	}else{
                		this.orderNum = val;
                	}
                }
              }
            })
          },
          onOk:() => {
        	  axios({
                  method:'post',
                  url:'<%=ctxPath%>/jb/config/orgorder/setorder',
                  params:{
                      userId:this.data1[index].id,
                      innerOrder:this.orderNum
                  },
                  responseType:'json'
                }).then((response) => {
                  var result = response.data;
                  if(result.flag){
                    this.data1[index].innerOrder = this.orderNum;
                  }
                });
          }
    	});
      }
    },
    created: function () {
      axios({
          method:'get',
          url:'<%=ctxPath%>/jb/config/orgorder/getorder/api',
          responseType:'json'
        }).then((response) => {
          this.data1=response.data.data;
        });
      console.log("模拟初始化--加载中...TODO")
    }
});
</script>
