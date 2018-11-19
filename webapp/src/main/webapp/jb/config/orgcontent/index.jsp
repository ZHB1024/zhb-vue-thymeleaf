<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
String ctxPath = request.getContextPath();
%>
<div id="app_content" v-cloak>
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item>首页</breadcrumb-item> 
            <breadcrumb-item>加班管理</breadcrumb-item> 
            <breadcrumb-item>内容设置</breadcrumb-item> 
        </Breadcrumb> 
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
          <Row type="flex" justify="center" align="top" class="code-i-row-bg">
            <i-col span="12" >
              <div style="text-align: center;">
                <h3>组织中工作内容</h3>
              </div>
              <i-table border :columns="columns2" :data="data2"></i-table> 
            </i-col>
            <i-col span="8">
              <div style="width: 400px;margin-left:  auto;margin-right:  auto;">
                <i-form method="post" action="<%=ctxPath %>/jb/config/orgcontent/add" ref="formValidate" :model="formParm" :rules="ruleValidate" :label-width="80">
                  <form-item label="内容" prop="contents">
                    <i-select name="contentIds" multiple v-model="formParm.contents">
                        <i-option v-bind:value="item.id" v-for="item in contents">
                        {{item.content}}
                        </i-option>
                    </i-select>
                  </form-item>
                  <form-item label="组织" prop="organizations">
                    <i-select name="orgIds" multiple v-model="formParm.organizations">
                        <i-option v-bind:value="item.id" v-for="item in orgs">
                        {{item.orgname}}
                        </i-option>
                    </i-select>
                  </form-item>
                  <form-item>
                    <i-button type="primary" @click="handleSubmit('formValidate')">提交</i-button>
                    <i-button @click="handleReset('formValidate')" style="margin-left: 8px">重置</i-button>
                  </form-item>
                </i-form>
                <br/>
                <div style="text-align: center;">
                  <h3>工作内容池</h3>
                </div>
                <i-table border :columns="columns1" :data="contents"></i-table> 
              </div>
            </i-col>
          </Row>
        </i-content> 
    </Layout>
</div>
<script>
var myVue = new Vue({
    el: '#app_content',
    data:{
      orgs:[],
      contents:[],
      data2:[],
      columns1:[
        {
          title: '序号',
          type: 'index',
          width: 60,
          align: 'center'
        }, 
        {
          title: '加班内容',
          key: 'content'
        }
      ],
      columns2:[
        {
          title: '序号',
          type: 'index',
          width: 60,
          align: 'center'
        }, 
        {
            title: '加班内容',
            key: 'content'
          },
        {
          title: '组织',
          render:(h, params) => {
        	  var arr = new Array();
        	  for(var i=0;i<params.row.orgNames.length;i++){
        		  arr.push(h('div',params.row.orgNames[i].orgName));
        	  }
        	  return h('div',arr);
          }
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
      ],
      formParm:{
    	  contents:[],
    	  organizations:[]
      },
      ruleValidate: {
        contents: [
        	{ required: true, type:"array", message: '请选择内容', trigger: 'change' }
        ],
        organizations: [
        	{ required: true, type:"array", message: '请选择组织', trigger: 'change' }
        ]
      }
    },
    methods: {
      handleSubmit (name) {
        this.$refs[name].validate((valid) => {
          if (valid) {
            this.$Message.success('Success!');
//             console.log(this.$refs[name].$el);
            this.$refs[name].$el.submit();
          } else {
            this.$Message.error('失败!');
          }
        })
      },
      handleReset (name) {
        this.$refs[name].resetFields();
      },
      remove: function (index) {
        var refId;
        if(myVue.data2[index].orgNames.length>1){
        	var indexOrg = 0;
	    	this.$Modal.confirm({
	            render: (h) => {
	              return h('input-number', {
	                props: {
	                  min:1,
	                  max:100,
	                  placeholder: '请输入要删除的组织的序号'
	                },
	                on: {
	                  input: (val) => {
	                      if(isNaN(val)){
	                          console.log("不是数字");
	                      }else{
	                    	  indexOrg = val-1;
	                      }
	                  }
	                }
	              })
	            },
	            onOk:() => {
	            	console.log(indexOrg);
	            	if(indexOrg<0){
	            		return;
	            	}
	            	refId = this.data2[index].refIds[indexOrg].refId;
	                axios({
	                    method:'post',
	                    url:'<%=ctxPath%>/jb/config/orgcontent/del',
	                    params:{
	                    	refId:refId
	                    },
	                    responseType:'json'
	                  }).then((response) => {
	                    var result = response.data;
	                    if(!result.flag){
	                        myVue.$Modal.error({
	                            title: '错误',
	                            content: '删除出现问题。'
	                        });
	                      }else{
                              myVue.data2[index].orgNames.splice(indexOrg, 1);
                              myVue.data2[index].refIds.splice(indexOrg, 1);
	                      }
	                  });
	            }
	          });
        }else{
        	refId = this.data2[index].refIds[0].refId;
	        this.$Modal.confirm({
	          title: '确认',
	          content: '确认要删除此项内容吗？',
	          onOk:function(){
	            axios({
	              method:'post',
	              url:'<%=ctxPath%>/jb/config/orgcontent/del',
	              params:{
	            	  refId:refId
	              },
	              responseType:'json'
	            }).then((response) => {
	              var result = response.data;
	              if(!result.flag){
	                myVue.$Modal.error({
	                    title: '错误',
	                    content: '删除出现问题。'
	                });
	              }else{
            		  myVue.data2.splice(index, 1);
	              }
	            });
	          }
	        });
        }
    	
      }
    },
    created: function () {
      axios({
        method:'get',
        url:'<%=ctxPath%>/jb/config/content/allworkcontent/api',
        responseType:'json'
      }).then((response) => {
        this.contents=response.data.data;
      });
      axios({
        method:'post',
        url:'/authority/getorganization/api',
        params:{
        	role:'ROLE_KQ_JB_NRHQ'
        },
        responseType:'json'
      }).then((response) => {
        this.orgs=response.data.data;
      });
      axios({
          method:'post',
          url:'/authority/getcontent/api',
          params:{
            role:'ROLE_KQ_JB_NRHQ'
          },
          responseType:'json'
        }).then((response) => {
          this.data2=response.data.data;
        });
      console.log("内容初始化--加载中...TODO")
    }
});
</script>
