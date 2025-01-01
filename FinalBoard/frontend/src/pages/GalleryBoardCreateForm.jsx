import { useState, useEffect } from "react";
import { Container, Row, Col, Form, Button, InputGroup } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import {
  createGalleryPostApi,
  getGalleryCategoriesApi,
} from "../services/galleryService.js";

const GalleryBoardCreateForm = () => {
  const navigate = useNavigate();

  const maxFiles = 5;

  const [formData, setFormData] = useState({
    title: "",
    content: "",
    categoryId: "",
    images: [],
  });

  const [categories, setCategories] = useState([]);

  useEffect(() => {
    const fetchCategories = async () => {
      const data = await getGalleryCategoriesApi();
      setCategories(data);
    };
    fetchCategories();
  }, []);

  const [fileInputs, setFileInputs] = useState([0]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleFileChange = (e, index) => {
    const files = [...formData.images];
    files[index] = e.target.files[0];
    setFormData({ ...formData, images: files });
  };

  const addFileInput = () => {
    if (fileInputs.length >= maxFiles) {
      alert("최대 5개의 파일만 업로드할 수 있습니다.");
      return;
    }
    setFileInputs([...fileInputs, fileInputs.length]);
  };

  const removeFileInput = (index) => {
    const updatedFileInputs = fileInputs.filter((_, i) => i !== index);
    const updatedImages = formData.images.filter((_, i) => i !== index);
    setFileInputs(updatedFileInputs);
    setFormData({ ...formData, images: updatedImages });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const res = await createGalleryPostApi(formData);

    navigate(`/gallery-board/post/${res.id}${location.search}`);
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
          <Col>
            <Form.Label>이미지</Form.Label>
            {fileInputs.map((input, index) => (
              <InputGroup className="mb-2" key={index}>
                <Form.Control
                  type="text"
                  placeholder={
                    formData.images[index]?.name || "선택된 파일 없음"
                  }
                  readOnly
                />
                <Form.Control
                  type="file"
                  className="d-none"
                  id={`fileInput${index}`}
                  onChange={(e) => handleFileChange(e, index)}
                />
                <Button
                  variant="outline-secondary"
                  onClick={() =>
                    document.getElementById(`fileInput${index}`).click()
                  }
                >
                  파일찾기
                </Button>
                <Button variant="danger" onClick={() => removeFileInput(index)}>
                  X
                </Button>
              </InputGroup>
            ))}

            {fileInputs.length < maxFiles && (
              <Button variant="primary" onClick={addFileInput}>
                추가
              </Button>
            )}
          </Col>
        </Row>

        <Row className="mt-4">
          <Col className="text-center">
            <Button type="submit" variant="success" className="me-2">
              등록
            </Button>

            <Button
              variant="secondary"
              onClick={() =>
                window.confirm("정말 취소하시겠습니까?") &&
                navigate(`/gallery-board${location.search}`)
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

export default GalleryBoardCreateForm;
