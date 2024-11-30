import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import { baseApiUrl } from "../constants/apiUrl.js";
import { checkBoardInput } from "../utils/validation.js";

const api = axios.create({
  baseURL: baseApiUrl,
});

const BoardUpdateForm = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const [formData, setFormData] = useState({
    createdBy: "",
    password: "",
    title: "",
    content: "",
    boardImages: [],
    boardAttachments: [],

    addImages: [],
    addAttachments: [],

    deleteImageIds: [],
    deleteAttachmentIds: [],
  });

  useEffect(() => {
    const fetchBoardDetail = async () => {
      try {
        const response = await api.get(`/api/v1/boards/${id}`);
        const data = response.data;
        setFormData({
          ...formData,
          createdBy: data.createdBy,
          title: data.title,
          content: data.content,
          boardImages: data.boardImages || [],
          boardAttachments: data.attachments || [],
        });
      } catch (error) {
        console.error(`api 호출 실패 -> /api/v1/boards/${id}`, error);
      }
    };

    fetchBoardDetail();
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

  const handleCheckboxChange = (e, type) => {
    const { value, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [type]: checked
        ? [...prev[type], Number(value)]
        : prev[type].filter((id) => id !== Number(value)),
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const isValid = checkBoardInput(e.target, "modify");
    if (!isValid) return;

    const data = new FormData();
    data.append("createdBy", formData.createdBy);
    data.append("password", formData.password);
    data.append("title", formData.title);
    data.append("content", formData.content);

    formData.deleteImageIds.forEach((id) => data.append("deleteImageIds", id));
    formData.deleteAttachmentIds.forEach((id) =>
      data.append("deleteAttachmentIds", id),
    );
    formData.addImages.forEach((file) => data.append("addImages", file));
    formData.addAttachments.forEach((file) =>
      data.append("addAttachments", file),
    );

    console.log([...data.entries()]);

    try {
      await api.put(`/api/v1/boards/${id}`, data);
      alert("게시물 수정 성공");
      navigate(`/boards/${id}`);
    } catch (error) {
      console.error("게시물 수정 실패 -> ", error);
      alert("게시물 수정 실패.");
    }
  };

  return (
    <div>
      <h1>게시판 - 수정</h1>
      <form onSubmit={handleSubmit} encType="multipart/form-data">
        <p>
          <label htmlFor="createdBy">작성자:</label>
          <input
            type="text"
            id="createdBy"
            name="createdBy"
            value={formData.createdBy}
            onChange={handleInputChange}
            required
          />
        </p>

        <p>
          <label htmlFor="password">비밀번호:</label>
          <input
            type="password"
            id="password"
            name="password"
            value={formData.password}
            onChange={handleInputChange}
            required
          />
        </p>

        <p>
          <label htmlFor="title">제목:</label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleInputChange}
            required
          />
        </p>

        <p>
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
        </p>

        <h3>게시판 이미지</h3>
        {formData.boardImages.map((image) => (
          <div key={image.id}>
            <img
              src={`${baseApiUrl}/images/${image.storedName}.${image.extension}`}
              alt="Board Image"
              width="300"
              height="200"
            />
            <label>
              <input
                type="checkbox"
                name="deleteImageIds"
                value={image.id}
                onChange={(e) => handleCheckboxChange(e, "deleteImageIds")}
              />
              삭제
            </label>
          </div>
        ))}

        <h3>첨부 파일</h3>
        {formData.boardAttachments.map((attachment) => (
          <div key={attachment.id}>
            <a
              href={`${baseApiUrl}/attachments/${attachment.id}/download`}
              target="_blank"
            >
              {attachment.logicalName}
            </a>
            <label>
              <input
                type="checkbox"
                name="deleteAttachmentIds"
                value={attachment.id}
                onChange={(e) => handleCheckboxChange(e, "deleteAttachmentIds")}
              />
              삭제
            </label>
          </div>
        ))}

        <h3>이미지 추가</h3>
        <input
          type="file"
          name="addImages"
          multiple
          onChange={handleFileChange}
        />

        <h3>파일 추가</h3>
        <input
          type="file"
          name="addAttachments"
          multiple
          onChange={handleFileChange}
        />

        <br />
        <button type="button" onClick={() => navigate(`/boards/${id}`)}>
          취소
        </button>
        <button type="submit">저장</button>
      </form>
    </div>
  );
};

export default BoardUpdateForm;
