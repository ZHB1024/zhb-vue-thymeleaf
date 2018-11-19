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
.swiper-container {
    width: 100%;
    height: 700px;
}  
</style>
<div id="app_content" >
    <Layout :style="{padding: '0 20px 20px'}"> 
    	<Breadcrumb :style="{margin: '24px 0'}"> 
            <breadcrumb-item><a href="<%=ctxPath%>/"><Icon type="md-home"></Icon></a></breadcrumb-item>
            <breadcrumb-item>附件管理</breadcrumb-item> 
            <breadcrumb-item>浏览图片</breadcrumb-item> 
        </Breadcrumb> 
    	<i-content :style="{padding: '24px', background: '#fff'}">
    		<div class="swiper-container">
    			<div class="swiper-wrapper">
    				<div class="swiper-slide" v-for="item in imgList" data-swiper-autoplay="1000" style="overflow-y:auto;">
    			  			<img :src="item" >
    				</div>
    			</div>
    			<div class="swiper-button-prev"></div>
    			<div class="swiper-button-next"></div>
			</div>
			<Page :total="pageParm.totalCount" :current="pageParm.currentPage" :page-size="pageParm.pageCount" @on-change="searchPage" @on-page-size-change="changePageSize" show-total show-sizer show-elevator/>
    	</i-content>
    </Layout>
</div>


<script>
var myVue = new Vue({
    el: '#app_content',
    data:{
    	imgList:[],
    	pageParm:{
	  		currentPage:1,
	        pageCount:20,
	        totalCount:0
	  	}
    },
    created: function () {
    	
    },
    methods: {
    	//点击页码查询
        searchPage:function (page) {
      	  let param = new URLSearchParams(); 
      	  param.append("type",1); 
      	  param.append("pageSize",myVue.pageParm.pageCount); 
      	  param.append("currentPage",page); 
      	  axios.post('<%=ctxPath %>/htgl/attachmentinfocontroller/scanattachmentinfopage/api', param)
      		  .then(function (response) {
      			  if(response.data.flag){
      				myVue.imgList = response.data.data.result;
      				flushPage(response.data.data);
      				myVue.$nextTick(() => {
		  		    	new Swiper('.swiper-container', {
		  		    		effect : 'coverflow',
		  		    		slidesPerView: 3,
		  		    		centeredSlides: true,
		  				   // direction: 'horizontal',
		  	    	  		nextButton: '.swiper-button-next',
		      	    		prevButton: '.swiper-button-prev',
		      	    		autoplay: {
		      	    		    delay: 3000,
		      	    		    stopOnLastSlide: true,
		      	    		    disableOnInteraction: true,
		      	    		},
		  				})
		  		  	 })
                    }else{
                  	  myVue.$Message.error({
                            content: response.data.errorMessages,
                            duration: 3,
                            closable: true
                        });
                    }
      		  })
        },
        //改变每页大小 
        changePageSize:function(pageSize){
      	  let param = new URLSearchParams(); 
      	  param.append("type",1);  
      	  param.append("pageSize",pageSize); 
      	  param.append("currentPage",1); 
      	  axios.post('<%=ctxPath %>/htgl/attachmentinfocontroller/scanattachmentinfopage/api', param)
      		  .then(function (response) {
      			  if(response.data.flag){
      				myVue.imgList = response.data.data.result;
      				flushPage(response.data.data);
      				myVue.$nextTick(() => {
		  		    	new Swiper('.swiper-container', {
		  		    		effect : 'coverflow',
		  		    		slidesPerView: 3,
		  		    		centeredSlides: true,
		  				   // direction: 'horizontal',
		  	    	  		nextButton: '.swiper-button-next',
		      	    		prevButton: '.swiper-button-prev',
		      	    		autoplay: {
		      	    		    delay: 3000,
		      	    		    stopOnLastSlide: true,
		      	    		    disableOnInteraction: true,
		      	    		},
		  				})
		  		  	 })
                    }else{
                  	  myVue.$Message.error({
                            content: response.data.errorMessages,
                            duration: 3,
                            closable: true
                        });
                    }
      		  })
        }
    },
    mounted: function (){
    	let param = new URLSearchParams(); 
    	param.append("type",1); 
 	    axios.post('<%=ctxPath %>/htgl/attachmentinfocontroller/scanattachmentinfopage/api', param)
	         .then(function (response) {
		  		if(response.data.flag){
		  			myVue.imgList = response.data.data.result;
		  			flushPage(response.data.data);
		  		  	myVue.$nextTick(() => {
		  		    	new Swiper('.swiper-container', {
		  		    		effect : 'coverflow',
		  		    		slidesPerView: 3,
		  		    		centeredSlides: true,
		  				   // direction: 'horizontal',
		  	    	  		nextButton: '.swiper-button-next',
		      	    		prevButton: '.swiper-button-prev',
		      	    		autoplay: {
		      	    		    delay: 3000,
		      	    		    stopOnLastSlide: true,
		      	    		    disableOnInteraction: false,
		      	    		},
		      	    		observer:true,//修改swiper自己或子元素时，自动初始化swiper 
		      	    		observeParents:true,//修改swiper的父元素时，自动初始化swiper 
		  				})
		  		  	})
		  			
         		}else{
       	  		myVue.$Message.error({
                 		content: response.data.errorMessages,
                 		duration: 3,
                 		closable: true
             		});
         		}
	  		})
    }
});
//刷新分页信息
function flushPage(page){
	myVue.pageParm.totalCount = page.totalCount;
	myVue.pageParm.pageCount = page.pageCount;
	myVue.pageParm.currentPage = page.currentPage;
};
</script>
