<%--
  Created by IntelliJ IDEA.
  User: jijunhyuk
  Date: 11/5/24
  Time: 6:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>BoardDetail</title>
</head>


<body>
<h1>게시판 - 보기</h1>
<p>작성자 : ${board.createdBy} | 등록일 : ${board.createdAt} | 수정일 : ${board.updatedAt} | 조회수 : ${board.viewCount}</p>
<h3>[${board.categoryName}] ${board.title} </h3>

<p>${board.content}</p>

<c:forEach var="boardImage" items="${board.boardImages}">
    <div>
        <img src="/images/${boardImage.storedName}${boardImage.extension}" alt="Board Image" width="300" height="200"/>
    </div>
</c:forEach>

<br>
<br>
<br>
<h3>첨부파일</h3>
<c:forEach var="attachment" items="${board.attachments}">
    <a href="/boards/downloads?fileName=${attachment.storedName}&filePath=${attachment.storedPath}&extension=${attachment.extension}">
            ${attachment.logicalName}${attachment.extension}
    </a>
</c:forEach>

<br>
<br>
<br>
<h3>댓글</h3>

<c:forEach var="comment" items="${board.comments}">
    <p>${comment.createdBy} ${comment.createdAt}</p>
    <p>${comment.content}</p>
</c:forEach>

<form action="/boards/${board.id}/comments" method="post">
    <p>
        <label for="name">이름:</label>
        <input type="text" id="name" name="name" required>
        <label for="password">비밀번호:</label>
        <input type="password" id="password" name="password" required>
    </p>
    <p>
        <label for="content">내용:</label><br>
        <textarea id="content" name="content" rows="4" cols="50" required></textarea>
    </p>
    <button type="submit">등록</button>
</form>

<div>
    <button onclick="location.href='/boards/free/list'">목록</button>
    <button onclick="location.href='/boards/free/modify/${board.id}'">수정</button>
    <form action="/boards/free/delete" method="post">
        <input type="hidden" name="boardId" value="${board.id}">
        <c:forEach var="attachment" items="${board.attachments}">
            <input type="hidden" name="attachmentFilePath" value="${attachment.storedPath}">
            <input type="hidden" name="attachmentStoredName" value="${attachment.storedName}">
            <input type="hidden" name="attachmentExtension" value="${attachment.extension}">
        </c:forEach>
        <c:forEach var="boardImage" items="${board.boardImages}">
            <input type="hidden" name="imageFilePath" value="${boardImage.storedPath}">
            <input type="hidden" name="imageStoredName" value="${boardImage.storedName}">
            <input type="hidden" name="imageExtension" value="${boardImage.extension}">
        </c:forEach>

        <button type="submit">삭제</button>
    </form>
</div>

</body>
</html>
