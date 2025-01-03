import { useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { Button } from "react-bootstrap";
import { deleteQuestionPostApi } from "../services/questionService.js";

const QuestionPostPasswordForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const [password, setPassword] = useState("");
  const { actionType } = location.state || {};
  const { post } = location.state || {};

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (actionType === "update") {
      navigate(`/question-board/post/${id}/update${location.search}`, {
        state: { post, password },
      });
    } else if (actionType === "delete") {
      if (window.confirm("정말 삭제하시겠습니까?")) {
        deleteQuestionPostApi(id, password);
        navigate(`/question-board${location.search}`);
      }
    } else {
      navigate(`/question-board/post/${id}`, {
        state: { password },
      });
    }
  };

  return (
    <div>
      <h2>비밀번호 입력</h2>
      <form onSubmit={handleSubmit}>
        <label>
          비밀번호:
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </label>
        <button type="submit">확인</button>
        <Button
          variant="primary"
          onClick={() => navigate(`/question-board${location.search}`)}
        >
          목록
        </Button>
      </form>
    </div>
  );
};

export default QuestionPostPasswordForm;
