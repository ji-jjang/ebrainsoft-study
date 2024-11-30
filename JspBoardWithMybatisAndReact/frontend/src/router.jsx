import { createBrowserRouter } from "react-router-dom";
import BoardList from "./pages/BoardList.jsx";
import BoardDetail from "./pages/BoardDetail.jsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <div>Hello world!</div>,
  },
  {
    path: "/boards",
    element: <BoardList />,
  },
  {
    path: "/boards/:id",
    element: <BoardDetail />,
  },
]);

export default router;
