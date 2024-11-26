<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<head>
    <script src="/js/boardInputValidation.js"></script>
</head>
<form action="/boards/${id}/delete" method="post"
      onsubmit="return checkBoardInput(this, 'delete')">
    <label>비밀번호:</label>
    <input type="password" name="password" required>
    <button type="submit">확인</button>
</form>