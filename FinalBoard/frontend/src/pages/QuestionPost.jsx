import { useEffect, useRef, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { Container, Card, Row, Col, Button } from "react-bootstrap";

import { getUserProfileApi } from "../services/userService.js";
import {
  deleteQuestionPostApi,
  getQuestionPostApi,
} from "../services/questionService.js";

const QuestionPost = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const [post, setPost] = useState({
    answer: {
      id: null,
      content: "",
      createdAt: "",
      user: {
        id: null,
        name: "",
      },
    },
    category: {
      id: null,
      name: "",
    },
    title: "",
    createdAt: "",
    createdBy: "",
    content: "",
    isAnswered: false,
    isPassword: false,
    userId: "",
    viewCount: 0,
  });

  const location = useLocation();

  const password = location.state?.password;

  const userIdRef = useRef("");

  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const [isPostOwner, setIsPostOwner] = useState(false);

  useEffect(() => {
    const loadFreePost = async () => {
      const post = await getQuestionPostApi(id, password);
      if (!post) return;
      setPost(post);

      const token = localStorage.getItem("accessToken");
      if (token) {
        const user = await getUserProfileApi();
        userIdRef.current = user.id;
        setIsLoggedIn(true);
        setIsPostOwner(userIdRef.current === post.userId);
      }
    };

    loadFreePost();
  }, [id]);

  const handleAction = (actionType) => {
    if (actionType === "update") {
      if (post.userId === null && !password) {
        navigate(`/question-board/post/${id}/passwordForm`, {
          state: { actionType, post },
        });
      } else if (post.userId === null && password) {
        navigate(`/question-board/post/${id}/update${location.search}`, {
          state: { post, password },
        });
      } else if (post.userId === userIdRef.current) {
        navigate(`/question-board/post/${id}/update${location.search}`, {
          state: { post },
        });
      }
    } else if (actionType == "delete") {
      if (post.userId === null && !password) {
        navigate(`/question-board/post/${id}/passwordForm`, {
          state: { actionType },
        });
      } else if (post.userId === null && password) {
        deleteQuestionPostApi(id, password);
        navigate(`/question-board${location.search}`);
      }
    }
  };

  return (
    <Container className="mt-5">
      <Card>
        <Card.Header>
          <h1 className="mb-0">문의 게시판</h1>
        </Card.Header>
        <Card.Body className="mt-5">
          <div className="d-flex border-bottom border-3 pb-2">
            <h3 className="mb-0">{post.category.name}</h3>
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
          <Card.Title className="mt-3">내용</Card.Title>
          <Card className="border-3 shadow-sm mt-3">
            <Card.Body className="p-4 bg-light">
              <div
                className="mb-4"
                style={{ minHeight: "15em", whiteSpace: "pre-wrap" }}
              >
                {post.content}
              </div>
            </Card.Body>
          </Card>

          {post.answer ? (
            <>
              <Card.Title className="mt-3">답변</Card.Title>
              <Card className="border-3 shadow-sm mt-3">
                <Card.Body className="p-4 bg-light">
                  <div className="d-flex justify-content-between">
                    <small className="text-muted">
                      {post.answer.createdBy} - {post.answer.createdAt}
                    </small>
                  </div>
                  <p className="mb-0 mt-2">{post.answer.content}</p>
                </Card.Body>
              </Card>
            </>
          ) : (
            <>
              <Card.Title className="mt-3">답변</Card.Title>
              <Card className="border-3 shadow-sm mt-3">
                <Card.Body className="p-4 bg-light">
                  <div className="align-content-center">
                    아직 등록된 답변이 없습니다.
                  </div>
                </Card.Body>
              </Card>
            </>
          )}

          <div className="mt-4 d-flex justify-content-center gap-3">
            <Button
              variant="primary"
              onClick={() => navigate(`/question-board${location.search}`)}
            >
              목록
            </Button>

            {(!isLoggedIn || isPostOwner) && (
              <Button
                variant="secondary"
                onClick={() => handleAction("update")}
                // onClick={() =>
                //   navigate(`/question-board/post/${id}/update${location.search}`, {
                //     state: { post, password },
                //   })
                // }
              >
                수정
              </Button>
            )}

            {(!isLoggedIn || isPostOwner) && (
              <Button
                variant="danger"
                onClick={() => handleAction("delete")}
                // onClick={() => {
                //   if (window.confirm("정말 삭제하시겠습니까?")) {
                //     deleteQuestionPostApi(id, password);
                //     navigate(`/question-board${location.search}`);
                //   }
                // }}
              >
                삭제
              </Button>
            )}
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default QuestionPost;
