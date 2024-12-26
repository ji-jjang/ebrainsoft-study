import api from "./api.js";

export const getUserProfileApi = async () => {
  const res = await api.get("/api/v1/user");
  return res.data;
};
