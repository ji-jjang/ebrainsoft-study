import { createBrowserRouter } from "react-router-dom";
import BoardList from "./pages/BoardList.jsx";
import BoardDetail from "./pages/BoardDetail.jsx";
import BoardCreateForm from "./pages/BoardCreateForm.jsx";
import BoardDeleteForm from "./pages/BoardDeleteForm.jsx";

const router = createBrowserRouter([
  {
    path: "/",

    element: (
      <>
        <h1>홈 화면</h1>
        <a href="/boards">게시판 목록 페이지</a>
      </>
    ),
  },
  {
    path: "/boards",
    element: <BoardList />,
  },
  {
    path: "/boards/:id",
    element: <BoardDetail />,
  },
  {
    path: "/boards/new",
    element: <BoardCreateForm />,
  },
  {
    path: "/boards/:id/delete",
    element: <BoardDeleteForm />
  }
]);

export default router;
