import { useEffect, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import {
  addCommentApi,
  deleteFreePostApi,
  getFreePostApi,
} from "../services/freeService.js";
import {
  Container,
  Card,
  Row,
  Col,
  Button,
  ListGroup,
  Form,
} from "react-bootstrap";
import { baseApiUrl } from "../constants/apiUrl.js";
import { getUserProfileApi } from "../services/userService.js";

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
    attachmentList: [],
    commentList: [],
    userId: "",
  });

  const userIdRef = useRef("");

  const [commentContent, setCommentContent] = useState("");

  const [isPostOwner, setIsPostOwner] = useState(false);

  useEffect(() => {
    const loadFreePost = async () => {
      const post = await getFreePostApi(id);
      setPost(post);

      const user = await getUserProfileApi();
      userIdRef.current = user.id;

      setIsPostOwner(userIdRef.current === post.userId);
    };

    loadFreePost();
  }, [id]);

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    const newComment = await addCommentApi(id, commentContent);
    setPost((prev) => ({
      ...prev,
      commentList: [newComment, ...prev.commentList],
    }));
    setCommentContent("");
  };

  return (
    <Container className="mt-5">
      <Card>
        <Card.Header>
          <h1 className="mb-0">자유 게시판</h1>
        </Card.Header>
        <Card.Body className="mt-5">
          <div className="d-flex border-bottom border-3 pb-2">
            <h3 className="mb-0">{post.categoryName}</h3>
            <h4 className="ms-5">{post.title}</h4>
            <small className="ms-auto">
              {post.createdAt} {post.createdBy}
            </small>
          </div>
          <div className="text-end mt-1">조회수 : {post.viewCount}</div>
        </Card.Body>
      </Card>

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

          <div className="mt-4 d-flex justify-content-center gap-3">
            <Button
              variant="primary"
              onClick={() => navigate(`/free-board${location.search}`)}
            >
              목록
            </Button>

            {isPostOwner && (
              <Button
                variant="secondary"
                onClick={() =>
                  navigate(`/free-board/post/${id}/update${location.search}`, {
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
                    deleteFreePostApi(id);
                    navigate(`/free-board${location.search}`);
                  }
                }}
              >
                삭제
              </Button>
            )}
          </div>
        </Col>
      </Row>

      <Row className="mt-5">
        <Col>
          <Card className="border-0 shadow-sm">
            <Card.Header className="bg-info text-white">
              <h3 className="h5 mb-0">첨부 파일</h3>
            </Card.Header>
            <Card.Body>
              <ListGroup>
                {post.attachmentList.map((attachment) => (
                  <ListGroup.Item
                    key={attachment.id}
                    className="d-flex justify-content-between align-items-center"
                  >
                    <a
                      href={`${baseApiUrl}/api/v1/attachments/${attachment.id}/download`}
                    >
                      {attachment.logicalName} ({attachment.size} bytes)
                    </a>
                    <span className="badge bg-secondary">
                      형식: {attachment.extension}
                    </span>
                  </ListGroup.Item>
                ))}
              </ListGroup>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="mt-5">
        <Col>
          <Card className="border-0 shadow-sm">
            <Card.Header className="bg-success text-white">
              <h3 className="h5 mb-0">댓글</h3>
            </Card.Header>
            <Card.Body>
              <Form onSubmit={handleCommentSubmit} className="mb-5">
                <Row className="g-3 align-items-center">
                  <Col md={9}>
                    <Form.Control
                      as="textarea"
                      name="comment"
                      value={commentContent}
                      onChange={(e) => setCommentContent(e.target.value)}
                      placeholder="댓글을 입력해 주세요"
                      required
                      style={{ resize: "none" }}
                    />
                  </Col>
                  <Col md={3} className="text-end">
                    <Button type="submit" className="w-100">
                      등록
                    </Button>
                  </Col>
                </Row>
              </Form>

              <ListGroup>
                {post.commentList.map((comment) => (
                  <ListGroup.Item key={comment.id}>
                    <div className="d-flex justify-content-between">
                      <small className="text-muted">
                        {comment.createdBy} - {comment.createdAt}
                      </small>
                    </div>
                    <p className="mb-0 mt-2">{comment.content}</p>
                  </ListGroup.Item>
                ))}
              </ListGroup>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Post;
