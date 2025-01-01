import { createBrowserRouter } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Home from "./pages/Home.jsx";
import AnnouncementBoard from "./pages/AnnouncementBoard.jsx";
import Header from "./components/Header.jsx";
import Register from "./pages/Register.jsx";
import AnnouncementPost from "./pages/AnnouncementPost.jsx";
import FreeBoard from "./pages/FreeBoard.jsx";
import FreeBoardCreateForm from "./pages/FreeBoardCreateForm.jsx";
import FreePost from "./pages/FreePost.jsx";
import UpdatePost from "./pages/FreeBoardUpdateForm.jsx";
import GalleryBoard from "./pages/GalleryBoard.jsx";
import GalleryPost from "./pages/GalleryPost.jsx";
import GalleryBoardCreateForm from "./pages/GalleryBoardCreateForm.jsx";
import GalleryBoardUpdateForm from "./pages/GalleryBoardUpdateForm.jsx";

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
  {
    path: "/free-board",
    element: (
      <>
        <Header />
        <FreeBoard />
      </>
    ),
  },
  {
    path: "/free-board/post/:id",
    element: (
      <>
        <Header />
        <FreePost />
      </>
    ),
  },
  {
    path: "/free-board/post/:id/update",
    element: (
      <>
        <Header />
        <UpdatePost />
      </>
    ),
  },
  {
    path: "/free-board/create",
    element: (
      <>
        <Header />
        <FreeBoardCreateForm />
      </>
    ),
  },
  {
    path: "/gallery-board",
    element: (
      <>
        <Header />
        <GalleryBoard />
      </>
    ),
  },
  {
    path: "/gallery-board/post/:id",
    element: (
      <>
        <Header />
        <GalleryPost />
      </>
    ),
  },
  {
    path: "/gallery-board/create",
    element: (
      <>
        <Header />
        <GalleryBoardCreateForm />
      </>
    ),
  },
  {
    path: "/gallery-board/post/:id/update",
    element: (
      <>
        <Header />
        <GalleryBoardUpdateForm />
      </>
    ),
  },
]);

export default router;
