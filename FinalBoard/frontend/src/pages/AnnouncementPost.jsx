import { useEffect, useState } from "react";
import { getAnnouncementPostApi } from "../services/announcementService.js";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { Button, Card, Row, Col, Container } from "react-bootstrap";

const Post = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [post, setPost] = useState({
    categoryName: "",
    title: "",
    createdAt: "",
    createdBy: "",
    viewCount: 0,
    content: "",
  });

  useEffect(() => {
    const loadAnnouncementPost = async () => {
      const data = await getAnnouncementPostApi(id);
      setPost(data);
    };

    loadAnnouncementPost();
  }, [id]);

  return (
    <>
      <Container className="mt-5">
        <Card.Header>
          <h1 className="mb-0">공지사항</h1>
        </Card.Header>
        <Card.Body className="mt-5">
          <div className="d-flex border-bottom border-3 pb-1">
            <h3 className="mb-0">{post.categoryName}</h3>
            <h4 className="ms-5">{post.title}</h4>
            <small className="ms-auto">
              {post.createdAt} {post.createdBy}
            </small>
          </div>
          <div className="text-end mt-1">조회수 : {post.viewCount}</div>
        </Card.Body>
        <Row>
          <Col>
            <Card className="border-0 shadow-sm mt-4">
              <Card.Body className="p-4 bg-light">
                <div
                  className="mb-4"
                  style={{ minHeight: "30em", whiteSpace: "pre-wrap" }}
                >
                  {post.content}
                </div>
              </Card.Body>
            </Card>
            <div className="mt-4 d-flex justify-content-center">
              <Button
                onClick={() =>
                  navigate(`/announcement-board${location.search}`)
                }
              >
                목록
              </Button>
            </div>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Post;
