<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
String ctxPath = request.getContextPath();
%>
<div id="app_content" style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item>首页</breadcrumb-item> 
            <breadcrumb-item>加班配置</breadcrumb-item> 
            <breadcrumb-item>内容配置</breadcrumb-item> 
        </Breadcrumb> 
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
            <i-form method="post" action="<%=ctxPath %>/jb/config/content/add" ref="formValidate" :model="formParm" :rules="ruleValidate" :label-width="60" class="add-person">
              <form-item label="内容" prop="content">
                <i-input name="content" v-model="formParm.content" placeholder="请输入..." style="width: 300px;"></i-input>
              </form-item >
              <form-item label="组织">
                <i-select name="orgId" style="width: 150px;">
                    <i-option v-bind:value="item.id" v-for="item in orgs">
                    {{item.name}}
                    </i-option>
                </i-select>
              </form-item>
              <form-item>
                <i-button type="primary" @click="handleSubmit('formValidate')">提交</i-button>
                <i-button @click="handleReset('formValidate')" style="margin-left: 8px">重置</i-button>
              </form-item>
            </i-form>

            <i-table border :columns="columns1" :data="data1"></i-table>
        </i-content> 
    </Layout>
</div>
<script>
var myVue = new Vue({
    el: '#app_content',
    data:{
        orgs:[],
        data1:[],
      columns1:[
        {
            title: '序号',
            type: 'index',
            width: 80,
            align: 'center'
        }, 
          {
          title: '加班内容',
          key: 'content'
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
          content:''
      },
      ruleValidate: {
          content: [
          { required: true, message: '内容不能为空', trigger: 'blur' }
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
        var countId = this.data1[index].id;
          this.$Modal.confirm({
              title: '确认',
              content: '确认要删除此项内容吗？',
              onOk:function(){
                  axios({
                    method:'post',
                    url:'<%=ctxPath%>/jb/config/content/delworkcontent/api',
                    params:{
                        contentId:countId
                    },
                    responseType:'json'
                  }).then((response) => {
                      var result = response.data;
                      if(!result.flag){
                          this.$Message.error({
                              top: 50,
                              duration: 10,
                          content: '该内容已有级联关系不能直接删除。',
                          closable: true
                        });
                      }else{
                          myVue.data1.splice(index, 1);
                      }
                  });
              }
          });
//           this.data1.splice(index, 1);
        }
    },
    created: function () {
      axios({
        method:'get',
        url:'<%=ctxPath%>/jb/config/content/allworkcontent/api',
        responseType:'json'
      }).then((response) => {
        this.data1=response.data.data;
      });
      axios({
        method:'get',
        url:'/system/getorganization/api',
        responseType:'json'
      }).then((response) => {
        this.orgs=response.data.data;
      });
      console.log("内容初始化--加载中...TODO")
    }
});
</script>
