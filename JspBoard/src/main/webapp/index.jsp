<%--
  Created by IntelliJ IDEA.
  User: jijunhyuk
  Date: 11/4/24
  Time: 11:01 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.juny.jspboard.ConnectionTest" %>
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

  out.println("This is a test string");
  ConnectionTest t = new ConnectionTest();
  out.println(t.getConnection());


%>

</body>
</html>
