<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<div id="app_content" v-cloak>
    <Layout> 
        <Breadcrumb> 
            <breadcrumb-item><a href="/">首页</a></breadcrumb-item> 
            <breadcrumb-item>请假管理</breadcrumb-item> 
            <breadcrumb-item><a href="<%=ctxPath%>/leave/application/index">请假单收取</a></breadcrumb-item> 
        </Breadcrumb>
        <i-content>
            <Alert show-icon>
            {{infoContent}}
            带有“签字”标识的请假单需要总经理审批。
            </Alert>
            <i-form method="post" inline action="" :model="formParam">
                <form-item prop="serial">
                    <i-input name="serial" clearable v-model="formParam.serial" placeholder="编号" style="width: 150px;"></i-input>
                </form-item>
                <form-item prop="orgId">
                    <i-select name="orgId" v-model="formParam.orgId" style="width: 150px;" @on-change="getEmployees" placeholder="部门">
                        <i-option v-bind:value="org.id" v-for="org in orgsParam" :key="org.id">
                            {{org.name}}
                        </i-option>
                    </i-select>
                </form-item>
                <form-item prop="empId">
                    <i-select name="empId" clearable v-model="formParam.empId" style="width: 150px;" placeholder="姓名">
                            <i-option v-bind:value="user.id" v-for="user in empParam" :key="user.id">
                                {{user.name}}
                            </i-option>
                    </i-select>
                </form-item>
                <form-item prop="createtime">
                    <Date-picker type="daterange" name="createtime" :start-date="startDate" v-model="formParam.createtime" format="yyyy-MM-dd" placeholder="申请日期" style="width: 200px">
                    </Date-picker>
                </form-item >
                <form-item prop="status">
                    <i-select name="status" v-model="formParam.status" style="width: 150px;" placeholder="请选择状态">
                        <i-option v-bind:value="checkStatus.index" v-for="checkStatus in statusParam" :key="checkStatus.index">
                            {{checkStatus.name}}
                        </i-option>
                    </i-select>
                </form-item>
                <form-item>
                    <i-button type="primary" @click="search()" >查询</i-button>
                </form-item>
            </i-form>

            <div class="creat-leave-btn-top">
                <i-button type="primary" @click="auditAll()">批量收取</i-button>
            </div>
            <i-table :row-class-name="rowClassName" border ref="selection" :columns="qjColumns" :data="qjListData" @on-selection-change="selectChange"></i-table>
            <div class="creat-leave-btn-bottom">
                <i-button type="primary" @click="auditAll()">批量收取</i-button>
            </div>
            <div style="margin-top:15px;">
                <Page :total="pageParam.totalCount" :current="pageParam.currentPage" :page-size="pageParam.pageCount" @on-change="searchPage" @on-page-size-change="changePageSize" show-total show-sizer show-elevator/>
            </div>
            
            <!-- 显示请假单详情 Modal -->
            <Modal v-model="showDetail" :title="entityDetail.serial" width="800">
                <div class="qj-detail-box">
                    <h2 class="qj-detail-title">请假申请单</h2>
                    <table class="qj-detail-table">
                        <tr>
                            <td class="left-td">姓名</td>
                            <td>{{entityDetail.realName}}</td>
                            <td class="left-td">部门</td>
                            <td>{{entityDetail.orgName}}</td>
                            <td class="left-td">职务</td>
                            <td>{{entityDetail.workRelation}}</td>
                        </tr>
                        <tr>
                            <td colspan="6">
                                <div class="detail-info-box">
                                    <p>
                                        休假总天数：{{entityDetail.totalDay}} 天&ensp;&ensp;&ensp;&ensp;休假总次数：{{entityDetail.totalTime}} 次
                                    </p>
                                    <div class="qj-list" v-for="entity in entityDetail.detail" >
                                        {{entity.xjlx}}：{{entity.totalDay}} 天&ensp;&ensp;&ensp;&ensp;休假次数：{{entity.totalTime}}次
                                        <div v-for="entity in entity.detail">
                                            {{entity}}
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="6">
                            申请日期 :  {{entityDetail.applyDate}}
                            </td>
                        </tr>
                    </table>
                </div>
                <div slot="footer">
                    <i-button type="primary" @click="reportEntity" v-if="entityDetail.leaveStatus==10">收取</i-button>
                    <i-button type="error" @click="abateEntity">废除</i-button>
                    <i-button type="text" @click="showDetail=false">取消</i-button>
                </div>
            </Modal>

            <!-- 废除modal -->
            <Modal v-model="abolishReasonModal" :title="abolishTitle" width="500">
                <div>
                    <i-form>
                        <form-item>
                            <i-input v-model="reasonTxt" type="textarea" :autosize="{minRows: 3,maxRows: 5}" :maxlength="50" placeholder="请填写废除原因..."/>
                        </form-item>
                    </i-form>
                </div>
                <div slot="footer">
                    <i-button type="text" @click="cancelReason">取消</i-button>
                    <i-button type="primary" @click="saveReason">确定废除</i-button>
                </div>
            </Modal>
        </i-content>
    </Layout>
</div>
<script type="x-template" id="detail">
<div>
    <Row class="expand-row">
        <i-col span="24">
            <span class="expand-key">废除原因: </span>
            <span class="expand-value">{{ row.fcyy }}</span>
        </i-col>
    </Row>
</div>
</script>
<script>
var unBillCount = '<%=request.getAttribute("unBillCount") %>' ;
Vue.component('newRow', {
    props: ['row'],
    template: '#detail'
  });
    var myVue = new Vue({
        el: '#app_content',
        data:{
            qjListData: [],
            formParam: {
                serial: "",
                orgId:'',
                empId:'',
                status:'',
                createtime:''
            },
            orgsParam: [],
            empParam: [],
            statusParam: [],
            ids:[],
            startDate:getPreDate(),
            pageParam:{
                currentPage:1,
                pageCount:20,
                totalCount:0
            },
            showDetail: false,
            entityDetail:'',
            detailId:'',
            abolishTitle:'',
            infoContent:unBillCount,
            expandFlag: false,
            abolishReasonModal: false,
            unpassIndex: "",
            reasonTxt: ""
        },
        computed: {
            qjColumns: function() {
                var qjColumnsData = [
                    {
                        type: "selection",
                        width: 50,
                        align: "center"
                    },
                    {
                        title: '序号',
                        type:"index",
                        align: "center",
                        width: 50
                    },
                    {
                        title: "部门",
                        key: "orgName",
                        width: 120,
                        sortable:true
                    },
                    {
                        title: "姓名",
                        key: "empName",
                        width: 120,
                        sortable:true
                    },
                    {
                        title: "编号",
                        key: "serial",
                        minWidth: 145,
                        render: function(h, params) {
                            var status = myVue.qjListData[params.index].status;
                            if(status != 25){
                                return h("div", [
                                    h('a', {
                                        on: {
                                            click: function () {
                                                myVue.showReportDetail(params.index);
                                            }
                                        }
                                    }, params.row.serial),
                                    h("Tooltip", {
                                        props: {
                                            placement: "bottom-start",
                                            content: "事假3天以上（含3天）、陪产假、探亲假需报总经理批准"
                                        }
                                    }, [
                                        h("Tag", {
                                            props: {
                                                color: "yellow"
                                            },
                                            style: {
                                                display: myVue.setLdqzDisplay(params.row.ldqz),
                                                marginLeft: "10px"
                                            }
                                        }, myVue.setLdqz(params.row.ldqz))
                                    ])
                                ])
                            }else{
                                return h('div',[
                                    h("span", params.row.serial),
                                    h("Tag", {
                                        props: {
                                            color: "yellow"
                                        },
                                        style: {
                                            display: myVue.setLdqzDisplay(params.row.ldqz),
                                            marginLeft: "10px"
                                        }
                                    }, myVue.setLdqz(params.row.ldqz))
                                ])
                            }
                        }
                    },
                    {
                        title: "请假总天数(天=小时/8)",
                        key: "totalHours",
                        width: 200,
                        align: "center",
                        sortable:true
                    },
                    {
                        title: "请假总次数",
                        key: "leaveNums",
                        width: 120,
                        align: "center",
                        sortable:true
                    },
                    {
                        title: "日期",
                        key: "createTime",
                        align: "center",
                        width: 130,
                        sortable:true
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 150,
                        align: 'center',
                        render: function(h, params) {
                            var status = myVue.qjListData[params.index].status,passFlag,unpassDisabled;
                            if(status != 10){
                                passFlag = 'disabled';
                            }
                            if(status == 25){
                                unpassDisabled = 'disabled';
                            }
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'primary',
                                        size: 'small',
                                        disabled:passFlag
                                    },
                                    style: {
                                        marginRight: '10px'
                                    },
                                    on: {
                                        click: function () {
                                            myVue.checkPass(params.index)
                                        }
                                    }
                                }, '收取'),
                                h('Button', {
                                    props: {
                                        type: 'error',
                                        size: 'small',
                                        disabled:unpassDisabled
                                    },
                                    on: {
                                        click: function() {
                                            myVue.checkUnPass(params.index)
                                        }
                                    }
                                }, '废除')
                            ]);
                        }
                    }
                ];
                var expandFlag = this.expandFlag;
                if (expandFlag) {
                    var expandRowColumn = {
                        type: 'expand',
                        width: 50,
                        render: function (h, params) {
                            return h("new-row", {
                                props: {
                                  row: params.row
                                }
                            })
                        }
                    }
                    qjColumnsData.splice(1, 0, expandRowColumn);
                }
                return qjColumnsData;
            }
        },
        created: function () {
            axios.all([
                axios.get('<%=ctxPath %>/leave/report/getentitys/api'),
                axios.get('<%=ctxPath %>/leave/report/getorgs/api'),
                axios.get('<%=ctxPath %>/leave/report/getemps/api'),
                axios.get('<%=ctxPath %>/leave/report/getstatus/api')
              ]).then(axios.spread(function (leaveAppResp,orgResp,empsResp,statusResp) {
                  myVue.qjListData = leaveAppResp.data.data.result;
                  flushPage(leaveAppResp.data.data);
                  flushExpandFlag(leaveAppResp.data.data.result);
                  myVue.orgsParam = orgResp.data.data;
                  if(orgResp.data.data.length==1){
                      myVue.formParam.orgId = orgResp.data.data[0].id;
                  }
                  myVue.empParam = empsResp.data.data;
                  myVue.statusParam = statusResp.data.data;
                  myVue.formParam.status = '<%=dsq%>';
              }));
        },
        methods: {
            //点查询
            search: function() {
                var param = new URLSearchParams(); 
                param.append("createtime",myVue.formParam.createtime);
                param.append("empId",myVue.formParam.empId); 
                param.append("orgId",myVue.formParam.orgId); 
                param.append("status",myVue.formParam.status); 
                param.append("serial",myVue.formParam.serial); 
                param.append("pageSize",myVue.pageParam.pageCount); 
                param.append("currentPage",1); 
                axios.post('<%=ctxPath %>/leave/report/getentitys/api', param)
                    .then(function (response) {
                        if(response.data.flag){
                            myVue.qjListData = response.data.data.result;
                            flushPage(response.data.data);
                            flushExpandFlag(response.data.data.result);
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
            //单条收取
            checkPass:function(index){
                this.$Modal.confirm({
                    title: myVue.qjListData[index].empName,
                    content: "确定收取？",
                    onOk:function(){
                        var param = new URLSearchParams(); 
                        param.append("id",myVue.qjListData[index].id);
                        param.append("createtime",myVue.formParam.createtime);
                        param.append("empId",myVue.formParam.empId); 
                        param.append("orgId",myVue.formParam.orgId); 
                        param.append("status",myVue.formParam.status); 
                        param.append("serial",myVue.formParam.serial); 
                        param.append("pageSize",myVue.pageParam.pageCount); 
                        param.append("currentPage",myVue.pageParam.currentPage);
                        axios.post('<%=ctxPath %>/leave/report/pass/api', param)
                             .then(function (response) {
                                if(response.data.flag){
                                    myVue.$Message.success({
                                        content: '收取成功',
                                        duration: 3,
                                        closable: true
                                      });
                                    myVue.qjListData=response.data.data.result;
                                    flushPage(response.data.data);
                                    flushExpandFlag(response.data.data.result);
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
            //废除
            checkUnPass:function(index){
                this.abolishReasonModal = true;
                this.unpassIndex = index;
                myVue.detailId = '';
                myVue.abolishTitle = myVue.qjListData[index].empName;
            },
            //保存废除意见
            saveReason: function() {
                var _this = this;
                var id = myVue.detailId;
                if(''==id){
                    id = myVue.qjListData[_this.unpassIndex].id;
                }
                var param = new URLSearchParams(); 
                param.append("id",id);
                param.append("createtime",myVue.formParam.createtime);
                param.append("empId",myVue.formParam.empId); 
                param.append("orgId",myVue.formParam.orgId); 
                param.append("status",myVue.formParam.status); 
                param.append("serial",myVue.formParam.serial); 
                param.append("bmyj",myVue.reasonTxt); 
                param.append("pageSize",myVue.pageParam.pageCount); 
                param.append("currentPage",myVue.pageParam.currentPage);
                axios.post('<%=ctxPath %>/leave/report/abate/api', param)
                     .then(function (response) {
                        if(response.data.flag){
                            myVue.$Message.success({
                                content: '废除成功',
                                duration: 3,
                                closable: true
                              });
                            myVue.qjListData=response.data.data.result;
                            flushPage(response.data.data);
                            flushExpandFlag(response.data.data.result);
                            flushIds();
                            flushAuditReasonModal();
                            flushDetails();
                            myVue.$forceUpdate();
                        }else{
                            myVue.$Message.warning({
                                content: response.data.errorMessages,
                                duration: 3,
                                closable: true
                            });
                            flushAuditReasonModal();
                        }
                    })
            },
            cancelReason:function(){
                flushAuditReasonModal();
                flushDetails();
            },
            //批量收取
            auditAll: function() {
               if(myVue.ids.length == 0){
                    myVue.$Message.warning({
                        content: '请选择数据',
                        duration: 3,
                        closable: true
                    });
                    return;
                }
                var count = myVue.ids.length;
                this.$Modal.confirm({
                    title: '提示',
                    content: "共 "+ count+" 条记录，确定批量收取？",
                    onOk:function(){
                        var param = new URLSearchParams(); 
                        param.append("ids",myVue.ids); 
                        param.append("createtime",myVue.formParam.createtime);
                        param.append("empId",myVue.formParam.empId); 
                        param.append("orgId",myVue.formParam.orgId); 
                        param.append("status",myVue.formParam.status); 
                        param.append("serial",myVue.formParam.serial); 
                        param.append("pageSize",myVue.pageParam.pageCount); 
                        param.append("currentPage",myVue.pageParam.currentPage);
                        axios.post('<%=ctxPath %>/leave/report/batchpass/api', param)
                            .then(function (response) {
                                if(response.data.flag){
                                    myVue.$Message.success({
                                        content: '批量收取成功',
                                        duration: 3,
                                        closable: true
                                      });
                                      myVue.qjListData=response.data.data.result;
                                      flushPage(response.data.data);
                                      flushExpandFlag(response.data.data.result);
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
                    }
                });
            },
            //勾选
            selectChange: function(selections) {
                myVue.ids = [];
                for (var i=0;i<selections.length;i++){
                    myVue.ids.push(selections[i].id);
                }
            },
            //分页查询，点击上一页、下一页、数字
            searchPage:function (page) {
               var param = new URLSearchParams(); 
               param.append("createtime",myVue.formParam.createtime);
               param.append("empId",myVue.formParam.empId); 
               param.append("orgId",myVue.formParam.orgId); 
               param.append("status",myVue.formParam.status); 
               param.append("serial",myVue.formParam.serial); 
               param.append("pageSize",myVue.pageParam.pageCount); 
               param.append("currentPage",page); 
               axios.post('<%=ctxPath %>/leave/report/getentitys/api', param)
                   .then(function (response) {
                       if(response.data.flag){
                           myVue.qjListData=response.data.data.result;
                           flushPage(response.data.data);
                           flushExpandFlag(response.data.data.result);
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
            //调整每页大小时
            changePageSize:function(pageSize){
                var param = new URLSearchParams(); 
                param.append("createtime",myVue.formParam.createtime);
                param.append("empId",myVue.formParam.empId); 
                param.append("orgId",myVue.formParam.orgId); 
                param.append("status",myVue.formParam.status); 
                param.append("serial",myVue.formParam.serial); 
                param.append("pageSize",pageSize); 
                param.append("currentPage",1); 
                myVue.pageParam.pageCount = pageSize;
                myVue.pageParam.currentPage = 1;
                axios.post('<%=ctxPath %>/leave/report/getentitys/api', param)
                    .then(function (response) {
                        if(response.data.flag){
                            myVue.qjListData=response.data.data.result;
                            flushPage(response.data.data);
                            flushExpandFlag(response.data.data.result);
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
            //组织 员工级联
            getEmployees:function(orgId){
                var param = new URLSearchParams(); 
                param.append("orgId",orgId); 
                axios.post('<%=ctxPath %>/leave/report/getempsbyorgid/api', param).then(function (response) {
                    if(response.data.flag){
                        myVue.empParam = response.data.data;
                        myVue.formParam.empId='';
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
            showReportDetail: function(index) {
                flushDetails();
                if('25'==myVue.qjListData[index].status){
                    myVue.$Message.warning({
                        content: "此单已废除，不能查看",
                        duration: 3,
                        closable: true
                    });
                    return ;
                }
                var param = new URLSearchParams(); 
                param.append("id",myVue.qjListData[index].id); 
                axios.post('<%=ctxPath %>/leave/report/getentitybyid/api', param)
                    .then(function (response) {
                        if(response.data.flag){
                            myVue.entityDetail = response.data.data;
                            myVue.detailId = myVue.qjListData[index].id;
                            myVue.showDetail = true;
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
            reportEntity: function() {
                this.$Modal.confirm({
                    title: '提示',
                    content: "确定收取？",
                    onOk:function(){
                        var param = new URLSearchParams(); 
                        param.append("id",myVue.detailId);
                        param.append("createtime",myVue.formParam.createtime);
                        param.append("empId",myVue.formParam.empId); 
                        param.append("orgId",myVue.formParam.orgId); 
                        param.append("status",myVue.formParam.status); 
                        param.append("serial",myVue.formParam.serial); 
                        param.append("pageSize",myVue.pageParam.pageCount); 
                        param.append("currentPage",myVue.pageParam.currentPage);
                        axios.post('<%=ctxPath %>/leave/report/pass/api', param)
                             .then(function (response) {
                                if(response.data.flag){
                                    myVue.$Message.success({
                                        content: '收取成功',
                                        duration: 3,
                                        closable: true
                                      });
                                    myVue.qjListData=response.data.data.result;
                                    flushPage(response.data.data);
                                    flushExpandFlag(response.data.data.result);
                                    flushIds();
                                    flushDetails();
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
            },
            abateEntity: function() {
                this.abolishReasonModal = true;
                myVue.abolishTitle = myVue.entityDetail.realName;
            },
            rowClassName: function(row, index) {
                if (row.color === 'info') {
                    return 'demo-table-dark-row';
                }else{
                    return '';
                }
            },
            setLdqz: function(ldqzFlag) {
                if (ldqzFlag) {
                    return "签字";
                }
            },
            setLdqzDisplay: function(ldqzFlag) {
                if (ldqzFlag) {
                    return "inline-block";
                } else {
                    return "none";
                }
            }
        }
    });
    
//刷新分页信息
function flushPage(page){
    myVue.pageParam.totalCount = page.totalCount;
    myVue.pageParam.pageCount = page.pageCount;
    myVue.pageParam.currentPage = page.currentPage;
};
//置空被选择的checkbox
function flushIds(){
    myVue.ids =[];
}
//置空弹出层
function flushDetails(){
    myVue.entityDetail = '';
    myVue.detailId = '';
    myVue.showDetail = false;
}
//刷新审核意见弹出层
function flushAuditReasonModal(){
    myVue.abolishReasonModal = false;
    myVue.unpassIndex = '';
    myVue.reasonTxt = '';
}
//有收取意见时，显示
function flushExpandFlag(result){
    myVue.expandFlag = false;
    if(null != result){
        for(data in result){
            if(null != result[data].fcyy){
                myVue.expandFlag = true;
                return;
            }
        }
    }
}
</script>
