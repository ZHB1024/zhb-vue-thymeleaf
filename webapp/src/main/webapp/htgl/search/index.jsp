<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
String ctxPath = request.getContextPath();
%>

<style>
li {list-style-type:none;}
</style>

<div id="app_content" style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item><a href="">首页</a></breadcrumb-item> 
            <breadcrumb-item>搜索管理</breadcrumb-item> 
            <breadcrumb-item>搜索信息</breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
          <i-form inline ref="formInline" method="post" action="" >
          		<form-item prop="fileName">
                  		<i-input type="text" clearable name="fileName" v-model="formParm.fileName" placeholder="请输入附件名称"></i-input>
                </form-item >
                	
        		<form-item prop="type">
        			<i-select name="type" clearable v-model="formParm.type" style="width: 150px;" placeholder="请选择附件类别">
                        	<i-option v-bind:value="item.value" :key="item.value" v-for="item in typeParm">
                        		{{item.key}}
                        	</i-option>
                	</i-select>
                </form-item>
                
                <form-item>
                    	<i-button type="primary" @click="solrSearch()" > solrSearch </i-button>
                    	<i-button type="primary" @click="elasticSearch()" > elasticSearch </i-button>
                    	<i-button type="primary" @click="luceneSearch()" > luceneSearch </i-button>
                </form-item>
        	</i-form>
			<i-table border :columns="columns1" :data="tableDatas"></i-table> 
        	<Page :total="pageParm.totalCount" :current="pageParm.currentPage" :page-size="pageParm.pageCount" @on-change="searchPage" @on-page-size-change="changePageSize" show-total show-sizer show-elevator/>
       </i-content>
        
    </Layout>
</div>


<script type="text/javascript">
var myVue = new Vue({
    el: '#app_content',
    data:{
    	typeParm:[] ,
    	formParm:{
    		fileName:'',
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
            	title: '附件', 
            	key: 'thumbnailUrl',
            	width: 100,
            	render: (h, params) => {
            	    return h('div',  [
            	        h('img', {
            	          props: {
            	            type: 'primary',
            	            size: 'small'
            	          },
                          style: {
                              marginRight: '10px'
                          },
            	          attrs: {
            	            src: params.row.thumbnailUrl, style: 'width: 40px;height: 40px;border-radius: 2px;'
            	          },
                          on: {
                              click: () => {
                              	  myVue.showOriginal(params)
                              }
                          }
            	        }),
            	        h('Icon', {
              	          props: {
              	            type: 'md-arrow-down',
              	            color:'green'
              	          },
                            on: {
                                click: () => {
                                	  myVue.downAttachment(params)
                                }
                            }
              	        })
            	      ]);
            	  }
            },
            {
                title: '附件名称',
                key: 'fileName',
                minWidth: 100,
                sortable:true,
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
                        }, params.row.fileName)
                    ]);
                }
            },
            {
                title: '附件类型',
                key: 'type',
                minWidth: 100
            },
            {
                title: '创建时间',
                key: 'createTime',
                minWidth: 100,
                sortable:true
            },
            {
                title: '状态',
                key: 'deleteFlagName',
                minWidth: 50,
                sortable:true
            },
            {
                title: '喜爱程度',
                key: 'likeDegree',
                minWidth: 150,
                align: "center",
                render: (h, params) => {
                	var colorFlag = 'green';
                	var leftEditorFlag,rightEditorFlag ;
                	if('1' == params.row.likeDegree){
                		leftEditorFlag = 'disabled';
                	}
                	if('2' == params.row.likeDegree){
                		colorFlag = 'blue';
                	}
                	if('3' == params.row.likeDegree){
                		colorFlag = 'red';
                		rightEditorFlag = 'disabled';
                	}
                	return h('div', [
                        h('Button', {
                            props: {
                              slot: 'append',
                              disabled:leftEditorFlag
                            },
                            class: "left-btn",
                            on: {
                              click: () => {
                                this.myVue.upLikeDegree(params.index,-1);
                              }
                            }
                        }, "-"),
                        h('Icon',{
                          class: "cbcs",
                          props: {
                              type: params.row.likeDegreeName,
                              color:colorFlag,
                              size:20
                          }
                        }, ''),
                        h('Button', {
                            props: {
                              slot: "prepend",
                              disabled:rightEditorFlag
                            },
                            class: "right-btn",
                            on: {
                              click: () => {
                                this.myVue.upLikeDegree(params.index,1);
                              }
                            }
                        }, "+")
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
    	    axios.get('<%=ctxPath %>/htgl/attachmentinfocontroller/getattachmenttype/api')
    	  ]).then(axios.spread(function (typeResp) {
    		  myVue.typeParm = typeResp.data.data;
    	  }));
    },
    methods: {
    	solrSearch:function() {
    		let param = new URLSearchParams(); 
      	  	if(undefined == myVue.formParm.type){
      		  	myVue.formParm.type = "";
      	  	}
      	  	param.append("fileName",myVue.formParm.fileName); 
      	  	param.append("type",myVue.formParm.type);
      	  	param.append("pageSize",myVue.pageParm.pageCount); 
     	   	param.append("currentPage",1); 
     	  	axios.post('<%=ctxPath %>/htgl/searchController/solrsearch/api', param)
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
        elasticSearch:function() {
        	let param = new URLSearchParams(); 
     	  	axios.post('<%=ctxPath %>/htgl/searchController/elasticsearch/api', param)
     		  	.then(function (response) {
     			  	if(response.data.flag){
     			  		myVue.$Message.success({
                            content: response.data.data,
                            duration: 3,
                            closable: true
                        });
                   }else{
                 	  myVue.$Message.error({
                           content: response.data.errorMessages,
                           duration: 3,
                           closable: true
                       });
                   }
     		  })
        },
        luceneSearch:function() {
        	let param = new URLSearchParams(); 
     	  	axios.post('<%=ctxPath %>/htgl/searchController/lucenesearch/api', param)
     		  	.then(function (response) {
     			  	if(response.data.flag){
     			  		myVue.$Message.success({
                            content: response.data.data,
                            duration: 3,
                            closable: true
                        });
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