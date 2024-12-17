import { createBrowserRouter } from "react-router-dom";
import Test from "./pages/Test.jsx";
import Login from "./pages/Login.jsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <div>Hello world!</div>,
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/signup",
    element: <div>signup</div>,
  },
  {
    path: "/hello",
    element: <Test />,
  },
]);

export default router;
