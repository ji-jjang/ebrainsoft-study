<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>게시판 상세 페이지</title>
</head>

<body>
<h1>게시판 - 보기</h1>
<p>작성자 : ${board.createdBy} | 등록일 : ${board.createdAt} | 수정일 : ${board.updatedAt} | 조회수
    : ${board.viewCount}</p>
<h3>[${board.categoryName}] ${board.title} </h3>

<p>${board.content}</p>

<c:forEach var="boardImage" items="${board.boardImages}">
    <div>
        <img src="/${boardImage.storedName}.${boardImage.extension}" alt="Board Image"
             width="300" height="200"/>
    </div>
</c:forEach>

<br>
<br>
<br>
<h3>첨부파일</h3>
<c:forEach var="attachment" items="${board.attachments}">
    <div>
        <a href="/attachments/${attachment.id}/download">
                ${attachment.logicalName}
        </a>
    </div>
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
        <label for="createdBy">이름:</label>
        <input type="text" id="createdBy" name="createdBy" required>
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
    <button onclick="location.href='/boards'">목록</button>

    <form action="/boards/${board.id}/update" method="get">
        <button type="submit">수정</button>
    </form>

    <form action="/boards/${board.id}/deleteForm" method="get">
        <button type="submit">삭제</button>
    </form>
</div>
</body>
</html>