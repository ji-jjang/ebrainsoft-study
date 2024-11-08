<%--
  Created by IntelliJ IDEA.
  User: jijunhyuk
  Date: 11/8/24
  Time: 9:51 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<form action="/processDeleteBoard" method="post">
    <input type="hidden" name="boardId" value="${param.boardId}">

    <c:forEach var="deleteImage" items="${deleteImages}">
        <div>${deleteImage}</div>
        <input type="hidden" name="deleteImages" value="${deleteImage}">
    </c:forEach>

    <c:forEach var="deleteAttachment" items="${deleteAttachments}">
        <div>${deleteAttachment}</div>
        <input type="hidden" name="deleteAttachments" value="${deleteAttachment}">
    </c:forEach>

    <label>비밀번호:</label>
    <input type="password" name="password" required>
    <button type="submit">확인</button>
</form>