import { Carousel, Button, Card, Container, Row, Col } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import { getUserProfileApi } from "../services/userService.js";
import {
  deleteGalleryPostApi,
  getGalleryPostApi,
} from "../services/galleryService.js";
import { baseApiUrl } from "../constants/apiUrl.js";
import "../css/App.css";

const GalleryPost = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [post, setPost] = useState({
    id: null,
    title: "",
    content: "",
    createdAt: "",
    createdBy: "",
    viewCount: 0,
    isNew: false,
    representImagePath: "",
    categories: {
      id: null,
      name: "",
    },
    galleryImages: [],
    imageCount: 0,
  });

  const userIdRef = useRef("");

  const [isPostOwner, setIsPostOwner] = useState(false);

  useEffect(() => {
    const loadGalleryPost = async () => {
      const post = await getGalleryPostApi(id);
      setPost(post);

      const user = await getUserProfileApi();
      userIdRef.current = user.id;

      setIsPostOwner(userIdRef.current === post.userId);

      console.log(post.galleryImages);
    };

    loadGalleryPost();
  }, []);

  return (
    <Container className="mt-5">
      <Card>
        <Card.Header>
          <h1 className="mb-0">갤러리 게시판</h1>
        </Card.Header>
        <Card.Body className="mt-5">
          <div className="d-flex border-bottom border-3 pb-2">
            <h3 className="mb-0">{post.categories.name}</h3>
            <h4 className="ms-5">{post.title}</h4>
            <small className="ms-auto">
              {post.createdAt} {post.createdBy}
            </small>
          </div>
          <div className="text-end mt-1">조회수 : {post.viewCount}</div>
        </Card.Body>
      </Card>

      <Carousel
        className="mt-5"
        controls={true}
        indicators={true}
        slide={true}
        id="galleryCarousel"
      >
        {post.galleryImages.map((image) => (
          <Carousel.Item key={image.id}>
            <img
              alt="이미지 설명"
              className="d-block w-100"
              style={{ maxHeight: "500px", objectFit: "contain" }}
              src={`${baseApiUrl}/images/${image.storedName}${image.extension}`}
            />
          </Carousel.Item>
        ))}
      </Carousel>

      <Row>
        <Col>
          <Card className="border-3 shadow-sm mt-4">
            <Card.Body className="p-4 bg-light">
              <div
                className="mb-4"
                style={{ minHeight: "15em", whiteSpace: "pre-wrap" }}
              >
                {post.content}
              </div>
            </Card.Body>
          </Card>
        </Col>

        <div className="mt-4 d-flex justify-content-center gap-3">
          <Button
            variant="primary"
            onClick={() => navigate(`/gallery-board${location.search}`)}
          >
            목록
          </Button>

          {isPostOwner && (
            <Button
              variant="secondary"
              onClick={() =>
                navigate(`/gallery-board/post/${id}/update${location.search}`, {
                  state: { post },
                })
              }
            >
              수정
            </Button>
          )}

          {isPostOwner && (
            <Button
              variant="danger"
              onClick={() => {
                if (window.confirm("정말 삭제하시겠습니까?")) {
                  deleteGalleryPostApi(id);
                  navigate(`/gallery-board${location.search}`);
                }
              }}
            >
              삭제
            </Button>
          )}
        </div>
      </Row>
    </Container>
  );
};

export default GalleryPost;
