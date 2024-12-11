import { createBrowserRouter } from "react-router-dom";
import Test from "./pages/Test.jsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <div>Hello world!</div>,
  },
  {
    path: "/signin",
    element: <div>signin</div>,
  },
  {
    path: "/signup",
    element: <div>signup</div>,
  },
  {
    path: "/hello",
    element: <Test />
  }
]);

export default router;
