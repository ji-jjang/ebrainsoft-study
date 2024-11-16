<%--
  Created by IntelliJ IDEA.
  User: jijunhyuk
  Date: 11/8/24
  Time: 9:51 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<head>
    <script src="/resources/js/boardInputValidation.js"></script>
</head>
<form action="/boards/free/delete/${param.boardId}" method="post"
      onsubmit="return checkBoardInput(this, 'delete')">
    <input type="hidden" name="boardId" value="${param.boardId}">
    <label>비밀번호:</label>
    <input type="password" name="password" required>
    <button type="submit">확인</button>
</form>