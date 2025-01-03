import api from "./api.js";
import { navigate } from "./navigateService.js";

export const getQuestionPostListApi = async (searchCondition) => {
  const res = await api.get("/api/v1/question-posts", {
    params: searchCondition,
  });

  return res.data;
};

export const getQuestionCategoriesApi = async () => {
  const res = await api.get("/api/v1/question-categories");

  return res.data;
};

export const getQuestionPostApi = async (id, password) => {
  try {
    const formData = new FormData();
    formData.append("password", password || "");
    const res = await api.post(`/api/v1/question-posts/${id}`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    return res.data;
  } catch (error) {
    if (error.message === "retry with password") {
      navigate(`/question-board/post/${id}/passwordForm`);
    }
    if (error.message.startsWith("user not match, post's userId:")) {
      alert("해당 게시물을 볼 권한이 없습니다.");
      navigate(`/question-board`);
    }
    return null;
  }
};

export const createQuestionPostApi = async (data) => {
  const formData = new FormData();

  formData.append("title", data.title);
  formData.append("content", data.content);
  formData.append("categoryId", data.categoryId);
  formData.append("isSecret", data.isSecret);
  formData.append("password", data.password);
  formData.append("passwordConfirm", data.passwordConfirm);

  const res = await api.post("/api/v1/question-posts", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return res.data;
};

export const updateQuestionPostApi = async (data) => {
  const formData = new FormData();

  formData.append("title", data.title);
  formData.append("content", data.content);
  formData.append("categoryId", data.category.id);
  formData.append("isSecret", data.isSecret);
  formData.append("password", data.password);

  const res = await api.patch(`/api/v1/question-posts/${data.id}`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return res.data;
};

export const deleteQuestionPostApi = async (id, password) => {
  const formData = new FormData();
  formData.append("password", password || "");

  await api.post(`/api/v1/question-posts/${id}/delete`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};
