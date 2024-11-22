<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>게시판 수정 폼</title>
    <script src="/js/boardInputValidation.js"></script>
</head>
<h1>게시판 - 수정</h1>
<body>
<form action="/boards/${board.id}" method="post" enctype="multipart/form-data"
      onsubmit="return checkBoardInput(this, 'modify')">
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
            <img src="/${boardImage.storedName}.${boardImage.extension}" alt="Board Image"
                 width="300" height="200"/>
            <label>
                <input type="checkbox" name="deleteImageIds"
                       value="${boardImage.id}">
                삭제
            </label>
        </div>
    </c:forEach>

    <h3>첨부 파일</h3>
    <c:forEach var="attachment" items="${board.attachments}">
        <div>
            <div>
                <a href="/attachments/${attachment.id}/download">
                        ${attachment.logicalName}
                </a>
            </div>
            <label>
                <input type="checkbox" name="deleteAttachmentIds"
                       value="${attachment.id}">
                삭제
            </label>
        </div>
    </c:forEach>

    <h3>이미지 추가</h3>
    <input type="file" name="images" multiple>

    <h3>파일 추가</h3>
    <input type="file" name="attachments" multiple>

    <br><br>
    <button type="submit">저장</button>
    <form action="/boards/${board.id}" method="post">
        <button type="submit">취소</button>
    </form>
</form>
</body>
</html>