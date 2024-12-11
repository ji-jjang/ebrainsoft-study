import api from "./api.js";

export const fetchHelloData = async () => {
  try {
    const res = await api.get("/hello");
    return res.data;
  } catch (error) {
    console.log("/hello API 호출 실패: ", error);
  }
};