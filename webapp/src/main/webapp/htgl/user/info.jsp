<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
String ctxPath = request.getContextPath();
%>
<style> 
p{
margin-top:20px;
margin-left:30px;
}
</style>

<div id="app_content" style="height: 100%">
    <Layout :style="{padding: '0 24px 24px', height: '100%'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item>首页</breadcrumb-item> 
            <breadcrumb-item>用户管理</breadcrumb-item> 
            <breadcrumb-item>个人信息</breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
            <Row style="background:#eee;padding:20px">
              <i-col span="20">
                  <Card :bordered="false">
                      <p slot="title">
                      个人信息
                      <a href="<%=ctxPath%>/htgl/userinfocontroller/toupdate" slot="extra" >
			            <Icon type="ios-create" size="24"/>
			          </a>
                      </p>
                      
			          
                      <Row>
                        <i-col span="15">
                          <p>用户名： {{userInfo.userName}}</p>
                          <p>姓名： {{userInfo.realName}}</p>
                          <p>身份证号： {{userInfo.identityCard}}</p>
                          <p>性别： {{userInfo.sex}}</p>
                          <p>出生日期： {{userInfo.birthday}}</p>
                          <p>国家/地区： {{userInfo.country}}</p>
                          <p>民族： {{userInfo.nation}}</p>
                          <p>毕业院校： {{userInfo.byyx}}</p>
                          <p>电话： {{userInfo.mobilePhone}}</p>
                          <p>邮箱： {{userInfo.email}}</p>
                          <p>密码：*********
                          	<a  href="<%=ctxPath%>/htgl/userinfocontroller/tomodifypassword">
                          		<Icon type="ios-create" size="15"/>
                          	</a>
                          </p>
                        </i-col>
                        
                        <i-col span="5">
                          <p style="margin-top:50px;margin-left: 50px;">
                          	<img :src="userInfo.headSrc" width="150" height="150" @click="showHead"/>
                          </p>
                          <p style="margin-top:50px">
                          	<Upload 
            					ref="upload" 
            					type="drag" 
            					name="upFile" 
            					:max-size="20480" 
            					:before-upload="beforeUpload"
            					:show-upload-list="false" 
            					action=""
            					>
        						<i-button type="primary">
                          			上传新头像
                          		</i-button>
    						</Upload>
                          </p>
                        </i-col>
                        
                      </Row>
                  </Card>
              </i-col>
          </Row>
        </i-content>
        
    </Layout>
</div>

<script>
var myVue =  new Vue({
	  el: '#app_content',
	  data:{
		  files: [],
		  userInfo:{
			  id:'',
			  userName:'',
			  realName:'',
			  identityCard:'',
			  sex:'',
			  birthday:'',
			  country:'',
			  nation:'',
			  byyx:'',
			  mobilePhone:'',
			  email:'',
			  headSrc:''
		  },
	  },
	  created: function () {
		  axios.all([
	    	    axios.get('<%=ctxPath %>/htgl/userinfocontroller/getselfinfo/api')
	    	  ]).then(axios.spread(function (userInfoResp) {
	    		  myVue.userInfo = userInfoResp.data.data;
	    		  myVue.userInfo.headSrc = '<%=ctxPath%>/htgl/attachmentinfocontroller/getoriginalattachmentinfo?id=' + userInfoResp.data.data.lobId;
	    	  }));
	 },
	  methods:{
		  beforeUpload:function (file) {
			  myVue.files = file;
			  var url = getObjectURL(file);
			  
			  var me_image = generatorLayerContent(url);
			  popup('上传新头像',me_image);
			  
		      <%-- let param = new URLSearchParams(); 
	    	  param.append("image",me_image); 
	    	  param.append("id",myVue.userInfo.id); 
	    	  axios.post('<%=ctxPath %>/htgl/attachmentinfocontroller/getlayercontent/api', param)
	    		  .then(function (response) {
	    			  if(response.data.flag){
	  		            popup(response.data.data);
	                  }else{
	                	  myVue.$Message.warning({
	                          content: response.data.errorMessages,
	                          duration: 3,
	                          closable: true
	                      });
	                  }
	    		  }) --%>
         	 return false;// 返回 falsa 停止自动上传 我们需要手动上传
         },
         showHead:function(){
        	 var head_image = '<img id="head_image" src="' + myVue.userInfo.headSrc + '">';
        	 popup('头像',head_image);
         }
	  }
});

function generatorLayerContent(img) {
	var sb ='';
	sb += '<div class="box">';
    sb +=   '<img id="new_me_image" src="' + img + '" width="800px" height="800px"/>';
    sb += '</div>';
    sb += '<div align="center" style="margin-top:10px;margin-bottom:5px;" > ';
    sb +=      '<input type="button" id="cut_upload" value="上传"  style="width:80px;height:30px;" />';
    sb +=      '<input type="button" id="cut_cancel" value="取消" style="width:80px;height:30px;" />';
    sb += '</div>';
    
    sb += '<script type="text/javascript">';
    /* https://blog.csdn.net/weixin_38023551/article/details/78792400 */
    sb += '     $("#new_me_image").cropper({';
    sb += '             aspectRatio: 3 / 2,';
    sb += '             viewMode:3,';
    sb += '             guides:false,';
    sb += '             modal:false,';
    sb += '             crop: function (e) {';
    sb += '             }';
    sb += '     });';
    sb += '     $("#cut_upload").on("click", function () {';
    sb += '          let formData = new FormData();';
    sb += '          var image_target = $("#new_me_image").cropper("getData", true); ';
    sb += '          var image_content = $("#new_me_image").attr("src");';
    sb += '          var userId = $("#userId").val();';
    
    sb += '          formData.append("userId", myVue.userInfo.id);';
    sb += '          formData.append("upFile", myVue.files);';
    sb += '          formData.append("image_content", image_content);';
    sb += '          formData.append("x", image_target.x);';
    sb += '          formData.append("y", image_target.y);';
    sb += '          formData.append("width", image_target.width);';
    sb += '          formData.append("height", image_target.height);';
    
    sb += '          $.ajax({';
    sb += '             url: "/htgl/attachmentinfocontroller/uploadHeadPhoto/api",';
    sb += '             type: "POST",';
    sb += '             data:formData,';
    sb += '             processData: false,';
    sb += '             contentType: false,';
    sb += '             success: function (result) { ';
    sb += '                 if(result.flag){';
    sb += '                 	layer.closeAll(); ';
    sb += '                 	window.location.reload() ; ';
    sb += '                 }else{';
    sb += '                 	alert(result.errorMessages)';
    sb += '                 }';
    sb += '             }, ';
    sb += '             error: function (result) {';
    sb += '                 layer.closeAll(); ';
    sb += '             }';
    sb += '          });';

    sb += '     });';
    sb += '     $("#cut_cancel").on("click", function () {';
    sb += '          layer.closeAll(); ';
    sb += '          window.location.reload() ; ';
    sb += '     });';
    sb += '</scr'+'ipt>';
    return sb;
}

function getObjectURL (file) {
    let url = null ;
    if (window.createObjectURL!=undefined) { // basic
      url = window.createObjectURL(file) ;
    }else if (window.webkitURL!=undefined) { // webkit or chrome
      url = window.webkitURL.createObjectURL(file) ;
    }else if (window.URL!=undefined) { // mozilla(firefox)
      url = window.URL.createObjectURL(file) ;
    }
    return url ;
}
 /*0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）*/
function popup(title,content) {
    layer.open({
        title: title,
        type: 1,
        //skin: 'layui-layer-rim', //加上边框
        area: ['900px', '950px'], //宽高
        content: content,
        //btn: ['确定','取消'],
        success: function(layero, index){
            layer.iframeAuto(index);
        },
        yes: function(index, layero){
            layer.closeAll();
        }
    });
}
</script>