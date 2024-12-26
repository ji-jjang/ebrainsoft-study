import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginApi } from "../services/authService.js";
import { Form, Button, Container, Row, Col } from "react-bootstrap";
import {
  isValidEmailFormat,
  isValidPasswordLength,
} from "../utils/vaildation.js";

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
    if (!isValidEmailFormat(email)) {
      alert("유효하지 않은 이메일 형식입니다.");
      return;
    }

    if (!isValidPasswordLength(password)) {
      alert("비밀번호는 4~11 자리이어야 합니다.");
      return;
    }

    (async () => {
      const { accessToken } = await loginApi(email, password);
      localStorage.setItem("accessToken", accessToken);
      navigate("/");
    })();
  };

  return (
    <Container
      fluid
      style={{ height: "50vh" }}
      className="d-flex justify-content-center align-items-center"
    >
      <Row className="w-100">
        <Col xs={12} md={6} lg={4} className="mx-auto">
          <h1 className="text-center mb-4">로그인</h1>
          <Form>
            <Form.Group className="mb-3" controlId="email">
              <Form.Label>이메일</Form.Label>
              <Form.Control
                type="email"
                name="email"
                placeholder="이메일을 입력하세요"
                value={email}
                onChange={onChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="password">
              <Form.Label>비밀번호</Form.Label>
              <Form.Control
                type="password"
                name="password"
                placeholder="비밀번호를 입력하세요"
                value={password}
                onChange={onChange}
                required
              />
            </Form.Group>

            <div className="d-grid">
              <Button variant="primary" onClick={login}>
                로그인
              </Button>
            </div>
            <div className="d-grid mt-1">
              <Button variant="secondary" href={"/register"}>
                회원 가입
              </Button>
            </div>
          </Form>
        </Col>
      </Row>
    </Container>
  );
}

export default Login;
