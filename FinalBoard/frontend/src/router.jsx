import { createBrowserRouter } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Home from "./pages/Home.jsx";
import AnnouncementBoard from "./pages/AnnouncementBoard.jsx";
import Header from "./components/Header.jsx";
import Register from "./pages/Register.jsx";
import AnnouncementPost from "./pages/AnnouncementPost.jsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <>
        <Header />
        <Home />,
      </>
    ),
  },
  {
    path: "/login",
    element: (
      <>
        <Header />
        <Login />,
      </>
    ),
  },
  {
    path: "/register",
    element: (
      <>
        <Header />
        <Register />
      </>
    ),
  },
  {
    path: "/announcement-board",
    element: (
      <>
        <Header />
        <AnnouncementBoard />
      </>
    ),
  },
  {
    path: "/announcement-board/post/:id",
    element: (
      <>
        <Header />
        <AnnouncementPost />
      </>
    ),
  },
]);

export default router;
