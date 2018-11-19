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
            <breadcrumb-item>字典管理</breadcrumb-item> 
            <breadcrumb-item>上传字典</breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
          <div style="width:100%;">
            <div style="width: 600px;margin-left: auto;margin-right: auto;">
            	<Upload type="drag" name="upFile" action="<%=ctxPath%>/htgl/dicinfocontroller/uploaddicinfo">
        			<div style="padding: 20px 0">
            			<Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
            			<p>Click or drag files here to upload</p>
        				</div>
    			</Upload>
    			
    			<%-- <Upload type="drag" name="upFile" :before-upload="handleUpload" action="<%=ctxPath%>/htgl/dicinfocontroller/uploaddic">
        			<div style="padding: 20px 0">
            			<Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
            			<p>Click or drag files here to upload</p>
        				</div>
    			</Upload>
    			<div v-if="file !== null">Upload file: {{ file.name }} <Button type="text" @click="upload" :loading="loadingStatus">{{ loadingStatus ? 'Uploading' : 'Click to upload' }}</Button></div>
    			 --%>
            </div>
          </div>
        </i-content>
        
    </Layout>
</div>


<script type="text/javascript">
var myVue =  new Vue({
	  el: '#app_content',
	  data:{
		  file: null,
          loadingStatus: false,
	  },
	  methods:{
	        handleUpload:function (file) {
                this.file = file;
                return false;
            },
            upload:function () {
                this.loadingStatus = true;
                setTimeout(() => {
                    this.file = null;
                    this.loadingStatus = false;
                    this.$Message.success('Success')
                }, 1500);
            }
	  }
});
</script>