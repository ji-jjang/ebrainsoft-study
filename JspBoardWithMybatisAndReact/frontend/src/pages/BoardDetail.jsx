import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import { baseApiUrl } from "../constants/apiUrl.js";

const api = axios.create({
  baseURL: baseApiUrl,
});

const BoardDetail = () => {

  const navigate = useNavigate();
  const { id } = useParams();
  const [board, setBoard] = useState({
    boardImages: [],
    attachments: [],
    comments: [],
  });

  useEffect(() => {
    const fetchBoardDetail = async () => {
      try {
        const response = await api.get(`/api/v1/boards/${id}`);
        const data = response.data;

        if (!data.boardImages) data.boardImages = [];
        if (!data.attachments) data.attachments = [];
        if (!data.comments) data.comments = [];

        setBoard(data);
      } catch (error) {
        console.error(`api 호출 실패 -> /api/v1/boards/${id}`, error);
      }
    };

    fetchBoardDetail();
  }, []);

  return (
    <div>
      <h1>게시판 - 보기</h1>
      <p>
        작성자: {board.createdBy} | 등록일: {board.createdAt} | 수정일:{" "}
        {board.updatedAt} | 조회수: {board.viewCount}
      </p>
      <h3>
        [{board.categoryName}] {board.title}
      </h3>

      <p>{board.content}</p>

      {board.boardImages?.map((image) => (
        <div key={image.id}>
          <img
            src={`${baseApiUrl}/images/${image.storedName}.${image.extension}`}
            alt="Board Image"
            width="300"
            height="200"
          />
        </div>
      ))}

      <h3>첨부파일</h3>
      {board.attachments?.map((attachment) => (
        <div key={attachment.id}>
          <a
            href={`${baseApiUrl}/api/v1/attachments/${attachment.id}/download`}
          >
            {attachment.logicalName}
          </a>
        </div>
      ))}

      <h3>댓글</h3>
      {board.comments?.map((comment) => (
        <div key={comment.id}>
          <p>
            {comment.createdBy} {comment.createdAt}
          </p>
          <p>{comment.content}</p>
        </div>
      ))}

      <div style={{ marginTop: "20px" }}>
        <button
          type="button"
          onClick={() => navigate(`/boards${location.search}`)}
        >
          목록
        </button>

        <button onClick={() => navigate(`/boards/${board.id}/update${location.search}`)}>
          수정
        </button>
        <button onClick={() => navigate(`/boards/${board.id}/delete${location.search}`)}>
          삭제
        </button>
      </div>
    </div>
  );
};

export default BoardDetail;
