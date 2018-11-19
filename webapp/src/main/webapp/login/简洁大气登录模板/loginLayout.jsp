<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%
  String ctxPath = request.getContextPath();
%>
<html>
<head>

<%-- <title><tiles:insertAttribute name="title" /></title> --%>

<title>简洁大气快速登录注册模板</title> 

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<script type="text/javascript" src="/login/js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="/login/images/login.js"></script>
<link href="/login/css/login2.css" rel="stylesheet" type="text/css" />

</head>

<body>

<h1>简洁大气快速登录注册模板<sup>2015</sup></h1>

        <tiles:insertAttribute name="body" />
</body>

</html>