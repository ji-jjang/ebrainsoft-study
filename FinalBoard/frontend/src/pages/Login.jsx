import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginApi } from "../services/loginService.js";

function Login() {
  const [loginInputData, setLoginInputData] = useState({
    email: "",
    password: "",
  });
  const { email, password } = loginInputData;
  const navigate = useNavigate();

  const onChange = (e) => {
    const { name, value } = e.target;
    setLoginInputData({
      ...loginInputData,
      [name]: value,
    });
  };

  const login = () => {
    if (!/\S+@\S+\.\S+/.test(email)) {
      alert("유효하지 않은 이메일 형식입니다.");
      return;
    }

    if (password.length < 4) {
      alert("비밀번호는 4자리 이상이어야 합니다.");
      return;
    }

    (async () => {
      const { accessToken } = await loginApi(email, password);
      localStorage.setItem("accessToken", accessToken);
      navigate("/");
    })();
  };

  return (
    <div
      style={{
        width: "100vw",
        margin: "60px 0",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      <div
        style={{
          paddingTop: "40px",
        }}
      >
        <h3>로그인</h3>
        <input
          name="email"
          type="email"
          placeholder="이메일을 입력해주세요"
          value={email}
          onChange={onChange}
        />
        <input
          name="password"
          type="password"
          placeholder="패스워드를 입력해주세요"
          value={password}
          onChange={onChange}
        />
        <button onClick={login}>로그인</button>
      </div>
    </div>
  );
}

export default Login;
