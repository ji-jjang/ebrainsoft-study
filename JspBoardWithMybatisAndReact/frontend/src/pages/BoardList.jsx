import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useLocation } from "react-router-dom";
import { baseApiUrl } from "../constants/apiUrl.js";

const api = axios.create({
  baseURL: baseApiUrl,
});

const BoardList = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [categories, setCategories] = useState([]);

  const [boards, setBoards] = useState([]);
  const [searchCondition, setSearchCondition] = useState({
    startDate: "",
    endDate: "",
    keyword: "",
    categoryName: "",
  });
  const [pageInfo, setPageInfo] = useState({
    page: 1,
    totalPages: 1,
    totalBoardCount: 0,
  });

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await api.get("/api/v1/categories");
        setCategories(response.data.categoryNames);
      } catch (error) {
        console.error("api 호출 실패 -> /api/v1/categories", error);
      }
    };

    fetchCategories();
  }, []);

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const startDate = queryParams.get("startDate") || "";
    const endDate = queryParams.get("endDate") || "";
    const keyword = queryParams.get("keyword") || "";
    const categoryName = queryParams.get("categoryName") || "";
    const page = parseInt(queryParams.get("page") || "1", 10);

    setSearchCondition({ startDate, endDate, keyword, categoryName });
    setPageInfo((prev) => ({ ...prev, page }));
    fetchBoards({ startDate, endDate, keyword, categoryName, page });
  }, [location.search]);

  const fetchBoards = async (params) => {
    try {
      const response = await api.get("/api/v1/boards", {
        params: {
          page: params.page || pageInfo.page,
          startDate: params.startDate || searchCondition.startDate,
          endDate: params.endDate || searchCondition.endDate,
          keyword: params.keyword || searchCondition.keyword,
          categoryName: params.categoryName || searchCondition.categoryName,
        },
      });

      const data = response.data;

      setBoards(data.boards);
      setSearchCondition((prev) => ({
        ...prev,
        startDate: data.searchCondition.startDate,
        endDate: data.searchCondition.endDate,
        keyword: data.searchCondition.keyword,
        categoryName: data.searchCondition.categoryName,
      }));
      setPageInfo({
        page: data.pageInfo.page,
        totalPages: data.pageInfo.totalPages,
        totalBoardCount: data.pageInfo.totalBoardCount,
      });
    } catch (error) {
      console.error("Error fetching boards:", error);
    }
  };

  const handleSearchChange = (e) => {
    const { name, value } = e.target;

    setSearchCondition((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSearch = () => {
    const queryParams = new URLSearchParams({
      ...searchCondition,
      page: 1,
    });
    navigate(`?${queryParams.toString()}`);
  };

  const handlePageChange = (page) => {
    const queryParams = new URLSearchParams({
      ...searchCondition,
      page,
    });
    navigate(`?${queryParams.toString()}`);
  };

  return (
    <div>
      <h1>자유 게시판 - 목록</h1>
      <p>총 게시물: {pageInfo.totalBoardCount}</p>

      <form>
        <label htmlFor="startDate">등록일:</label>
        <input
          type="date"
          id="startDate"
          name="startDate"
          value={searchCondition.startDate}
          onChange={handleSearchChange}
        />
        ~
        <input
          type="date"
          id="endDate"
          name="endDate"
          value={searchCondition.endDate}
          onChange={handleSearchChange}
        />
        <label htmlFor="category">카테고리:</label>
        <select
          id="category"
          name="categoryName"
          value={searchCondition.categoryName}
          onChange={handleSearchChange}
        >
          <option value="">전체 카테고리</option>
          {categories.map((category, index) => (
            <option key={index} value={category}>
              {category}
            </option>
          ))}
        </select>
        <label htmlFor="keyword">검색어:</label>
        <input
          type="text"
          id="keyword"
          name="keyword"
          value={searchCondition.keyword}
          onChange={handleSearchChange}
          placeholder="제목, 작성자, 내용 검색"
        />
        <button type="button" onClick={handleSearch}>
          검색
        </button>
      </form>

      {/* 게시판 목록 */}
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
          {boards.map((board) => (
            <tr key={board.id}>
              <td>{board.categoryName}</td>
              <td>{board.hasAttachment ? "있음" : ""}</td>
              <td>
                <a
                  href={`/boards/${board.id}`}
                  onClick={(e) => {
                    e.preventDefault();
                    navigate(`/boards/${board.id}${location.search}`);
                  }}
                >
                  {board.title}
                </a>
              </td>
              <td>{board.createdBy}</td>
              <td>{board.viewCount}</td>
              <td>{board.createdAt}</td>
              <td>{board.updatedAt || ""}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="pagination">
        {Array.from({ length: pageInfo.totalPages }, (_, i) => i + 1).map(
          (page) => (
            <button
              key={page}
              style={{
                color: page === pageInfo.page ? "blue" : "black",
              }}
              onClick={() => handlePageChange(page)}
            >
              {page}
            </button>
          ),
        )}
      </div>

      <form action="/boards/new" method="get">
        <button
          type="button"
          onClick={(e) => {
            e.preventDefault();
            navigate(`/boards/new${location.search}`);
          }}
        >
          등록
        </button>
      </form>
    </div>
  );
};

export default BoardList;
