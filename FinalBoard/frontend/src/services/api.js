import axios from "axios";
import { baseApiUrl } from "../constants/apiUrl.js";

const api = axios.create({
  baseURL: baseApiUrl,
});

export default api;