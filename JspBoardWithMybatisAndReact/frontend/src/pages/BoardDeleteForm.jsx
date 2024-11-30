import React, { useState } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import { baseApiUrl } from "../constants/apiUrl.js";
import { checkBoardInput } from "../utils/validation.js";

const api = axios.create({
  baseURL: baseApiUrl,
});

const BoardDeleteForm = () => {
  const navigate = useNavigate();

  const { id } = useParams();

  const [password, setPassword] = useState("");

  const handleInputChange = (e) => {
    setPassword(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const isValid = checkBoardInput(e.target, "delete");
    if (!isValid) {
      return;
    }

    try {
      await api.delete(`/api/v1/boards/${id}`, {
        data: { password },
      });
      alert("게시물 삭제 성공");
      navigate("/boards");
    } catch (error) {
      console.error(`api 호출 실패 -> /api/v1/boards/${id}`, error);
      alert("게시물 삭제 실패");
    }
  };

  return (
    <div>
      <h1>게시판 - 삭제</h1>

      <form onSubmit={handleSubmit}>
        <label htmlFor="password">비밀번호:</label>
        <input
          type="password"
          id="password"
          name="password"
          value={password}
          onChange={handleInputChange}
          required
        />
        <br />
        <br />

        <button
          type="button"
          onClick={() => navigate(`/boards${location.search}`)}
        >
          취소
        </button>
        <button type="submit">삭제</button>
      </form>
    </div>
  );
};

export default BoardDeleteForm;
