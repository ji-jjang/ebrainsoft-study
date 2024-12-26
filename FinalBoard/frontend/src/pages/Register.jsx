import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  isValidEmailFormat,
  isValidNameLength,
  isValidPasswordLength,
  isValidPasswordRules,
} from "../utils/vaildation.js";
import { registerApi } from "../services/authService.js";
import { Button, Col, Container, Form, Row } from "react-bootstrap";

function Register() {
  const [registerInputData, setRegisterInputData] = useState({
    email: "",
    password: "",
    passwordConfirm: "",
    name: "",
  });
  const { email, password, passwordConfirm, name } = registerInputData;
  const navigate = useNavigate();

  const onChange = (e) => {
    const { name, value } = e.target;
    setRegisterInputData({
      ...registerInputData,
      [name]: value,
    });
  };

  const register = () => {
    if (password !== passwordConfirm) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }

    if (!isValidEmailFormat(email)) {
      alert("유효하지 않은 이메일 형식입니다.");
      return;
    }

    if (!isValidPasswordLength(password)) {
      alert("비밀번호는 4~11 자리이어야 합니다.");
      return;
    }

    if (!isValidPasswordRules(password)) {
      alert("비밀번호는 연속된 3가지 문자를 사용할 수 없습니다.");
      return;
    }

    if (!isValidNameLength(name)) {
      alert("이름은 2~5 자리이어야 합니다.");
      return;
    }

    (async () => {
      await registerApi({ email, password, passwordConfirm, name });
      alert("회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.");
      navigate("/login");
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
          <h1 className="text-center mb-4">회원가입</h1>
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

            <Form.Group className="mb-3" controlId="passwordConfirm">
              <Form.Label>비밀번호 확인</Form.Label>
              <Form.Control
                type="password"
                name="passwordConfirm"
                placeholder="비밀번호를 다시 입력하세요"
                value={passwordConfirm}
                onChange={onChange}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="name">
              <Form.Label>이름</Form.Label>
              <Form.Control
                type="text"
                name="name"
                placeholder="이름을 입력하세요"
                value={name}
                onChange={onChange}
                required
              />
            </Form.Group>

            <div className="d-grid">
              <Button variant="primary" onClick={register}>
                회원가입
              </Button>
            </div>
            <div className="d-grid mt-1">
              <Button variant="secondary" href={"/login"}>
                로그인 페이지로 이동
              </Button>
            </div>
          </Form>
        </Col>
      </Row>
    </Container>
  );
}

export default Register;
