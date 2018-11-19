<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
String ctxPath = request.getContextPath();
%>
<jsp:forward page="/htgl/indexcontroller/index">
<jsp:param value="name" name="张灿"/>
</jsp:forward>