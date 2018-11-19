<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="com.zhb.forever.framework.util.DateTimeUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String ctxPath = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<table width="760" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td style=" background-position:center; background-repeat:no-repeat; color:#FF0000;font-weight:600;font-size:20px; letter-spacing:2px; font-family:'楷体_utf-8'" height="400" align="center" valign="middle">
	        <%
	        String message = (String)request.getAttribute("errorMessage");
	        if(null != message && !"".equals(message)){
	            out.print(message);
	        } else {
	        %>
	                         非正常访问！
			<%
			}
			%>
        	<br /><br />
	        <span style="font-size:15;padding-right:14px;">
	         <a href="javascript:history.go(-1)">返回</a>
	        </span>
	        
        	<br /><br />
	        <span style="font-size:15;padding-right:14px;">
	        <%
	        String currentTime = DateTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
	        out.println(currentTime);
	        %>
	        </span>
        </td>
    </tr>
</table>
</body>
</html>