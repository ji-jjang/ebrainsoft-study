import { useState, useEffect } from "react";
import { Container, Row, Col, Form, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import {
  createQuestionPostApi,
  getQuestionCategoriesApi,
} from "../services/questionService.js";

const QuestionPostCreateForm = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    title: "",
    content: "",
    categoryId: "",
    isSecret: false,
    password: "",
    passwordConfirm: "",
  });

  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const [categories, setCategories] = useState([]);

  useEffect(() => {
    const fetchCategories = async () => {
      const data = await getQuestionCategoriesApi();
      setCategories(data);
    };
    const token = localStorage.getItem("accessToken");
    setIsLoggedIn(!!token);
    fetchCategories();
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const res = await createQuestionPostApi(formData);

    navigate(`/question-board/post/${res.id}${location.search}`);
  };

  return (
    <Container className="mt-4">
      <h1>게시글 생성</h1>
      <Form onSubmit={handleSubmit}>
        <Row className="mb-3">
          <Col md={3}>
            <Form.Label>분류</Form.Label>
            <Form.Select
              name="categoryId"
              value={formData.categoryId}
              onChange={handleChange}
              required
            >
              <option value="">전체</option>
              {categories.map((category) => (
                <option key={category.id} value={category.id}>
                  {category.name}
                </option>
              ))}
            </Form.Select>
          </Col>
        </Row>

        <Row className="mb-3">
          <Col>
            <Form.Label>제목</Form.Label>
            <Form.Control
              type="text"
              name="title"
              value={formData.title}
              onChange={handleChange}
              required
              placeholder="제목을 입력하세요"
            />
          </Col>
        </Row>

        <Row className="mb-3">
          <Col>
            <Form.Label>내용</Form.Label>
            <Form.Control
              as="textarea"
              rows={5}
              name="content"
              value={formData.content}
              onChange={handleChange}
              required
              placeholder="내용을 입력하세요"
            />
          </Col>
        </Row>

        <Row className="mb-3">
          <Col md={3}>
            <Form.Check
              type="checkbox"
              label="비밀글"
              name="isSecret"
              checked={formData.isSecret}
              onChange={handleChange}
            />
          </Col>
        </Row>

        {!isLoggedIn && (
          <>
            <Row className="mb-3">
              <Col>
                <Form.Label>비밀번호</Form.Label>
                <Form.Control
                  type="password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                  placeholder="비밀번호를 입력하세요"
                />
              </Col>
              <Col>
                <Form.Label>비밀번호 확인</Form.Label>
                <Form.Control
                  type="password"
                  name="passwordConfirm"
                  value={formData.passwordConfirm}
                  onChange={handleChange}
                  required
                  placeholder="비밀번호를 다시 입력하세요"
                />
              </Col>
            </Row>
          </>
        )}

        <Row className="mt-4">
          <Col className="text-center">
            <Button type="submit" variant="success" className="me-2">
              등록
            </Button>

            <Button
              variant="secondary"
              onClick={() =>
                window.confirm("정말 취소하시겠습니까?") &&
                navigate(`/question-board${location.search}`)
              }
            >
              취소
            </Button>
          </Col>
        </Row>
      </Form>
    </Container>
  );
};

export default QuestionPostCreateForm;
