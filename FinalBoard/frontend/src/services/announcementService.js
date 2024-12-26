import api from "./api.js";

export const getAnnouncementPostListApi = async (searchCondition) => {
  const res = await api.get("/api/v1/announcement-posts", {
    params: searchCondition,
  });

  return res.data;
};

export const getAnnouncementPostApi = async (id) => {
  const res = await api.get(`/api/v1/announcement-posts/${id}`);

  return res.data;
};

export const getAnnouncementCategoriesApi = async () => {
  const res = await api.get("/api/v1/announcement-categories");

  return res.data;
};
