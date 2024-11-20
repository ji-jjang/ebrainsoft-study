<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>게시판 목록 페이지</title>
</head>

<body>
<h1>자유 게시판 - 목록</h1>

<p>총 게시물: ${pageInfo.totalBoardCount}</p>

<form action="/boards" method="get">
    <label for="startDate">등록일:</label>
    <input type="date" id="startDate" name="startDate"
           value="${searchCondition.startDate}">
    ~
    <input type="date" id="endDate" name="endDate"
           value="${searchCondition.endDate}">

    <label for="category">카테고리:</label>
    <select id="category" name="categoryName">
        <option value="">전체 카테고리</option>
        <c:forEach var="category" items="${categories}">
            <option value="${category}"
                ${searchCondition.categoryName == category ? "selected" : ""}>${category}</option>
        </c:forEach>
    </select>

    <label for="keyword">검색어:</label>
    <input type="text" id="keyword" name="keyword"
           value="${searchCondition.keyword}" placeholder="제목, 작성자, 내용 검색">
    <button type="submit">검색</button>
</form>

<table border="1">
    <thead>
    <tr>
        <th>카테고리</th>
        <th>첨부파일</th>
        <th>제목</th>
        <th>작성자</th>
        <th>조회수</th>
        <th>등록 일시</th>
        <th>수정 일시</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="board" items="${boards}">
        <tr>
            <td>${board.categoryName}</td>
            <td>
                <c:if test="${board.hasAttachment}">
                    있음
                </c:if>
            </td>
            <td>
                <a href="/boards/${board.id}">${board.title}</a>
            </td>
            <td>${board.createdBy}</td>
            <td>${board.viewCount}</td>
            <td>${board.createdAt}</td>
            <td>${board.updatedAt}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<div class="pagination">
    <c:forEach var="i" begin="1" end="${pageInfo.totalPages}">
        <a href="/boards?page=${i}&startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}&categoryName=${searchCondition.categoryName}&keyword=${searchCondition.keyword}"
           style="${i == pageInfo.page ? 'color: blue' : ''}">${i}</a>
    </c:forEach>
</div>

<form action="/boards/new" method="get">
    <button type="submit">등록</button>
</form>
</body>
</html>
