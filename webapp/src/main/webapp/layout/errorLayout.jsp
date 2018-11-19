<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%
  String ctxPath = request.getContextPath();
%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">

<title><tiles:insertAttribute name="title" /></title>

    <link rel="stylesheet" href="/css/iview.css">
    <script src="https://t1.chei.com.cn/common/js/vue/2.5.6/vue.js"></script>
    <script src="/js/iview-3.0.0.js"></script>
    <script src="/js/axios.min.js"></script>
    <script src="/js/jquery-3.3.1.js"></script>

	<link rel="stylesheet" type="text/css" href="/css/my.css">

</head>

<body>

	<div id="header">
        <tiles:insertAttribute name="header" />
    </div>
    <div id="body">
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