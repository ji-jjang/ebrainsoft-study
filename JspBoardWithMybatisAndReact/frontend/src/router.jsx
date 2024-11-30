import { createBrowserRouter } from "react-router-dom";
import BoardList from "./pages/BoardList.jsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <div>Hello world!</div>,
  },
  {
    path: "/boards",
    element: <BoardList />,
  },
]);

export default router;
