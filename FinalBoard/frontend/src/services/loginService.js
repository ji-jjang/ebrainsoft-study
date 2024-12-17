import api from "./api.js";

export const loginApi = async (email, password) => {
  const formData = new FormData();
  formData.append("email", email);
  formData.append("password", password);

  const response = await api.post("/api/v1/auth/login", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return response.data;
};
