import api from "./api.js";

export const helloApi = async () => {
  const res = await api.get("/api/v1/announcement-posts");
  return res.data;
};
