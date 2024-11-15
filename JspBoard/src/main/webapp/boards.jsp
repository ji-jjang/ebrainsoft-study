<%--
  Created by IntelliJ IDEA.
  User: jijunhyuk
  Date: 11/5/24
  Time: 10:04 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<html>
<head>
    <title>Board List</title>
</head>
<body>
<h1>자유 게시판 - 목록</h1>

<총 게시물: ${totals}>
    <form action="/boards/free/list" method="get">
        <label for="startDate">등록일:</label>
        <input type="date" id="startDate" name="startDate"
               value="${param.startDate != null ? param.startDate : defaultStartDate}">
        ~
        <input type="date" id="endDate" name="endDate"
               value="${param.endDate != null ? param.endDate : defaultEndDate}">

        <label for="category">카테고리:</label>
        <select id="category" name="category">
            <option value="">전체 카테고리</option>
            <c:forEach var="category" items="${categories}">
                <option value="${category}"
                    ${param.category == category ? "selected" : ""}>${category}</option>
            </c:forEach>
        </select>

        <label for="keyword">검색어:</label>
        <input type="text" id="keyword" name="keyword"
               value="${param.keyword != null ? param.keyword : ''}"
               placeholder="제목, 작성자, 내용 검색">
        <button type="submit">검색</button>
    </form>

    <table border="1">
        <tr>
            <th>카테고리</th>
            <th>첨부파일</th>
            <th>제목</th>
            <th>작성자</th>
            <th>조회수</th>
            <th>등록 일시</th>
            <th>수정 일시</th>
        </tr>
        <c:forEach var="board" items="${boards}">
            <tr>
                <td>${board.categoryName}</td>
                <td>
                    <c:if test="${board.hasAttachment}">
                        있음
                    </c:if>
                </td>
                <td>
                    <a href="/boards/free/view/${board.id}?category=${board.categoryName}">${board.title}</a>
                </td>
                <td>${board.createdBy}</td>
                <td>${board.viewCount}</td>
                <td>${board.createdAt}</td>
                <td>${board.updatedAt}</td>
            </tr>
        </c:forEach>
    </table>

    <div class="pagination">
        <c:forEach var="i" begin="1" end="${totalPages}">
            <a href="/boards/free/list?page=${i}${queryParams}"
               style="${i == page ? 'color: blue' : ''}">${i}</a>
        </c:forEach>
    </div>

    <button onclick="location.href='/boards/free/write'">등록</button>
</body>
</html>