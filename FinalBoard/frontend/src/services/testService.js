import api from "./api.js";

export const helloApi = async () => {
  try {
    const res = await api.get("/api/v1/hello");
    return res.data;
  } catch (error) {
    console.log("/hello API 호출 실패: ", error);
  }
};
