package com.juny.finalboard.domain.post.question.admin.service;

import com.juny.finalboard.domain.post.question.common.dto.ReqCreateAnswer;
import com.juny.finalboard.domain.post.question.common.entity.Answer;
import com.juny.finalboard.domain.post.question.common.entity.QuestionPost;
import com.juny.finalboard.domain.post.question.common.repository.AnswerRepository;
import com.juny.finalboard.domain.user.common.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAnswerService {

  private final AnswerRepository answerRepository;

  /**
   *
   *
   * <h1>답변 생성 </h1>
   *
   * <br>
   * - 이미 답변이 있는 질문에선 답변 생성 불가
   *
   * @param req 답변 생성 폼
   * @param post 질문 게시글
   * @param userId 유저아이디
   * @return 생성된 질문
   */
  public Answer createAnswer(ReqCreateAnswer req, QuestionPost post, Long userId) {

    if (post.getAnswer() != null) {
      throw new RuntimeException("answer is already exist");
    }

    Answer answer =
        Answer.builder()
            .content(req.content())
            .post(post)
            .user(User.builder().id(userId).build())
            .build();

    answerRepository.save(answer);

    return answer;
  }
}
