import api from "./api.js";

export const loginApi = async (email, password) => {
  const formData = new FormData();
  formData.append("email", email);
  formData.append("password", password);

  const res = await api.post("/api/v1/auth/login", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return res.data;
};

export const registerApi = async ({
  email,
  password,
  passwordConfirm,
  name,
}) => {
  const requestBody = {
    email,
    password,
    passwordConfirm,
    name,
  };

  const res = await api.post("/api/v1/register", requestBody, {
    headers: { "Content-Type": "application/json" },
  });

  return res.data;
};
