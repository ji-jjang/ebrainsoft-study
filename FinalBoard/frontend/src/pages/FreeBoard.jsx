import { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  Container,
  Row,
  Col,
  Table,
  Button,
  Form,
  Pagination,
} from "react-bootstrap";
import {
  getFreeCategoriesApi,
  getFreePostListApi,
} from "../services/freeService.js";

const FreeBoard = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [freePosts, setFreePosts] = useState([]);
  const [searchCondition, setSearchCondition] = useState({
    startDate: "",
    endDate: "",
    categoryId: "",
    keyword: "",
    pageSize: "",
    sort: "",
  });
  const [pageInfo, setPageInfo] = useState({});

  const [categories, setCategories] = useState([]);

  const fetchFreeBoardList = async (condition) => {
    const data = await getFreePostListApi(condition);

    setFreePosts(data.postList);
    setSearchCondition(data.searchCondition);
    setPageInfo(data.pageInfo);
  };

  useEffect(() => {
    const loadCategories = async () => {
      const data = await getFreeCategoriesApi();
      setCategories(data);
    };

    loadCategories();

    const queryParams = Object.fromEntries(
      new URLSearchParams(location.search).entries(),
    );

    const updatedSearchCondition = {
      ...searchCondition,
      ...queryParams,
    };

    fetchFreeBoardList(updatedSearchCondition);
  }, []);

  const handleSortChange = (e) => {
    const newSort = e.target.value;

    const updatedSearchCondition = {
      ...searchCondition,
      sort: newSort,
      page: "1",
    };
    setSearchCondition(updatedSearchCondition);
    fetchFreeBoardList(updatedSearchCondition);
    const queryParams = new URLSearchParams(updatedSearchCondition).toString();
    navigate(`?${queryParams}`);
  };

  const handleSearch = () => {
    fetchFreeBoardList(searchCondition);
    const queryParams = new URLSearchParams(searchCondition).toString();
    navigate(`?${queryParams}`);
  };

  const handlePageSizeChange = (e) => {
    const newPageSize = e.target.value;

    const updatedSearchCondition = {
      ...searchCondition,
      pageSize: newPageSize,
      page: "1",
    };
    setSearchCondition(updatedSearchCondition);
    fetchFreeBoardList(updatedSearchCondition);
    const queryParams = new URLSearchParams(updatedSearchCondition).toString();
    navigate(`?${queryParams}`);
  };

  const handlePageChange = (page) => {
    const updatedSearchCondition = {
      ...searchCondition,
      page: page,
    };
    setSearchCondition(updatedSearchCondition);
    fetchFreeBoardList(updatedSearchCondition);
    const queryParams = new URLSearchParams(updatedSearchCondition).toString();
    navigate(`?${queryParams}`);
  };

  const handleSearchChange = (e) => {
    const { name, value } = e.target;
    setSearchCondition((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  return (
    <Container fluid>
      <Row className="mt-4">
        <Col>
          <h1>자유 게시판</h1>
        </Col>
      </Row>

      <Row className="mt-3 align-items-center">
        <Col md={2}>
          <Form.Control
            type="date"
            name="startDate"
            value={searchCondition.startDate || ""}
            onChange={handleSearchChange}
          />
        </Col>
        <Col md={2}>
          <Form.Control
            type="date"
            name="endDate"
            value={searchCondition.endDate || ""}
            onChange={handleSearchChange}
          />
        </Col>
        <Col md={2}>
          <Form.Select
            name="categoryId"
            value={searchCondition.categoryId || ""}
            onChange={handleSearchChange}
          >
            <option value="">전체 카테고리</option>
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </Form.Select>
        </Col>
        <Col md={4}>
          <Form.Control
            type="text"
            name="keyword"
            placeholder="제목, 작성자, 내용 검색"
            value={searchCondition.keyword || ""}
            onChange={handleSearchChange}
          />
        </Col>
        <Col md="auto">
          <Button variant="primary" onClick={() => handleSearch()}>
            검색
          </Button>
        </Col>
      </Row>

      <Row className="mt-3">
        <Col>
          <Form.Select
            name="pageSize"
            value={searchCondition.pageSize || ""}
            onChange={handlePageSizeChange}
          >
            <option value={10}>10개씩 보기</option>
            <option value={20}>20개씩 보기</option>
            <option value={30}>30개씩 보기</option>
            <option value={40}>40개씩 보기</option>
          </Form.Select>
        </Col>
        <Col>
          <Form.Select
            name="sort"
            value={searchCondition.sort}
            onChange={handleSortChange}
          >
            <option value="created_at:desc">등록일시 내림차순</option>
            <option value="created_at:asc">등록일시 오름차순</option>
            <option value="view_count:desc">조회수 내림차순</option>
            <option value="view_count:asc">조회수 오름차순</option>
          </Form.Select>
        </Col>
      </Row>

      <Col className="text-end mt-3">
        <Button
          variant="primary"
          href={`/free-board/create`}
          onClick={(e) => {
            e.preventDefault();
            navigate(`/free-board/create${location.search}`);
          }}
        >
          글 등록
        </Button>
      </Col>

      <Row className="mt-3">
        <Col>
          <Table bordered hover>
            <thead>
              <tr>
                <th>번호</th>
                <th>분류</th>
                <th>제목</th>
                <th>조회수</th>
                <th>등록일시</th>
                <th>등록자</th>
              </tr>
            </thead>
            <tbody>
              {freePosts.map((post) => (
                <tr key={post.id} className="table-secondary">
                  <td>{post.id}</td>
                  <td>{post.categoryName}</td>
                  <td>
                    <a
                      href={`/free-board/post/${post.id}`}
                      onClick={(e) => {
                        e.preventDefault();
                        navigate(
                          `/free-board/post/${post.id}${location.search}`,
                        );
                      }}
                    >
                      {post.title} {`(${post.commentCount})`}
                      {post.isNew && <span>✨</span>}
                      {post.hasAttachment && <span>📎</span>}
                    </a>
                  </td>
                  <td>{post.viewCount}</td>
                  <td>{post.createdAt}</td>
                  <td>{post.createdBy}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>

      <Row>
        <Col>
          <Pagination>
            {pageInfo.page > 3 && (
              <Pagination.First
                onClick={() => handlePageChange(1)}
                disabled={pageInfo.page === 1}
              />
            )}

            <Pagination.Prev
              onClick={() => handlePageChange(pageInfo.page - 1)}
              disabled={pageInfo.page === 1}
            />

            {(() => {
              const startPage = Math.max(1, pageInfo.page - 2);
              const endPage = Math.min(pageInfo.totalPages, pageInfo.page + 2);
              return Array.from(
                { length: endPage - startPage + 1 },
                (_, i) => startPage + i,
              ).map((page) => (
                <Pagination.Item
                  key={page}
                  active={page === pageInfo.page}
                  onClick={() => handlePageChange(page)}
                >
                  {page}
                </Pagination.Item>
              ));
            })()}

            <Pagination.Next
              onClick={() => handlePageChange(pageInfo.page + 1)}
              disabled={pageInfo.page === pageInfo.totalPages}
            />

            {pageInfo.page < pageInfo.totalPages - 2 && (
              <Pagination.Last
                onClick={() => handlePageChange(pageInfo.totalPages)}
                disabled={pageInfo.page === pageInfo.totalPages}
              />
            )}
          </Pagination>
        </Col>
      </Row>
    </Container>
  );
};

export default FreeBoard;
