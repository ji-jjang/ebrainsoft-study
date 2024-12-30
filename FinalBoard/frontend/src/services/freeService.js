import api from "./api.js";

export const getFreePostListApi = async (searchCondition) => {
  const res = await api.get("/api/v1/free-posts", {
    params: searchCondition,
  });

  return res.data;
};

export const getFreePostApi = async (id) => {
  const res = await api.get(`/api/v1/free-posts/${id}`);

  return res.data;
};

export const getFreeCategoriesApi = async () => {
  const res = await api.get("/api/v1/free-categories");

  return res.data;
};

export const createFreePostApi = async (data) => {
  const formData = new FormData();

  formData.append("title", data.title);
  formData.append("content", data.content);
  formData.append("categoryId", data.categoryId);

  data.attachments.forEach((file) => {
    formData.append("attachments", file);
  });

  const res = await api.post("/api/v1/free-posts", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return res.data;
};

export const updateFreePostApi = async (data) => {
  const formData = new FormData();

  formData.append("title", data.title);
  formData.append("content", data.content);
  formData.append("categoryId", data.categoryId);

  data.newAttachments.forEach((file) => {
    if (file) {
      formData.append("addAttachments", file);
    }
  });

  if (data.deleteAttachmentIds.length > 0) {
    data.deleteAttachmentIds.forEach((id) => {
      formData.append("deleteAttachmentIds", id);
    });
  }

  const res = await api.patch(`/api/v1/free-posts/${data.id}`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return res.data;
};

export const deleteFreePostApi = async (id) => {
  const res = await api.delete(`/api/v1/free-posts/${id}`);

  return res.data;
};

export const addCommentApi = async (id, content) => {
  const res = await api.post(`/api/v1/free-posts/${id}/comments`, { content });

  return res.data;
};
