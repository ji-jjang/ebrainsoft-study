import { useState, useEffect } from "react";

import { Container, Row, Col, ListGroup, Table, Button } from "react-bootstrap";
import { getAnnouncementPostListApi } from "../services/announcementService.js";

const Home = () => {
  const [announcementPostList, setAnnouncementPostList] = useState([]);

  useEffect(() => {
    const loadAnnouncementPostList = async () => {
      const data = await getAnnouncementPostListApi();

      const slicedPosts = data.resPinnedPostList.slice(0, 5);
      setAnnouncementPostList(slicedPosts);
    };
    loadAnnouncementPostList();
  }, []);

  const data = {
    freeBoard: [
      "자유게시판 1",
      "자유게시판 2",
      "자유게시판 3",
      "자유게시판 4",
      "자유게시판 5",
    ],
    gallery: ["갤러리 1", "갤러리 2", "갤러리 3", "갤러리 4", "갤러리 5"],
    qnaBoard: ["문의 1", "문의 2", "문의 3", "문의 4", "문의 5"],
  };

  return (
    <div>
      <Container className="mt-4 vh-100 d-flex flex-column">
        <Row>
          <Col md={6} className="mb-3">
            <div className="d-flex justify-content-between align-items-center">
              <h5 className="mb-0">공지사항</h5>
              <Button variant="link" href="/announcement-board">
                더보기
              </Button>
            </div>
            <Table striped bordered hover>
              <thead>
                <tr>
                  <th>번호</th>
                  <th>분류</th>
                  <th>제목</th>
                </tr>
              </thead>
              <tbody>
                {announcementPostList.map((item, index) => (
                  <tr key={item.id}>
                    <td>{index + 1}</td>
                    <td>{item.categoryName || "없음"}</td>
                    <td>
                      {item.title}{" "}
                      {item.isNew && <span>🆕</span>}
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Col>

          <Col md={6} className="mb-3">
            <h5>자유 게시판</h5>
            <ListGroup>
              {data.freeBoard.map((item, index) => (
                <ListGroup.Item key={index}>{item}</ListGroup.Item>
              ))}
            </ListGroup>
          </Col>

          <Col md={6} className="mb-3">
            <h5>갤러리</h5>
            <ListGroup>
              {data.gallery.map((item, index) => (
                <ListGroup.Item key={index}>{item}</ListGroup.Item>
              ))}
            </ListGroup>
          </Col>

          <Col md={6} className="mb-3">
            <h5>문의 게시판</h5>
            <ListGroup>
              {data.qnaBoard.map((item, index) => (
                <ListGroup.Item key={index}>{item}</ListGroup.Item>
              ))}
            </ListGroup>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Home;
