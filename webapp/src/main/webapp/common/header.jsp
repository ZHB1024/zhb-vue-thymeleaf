<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.zhb.forever.framework.util.PropertyUtil" %>
<%
	String ctxPath = request.getContextPath();
%>

<i-header id="app_header"> 
        <i-menu mode="horizontal" theme="dark" active-name="1">
          <div class="layout-logo"><%=PropertyUtil.getSystemLogoName()%></div>
          <div class="layout-nav" style="text-align: right;">
          </div>
        </i-menu> 
</i-header>

<script>
new Vue({
	el: '#app_header'
});
</script>