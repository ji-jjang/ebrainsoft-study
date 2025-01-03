import { useState, useEffect } from "react";
import { Container, Row, Col, Form, Button } from "react-bootstrap";
import { useNavigate, useLocation } from "react-router-dom";
import {
  getQuestionCategoriesApi,
  updateQuestionPostApi,
} from "../services/questionService.js";

const QuestionBoardUpdateForm = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const post = location.state?.post;
  const password = location.state?.password;

  const [formData, setFormData] = useState({
    ...post,
    password: password || "",
  });

  const [categories, setCategories] = useState([]);

  useEffect(() => {
    const fetchCategories = async () => {
      const data = await getQuestionCategoriesApi();
      setCategories(data);
    };
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

    await updateQuestionPostApi(formData, password);

    navigate(`/question-board/post/${post.id}${location.search}`, {
      state: { password },
    });
  };

  return (
    <Container className="mt-4">
      <h1>게시글 수정</h1>
      <Form onSubmit={handleSubmit}>
        <Row className="mb-3">
          <Col md={3}>
            <Form.Label>분류</Form.Label>
            <Form.Select
              name="categoryId"
              value={formData.category.id}
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

        <Row className="mt-4">
          <Col className="text-center">
            <Button type="submit" variant="success" className="me-2">
              수정
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

export default QuestionBoardUpdateForm;
