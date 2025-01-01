import api from "./api.js";

export const createGalleryPostApi = async (data) => {
  const formData = new FormData();

  formData.append("title", data.title);
  formData.append("content", data.content);
  formData.append("categoryId", data.categoryId);

  data.images.forEach((file) => {
    formData.append("images", file);
  });

  const res = await api.post("/api/v1/gallery-posts", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return res.data;
};

export const getGalleryPostListApi = async (searchCondition) => {
  const res = await api.get("/api/v1/gallery-posts", {
    params: searchCondition,
  });

  return res.data;
};

export const getGalleryCategoriesApi = async () => {
  const res = await api.get("/api/v1/gallery-categories");

  return res.data;
};

export const getGalleryPostApi = async (id) => {
  const res = await api.get(`/api/v1/gallery-posts/${id}`);

  return res.data;
};

export const updateGalleryPostApi = async (data) => {
  const formData = new FormData();

  formData.append("title", data.title);
  formData.append("content", data.content);
  formData.append("categoryId", data.categories.id);

  data.newImages.forEach((file) => {
    if (file) {
      formData.append("addImages", file);
    }
  });

  if (data.deleteImageIds.length > 0) {
    data.deleteImageIds.forEach((id) => {
      formData.append("deleteImageIds", id);
    });
  }

  const res = await api.patch(`/api/v1/gallery-posts/${data.id}`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return res.data;
};

export const deleteGalleryPostApi = async (id) => {
  const res = await api.delete(`/api/v1/gallery-posts/${id}`);

  return res.data;
};
