import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { baseApiUrl } from "../constants/apiUrl.js";
import { checkBoardInput } from "../utils/validation.js";

const api = axios.create({
  baseURL: baseApiUrl,
});

const BoardCreateForm = () => {
  const navigate = useNavigate();

  const [categories, setCategories] = useState([]);

  const [formData, setFormData] = useState({
    categoryName: "",
    createdBy: "",
    password: "",
    passwordConfirm: "",
    title: "",
    content: "",
    images: [],
    attachments: [],
  });

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await api.get("/api/v1/categories");
        setCategories(response.data.categoryNames || []);
      } catch (error) {
        console.error("api 호출 실패 -> /api/v1/categories", error);
      }
    };

    fetchCategories();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleFileChange = (e) => {
    const { name } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: Array.from(e.target.files),
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const isValid = checkBoardInput(e.target, "create");
    if (!isValid) {
      return;
    }

    const data = new FormData();
    data.append("categoryName", formData.categoryName);
    data.append("createdBy", formData.createdBy);
    data.append("password", formData.password);
    data.append("passwordConfirm", formData.password);
    data.append("title", formData.title);
    data.append("content", formData.content);

    formData.images.forEach((file) => data.append("images", file));
    formData.attachments.forEach((file) => data.append("attachments", file));

    try {
      await api.post("/api/v1/boards", data);
      alert("게시물 등록 성공");
      navigate("/boards");
    } catch (error) {
      console.error("게시물 등록 실패:", error);
      alert("게시물 등록 실패");
    }
  };

  return (
    <div>
      <h1>게시판 - 등록</h1>

      <form onSubmit={handleSubmit}>
        <label htmlFor="category">카테고리:</label>
        <select
          id="category"
          name="categoryName"
          value={formData.categoryName}
          onChange={handleInputChange}
          required
        >
          <option value="">카테고리 선택</option>
          {categories?.length > 0 &&
            categories.map((category, index) => (
              <option key={index} value={category}>
                {category}
              </option>
            ))}
        </select>
        <br />
        <br />

        <label htmlFor="images">이미지 추가:</label>
        <input
          type="file"
          id="images"
          name="images"
          multiple
          onChange={handleFileChange}
        />
        <br />
        <br />

        <label htmlFor="createdBy">작성자:</label>
        <input
          type="text"
          id="createdBy"
          name="createdBy"
          value={formData.createdBy}
          onChange={handleInputChange}
          required
        />
        <br />
        <br />

        <label htmlFor="password">비밀번호:</label>
        <input
          type="password"
          id="password"
          name="password"
          value={formData.password}
          onChange={handleInputChange}
          required
        />
        <br />
        <br />

        <label htmlFor="passwordConfirm">비밀번호 확인:</label>
        <input
          type="password"
          id="passwordConfirm"
          name="passwordConfirm"
          value={formData.passwordConfirm}
          onChange={handleInputChange}
          required
        />
        <br />
        <br />

        <label htmlFor="title">제목:</label>
        <input
          type="text"
          id="title"
          name="title"
          value={formData.title}
          onChange={handleInputChange}
          required
        />
        <br />
        <br />

        <label htmlFor="content">내용:</label>
        <textarea
          id="content"
          name="content"
          rows="5"
          cols="50"
          value={formData.content}
          onChange={handleInputChange}
          required
        ></textarea>
        <br />
        <br />

        <label>파일 첨부:</label>
        <input
          type="file"
          name="attachments"
          multiple
          onChange={handleFileChange}
        />
        <br />
        <br />

        <button
          type="button"
          onClick={() => navigate(`/boards${location.search}`)}
        >
          취소
        </button>
        <button type="submit">저장</button>
      </form>
    </div>
  );
};

export default BoardCreateForm;
