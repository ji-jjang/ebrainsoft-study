import { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Form,
  Button,
  InputGroup,
  ListGroup,
} from "react-bootstrap";
import { useNavigate, useLocation } from "react-router-dom";
import {
  getGalleryCategoriesApi,
  updateGalleryPostApi,
} from "../services/galleryService.js";
import { baseApiUrl } from "../constants/apiUrl.js";

const GalleryBoardEditForm = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const post = location.state?.post || {
    title: "",
    content: "",
    categories: [],
    galleryImages: [],
  };

  const [fileInputs, setFileInputs] = useState([0]);
  const [formData, setFormData] = useState({
    ...post,
    deleteImageIds: new Set(),
    newImages: [],
  });

  const [categories, setCategories] = useState([]);
  const maxFiles = 5;

  useEffect(() => {
    const fetchCategories = async () => {
      const data = await getGalleryCategoriesApi();
      setCategories(data);
    };
    fetchCategories();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleFileChange = (e, index) => {
    const files = [...formData.newImages];
    files[index] = e.target.files[0];
    setFormData({ ...formData, newImages: files });
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
    const updatedImages = formData.newImages.filter((_, i) => i !== index);
    setFormData({ ...formData, newImages: updatedImages });
    setFileInputs(updatedFileInputs);
  };

  const removeExistingImage = (imageId) => {
    setFormData((prev) => ({
      ...prev,
      deleteImageIds: new Set([...prev.deleteImageIds, imageId]),
      galleryImages: prev.galleryImages.filter((image) => image.id !== imageId),
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await updateGalleryPostApi({
      ...formData,
      deleteImageIds: Array.from(formData.deleteImageIds),
    });
    navigate(`/gallery-board/post/${post.id}${location.search}`);
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
              value={formData.categories.id}
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
            <ListGroup>
              {formData.galleryImages.map((image) => (
                <ListGroup.Item
                  key={image.id}
                  className="d-flex justify-content-between align-items-center"
                >
                  <div>
                    <img
                      alt="이미지 설명"
                      className="d-block w-100"
                      style={{ maxHeight: "100px", objectFit: "contain" }}
                      src={`${baseApiUrl}/images/${image.storedName}${image.extension}`}
                    />
                    <a
                      href={`${baseApiUrl}/api/v1/images/${image.id}/download`}
                    >
                      {image.logicalName} ({image.size} bytes)
                    </a>
                  </div>
                  <div>
                    <span className="badge bg-secondary me-2">
                      {image.extension}
                    </span>
                    <Button
                      variant="danger"
                      size="sm"
                      onClick={() => removeExistingImage(image.id)}
                    >
                      삭제
                    </Button>
                  </div>
                </ListGroup.Item>
              ))}
            </ListGroup>

            {fileInputs.map((_, index) => (
              <InputGroup className="mb-2 mt-3" key={index}>
                <Form.Control
                  type="text"
                  placeholder={
                    formData.newImages[index]?.name || "선택된 파일 없음"
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
              <Button variant="primary mt-3" onClick={addFileInput}>
                추가
              </Button>
            )}
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

export default GalleryBoardEditForm;
