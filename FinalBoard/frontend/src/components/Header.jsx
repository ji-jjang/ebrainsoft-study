import { useEffect, useState } from "react";
import { getUserProfileApi } from "../services/userService.js";
import { Container, Nav, Navbar } from "react-bootstrap";
import { useLocation } from "react-router-dom";

const Header = () => {
  const location = useLocation();

  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const [username, setUsername] = useState("");

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    setIsLoggedIn(!!token);
  }, [location]);

  useEffect(() => {
    if (isLoggedIn) {
      const loadUserProfile = async () => {
        const data = await getUserProfileApi();
        setUsername(data.name);
      };
      loadUserProfile();
    }
  }, [isLoggedIn]);

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    setIsLoggedIn(false);
    alert("로그아웃되었습니다.");
  };

  return (
    <Navbar bg="dark" variant="dark" expand="lg">
      <Container>
        <Navbar.Brand href="/">게시판 프로젝트</Navbar.Brand>
        <Nav className="me-auto">
          <Nav.Link href="/announcement-board">공지사항</Nav.Link>
          <Nav.Link href="/free-board">자유게시판</Nav.Link>
          <Nav.Link href="/gallery-board">갤러리</Nav.Link>
          <Nav.Link href="/qna-board">문의게시판</Nav.Link>
        </Nav>
        <Nav>
          {isLoggedIn ? (
            <Nav.Link>
              <span>{username}님 안녕하세요!</span>
              <button onClick={handleLogout} className="btn btn-link ms-3">
                로그아웃
              </button>
            </Nav.Link>
          ) : (
            <>
              <Nav.Link href="/login">로그인</Nav.Link>
              <Nav.Link href="/register">회원가입</Nav.Link>
            </>
          )}
        </Nav>
      </Container>
    </Navbar>
  );
};

export default Header;
