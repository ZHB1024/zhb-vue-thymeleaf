<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String ctxPath = request.getContextPath();
%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" i-content="width=device-width, initial-scale=1, maximum-scale=1">

<title><tiles:insertAttribute name="title" /></title>

<!-- <link rel="stylesheet" type="text/css" href="https://unpkg.com/iview@3.0.0/dist/styles/iview.css">
<script src="https://code.jquery.com/jquery-1.12.4.js" integrity="sha256-Qw82+bXyGq6MydymqBxNPYTaUXXq7c8v3CwiYwLLNXU=" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/vue@2.5.16/dist/vue.js"></script>
<script type="text/javascript" src="https://unpkg.com/iview@3.0.0/dist/iview.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script> -->

	<link rel="stylesheet" href="/css/iview.css">
    <link rel="stylesheet" href="/css/htgl.css">
    <script src="https://t1.chei.com.cn/common/js/vue/2.5.6/vue.js"></script>
    <script src="/js/iview-3.0.0.js"></script>
    <script src="/js/axios.min.js"></script>
    <script src="/js/jquery-3.3.1.js"></script>
    <script src="/js/layui/layer.js"></script>
    <!-- <script src="/js/layui/layui.js"></script> -->
    
    <!-- 裁剪图片 -->
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/css/cropper/cropper.css" />
	<script type="text/javascript" src="<%=ctxPath%>/js/cropper/cropper.js"></script>
	
    <!-- 轮播图片 -->
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/css/swiper/swiper-3.4.2.min.css" />
	<script type="text/javascript" src="<%=ctxPath%>/js/swiper/swiper-3.4.2.min.js"></script>
	
	<!-- 统计图 -->
	<script type="text/javascript" src="<%=ctxPath%>/js/echarts/echarts.js"></script>

	<link rel="stylesheet" type="text/css" href="/css/my.css">


<!-- <link rel="stylesheet" type="text/css" href="/css/iview.css">
<script type="text/javascript" src="/js/jquery-1.12.4.js"></script>
<script type="text/javascript" src="/js/vue.js"></script>
<script type="text/javascript" src="/js/iview.js"></script>
<script type="text/javascript" src="/js/axios.min.js"></script> -->

</head>

<body>
    <div id="header">
        <tiles:insertAttribute name="header" />
    </div>
    <div id="body">
    	<tiles:insertAttribute name="left" />
    	<tiles:insertAttribute name="body" />
    </div>
    <div id="footer">
        <tiles:insertAttribute name="footer" />
    </div>
</body>

<script type="text/javascript" >
    $("#body").css('minHeight',$(window).height()-$("#header").height()-$("#footer").height());
</script>

</html>