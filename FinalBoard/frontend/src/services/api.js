import axios from "axios";
import { baseApiUrl } from "../constants/apiUrl.js";
import { navigate } from "../services/navigateService.js";

const api = axios.create({
  baseURL: baseApiUrl,
});

api.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem("accessToken");

    if (accessToken) {
      config.headers["Authorization"] = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => {
    console.log("인터셉터 요청 에러", error);
    return Promise.reject(error);
  },
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    let errorResponse = {
      code: "SERVER_ERROR",
      message: "서버에서 오류가 발생했습니다.",
      status: 500,
    };

    if (error.response) {
      const { code, msg } = error.response.data;
      errorResponse = {
        code: code || errorResponse.code,
        message: msg || errorResponse.message,
        status: error.response.status,
      };
    }

    if (errorResponse.status === 400 && errorResponse.code === "AT1") {
      navigate("/login");
    }

    console.error(
      `API 호출 에러: ${errorResponse.code}, ${errorResponse.message}`,
    );
    return Promise.reject(errorResponse);
  },
);

export default api;
