import { createBrowserRouter } from "react-router-dom";
import BingoGame from "./BingoGame";

const router = createBrowserRouter([
  {
    path: "/",
    element: <BingoGame />,
  },
]);

export default router;
