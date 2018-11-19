<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%
String ctxPath = request.getContextPath();
%>
<style scoped>
.time{
    font-size: 14px;
    font-weight: bold;
}
.content{
    padding-left: 5px;
}
</style>
<div id="app_content" >
    <Layout :style="{padding: '0 20px 20px'}"> 
        <Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item><a href="<%=ctxPath%>/"><Icon v-bind:type="homeIcon.value"></Icon></a></breadcrumb-item>
            <breadcrumb-item>欢迎</breadcrumb-item> 
        </Breadcrumb> 
        
        <i-content :style="{padding: '24px', minHeight: '428px', background: '#fff'}">
        	
        	<div class="filter-box clearfix">
                <div>welcome</div>
            </div>
        	
        </i-content>
        
    </Layout>
</div>


<script>
var myVue = new Vue({
    el: '#app_content',
    data:{
    	homeIcon:{
    		name:'',
    		value:''
    	},
    },
    created: function () {
    	axios.all([
    	    axios.get('<%=ctxPath %>/htgl/iconinfocontroller/geticoninfobyname/api', {
    	    	params : { 
    	    		name : 'home'
    	    	}
    	    })
    	  ]).then(axios.spread(function (iconinfoResp) {
    		  if(iconinfoResp.data.flag){
    			  myVue.homeIcon = iconinfoResp.data.data;
    		  }else{
    			  myVue.homeIcon.name = 'md-home';
    		  }
    	  }));
    },
    methods: {
       }
});
</script>
