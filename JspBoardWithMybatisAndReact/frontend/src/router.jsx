import { createBrowserRouter } from "react-router-dom";

const router = createBrowserRouter([
  {
    path: "/",
    element: <div>Hello world!</div>,
  },
  {
    path: "/login",
    element: <div>login</div>,
  },
  {
    path: "/register",
    element: <div>register</div>,
  },
]);

export default router;
