import { useState, useEffect } from "react";

import { Container, Row, Col, Table, Button } from "react-bootstrap";
import { getAnnouncementPostListApi } from "../services/announcementService.js";
import { getFreePostListApi } from "../services/freeService.js";
import { getGalleryPostListApi } from "../services/galleryService.js";
import { baseApiUrl } from "../constants/apiUrl.js";
import { getQuestionPostListApi } from "../services/questionService.js";

const Home = () => {
  const [announcementPostList, setAnnouncementPostList] = useState([]);
  const [freePostList, setFreePostList] = useState([]);
  const [galleryPostList, setGalleryPostList] = useState([]);
  const [questionPostList, setQuestionPostList] = useState([]);

  useEffect(() => {
    const loadAnnouncementPostList = async () => {
      const announcementPostList = await getAnnouncementPostListApi();

      const slicedAnnouncedPosts = announcementPostList.resPinnedPostList.slice(
        0,
        5,
      );
      setAnnouncementPostList(slicedAnnouncedPosts);

      const freePostList = await getFreePostListApi();

      const slicedFreePosts = freePostList.postList.slice(0, 5);

      setFreePostList(slicedFreePosts);

      const galleryPostList = await getGalleryPostListApi();

      const slicedGalleryPostList = galleryPostList.postList.slice(0, 5);

      setGalleryPostList(slicedGalleryPostList);

      const questionPostList = await getQuestionPostListApi();

      const slicedQuestionPostList = questionPostList.postList.slice(0, 5);

      setQuestionPostList(slicedQuestionPostList);
    };
    loadAnnouncementPostList();
  }, []);

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
                      {item.title} {item.isNew && <span>🆕</span>}
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Col>

          <Col md={6} className="mb-3">
            <div className="d-flex justify-content-between align-items-center">
              <h5 className="mb-0">자유 게시판</h5>
              <Button variant="link" href="/free-board">
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
                {freePostList.map((item, index) => (
                  <tr key={item.id}>
                    <td>{index + 1}</td>
                    <td>{item.categoryName || "없음"}</td>
                    <td>
                      {item.title} {item.isNew && <span>🆕</span>}
                      {item.hasAttachment && <span>📎</span>}
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Col>

          <Col md={6} className="mb-3">
            <div className="d-flex justify-content-between align-items-center">
              <h5 className="mb-0">갤러리 게시판</h5>
              <Button variant="link" href="/gallery-board">
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
                {galleryPostList.map((item, index) => (
                  <tr key={item.id}>
                    <td>{index + 1}</td>
                    <td>{item.categories.name || "없음"}</td>
                    <td>
                      <div
                        style={{
                          width: "100px",
                          height: "100px",
                          marginRight: "10px",
                          display: "inline",
                        }}
                      >
                        {item.representImagePath && (
                          <img
                            src={`${baseApiUrl}/images/${item.representImagePath}`}
                            alt={item.title}
                            style={{
                              width: "50%",
                              height: "50%",
                            }}
                          />
                        )}
                        <span style={{ display: "inline" }}>
                          {" "}
                          (+{item.imageCount})
                        </span>
                        {item.isNew && (
                          <span
                            style={{ display: "inline", marginLeft: "5px" }}
                          >
                            🆕
                          </span>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Col>

          <Col md={6} className="mb-3">
            <div className="d-flex justify-content-between align-items-center">
              <h5 className="mb-0">문의 게시판</h5>
              <Button variant="link" href="/question-board">
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
                {questionPostList.map((item, index) => (
                  <tr key={item.id}>
                    <td>{index + 1}</td>
                    <td>{item.category.id}</td>
                    <td>
                      {item.title}
                      {item.isAnswered ? "(답변완료)" : "(미답변)"}
                      {item.isNew && <span>🆕</span>}
                      {item.isSecret && <span> 🔒</span>}
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Home;
