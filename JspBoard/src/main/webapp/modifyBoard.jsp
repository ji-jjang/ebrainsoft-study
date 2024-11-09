<%--
  Created by IntelliJ IDEA.
  User: jijunhyuk
  Date: 11/7/24
  Time: 2:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Edit Board</title>
    <script src="/resources/js/boardInputValidation.js"></script>
</head>
<h1>게시판 - 수정</h1>
<body>
<form action="/processModifyBoard" method="post" enctype="multipart/form-data"
      onsubmit="return checkBoardInput(this)">>
    <input type="hidden" name="boardId" value="${board.id}">
    <p>
        <label>카테고리:</label>
        <span>${board.categoryName}</span>
    </p>
    <p>
        <label>등록일시:</label>
        <span>${board.createdAt}</span>
    </p>
    <p>
        <label>수정일시:</label>
        <span>${board.updatedAt}</span>
    <p>
        <label for="createdBy">작성자:</label>
        <input type="text" id="createdBy" name="createdBy" value="${board.createdBy}" required>
    </p>

    <p>
        <label for="password">비밀번호:</label>
        <input type="password" id="password" name="password" required>
    </p>

    <p>
        <label for="title">제목:</label>
        <input type="text" id="title" name="title" value="${board.title}" required>
    </p>

    <p>
        <label for="content">내용:</label><br>
        <textarea id="content" name="content" rows="5" cols="50"
                  required>${board.content}</textarea>
    </p>

    <h3>게시판 이미지</h3>
    <c:forEach var="boardImage" items="${board.boardImages}">
        <div>
            <img src="/images/${boardImage.storedName}${boardImage.extension}" alt="Board Image"
                 width="300" height="200"/>
            <label>
                <input type="checkbox" name="deleteImages"
                       value="${boardImage.id},${boardImage.storedPath},${boardImage.storedName},${boardImage.extension}">
                삭제
            </label>
        </div>
    </c:forEach>

    <h3>첨부 파일</h3>
    <c:forEach var="attachment" items="${board.attachments}">
        <div>
            <a href="/downloads?fileName=${attachment.storedName}&filePath=${attachment.storedPath}&extension=${attachment.extension}">
                    ${attachment.logicalName}${attachment.extension}
            </a>
            <label>
                <input type="checkbox" name="deleteAttachments"
                       value="${attachment.id},${attachment.storedPath},${attachment.storedName},${attachment.extension}">
                삭제
            </label>
        </div>
    </c:forEach>

    <h3>이미지 추가</h3>
    <input type="file" name="images" multiple>

    <h3>파일 추가</h3>
    <input type="file" name="files" multiple>

    <br><br>
    <button type="submit">저장</button>
    <button type="button" onclick="location.href='/boards/free/view/${board.id}'">취소</button>
</form>
</body>
</html>
