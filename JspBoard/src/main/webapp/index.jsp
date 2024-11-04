<%--
  Created by IntelliJ IDEA.
  User: jijunhyuk
  Date: 11/4/24
  Time: 11:01 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.juny.jspboard.utility.DriverManagerUtils" %>
<%@ page import="java.lang.module.Configuration" %>
<%@ page import="java.sql.Connection" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="hello-servlet">Hello Servlet</a>


<%
  out.println(DriverManagerUtils.getConnection());
%>

</body>
</html>
