package com.juny.finalboard.domain.user.user;

import com.juny.finalboard.domain.user.common.ReqCreateUser;
import com.juny.finalboard.domain.user.common.ResUser;
import com.juny.finalboard.domain.user.common.User;
import com.juny.finalboard.domain.user.common.UserMapper;
import com.juny.finalboard.domain.user.common.UserService;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   *
   *
   * <h1>사용자 정보 조회 </h1>
   *
   * @param userDetails 인증된 사용자 정보
   * @return 사용자
   */
  @GetMapping("/api/v1/user")
  public ResponseEntity<ResUser> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {

    User user = userService.getUserProfile(userDetails.getId());

    ResUser resUser = UserMapper.toResUser(user);

    return new ResponseEntity<>(resUser, HttpStatus.OK);
  }

  /**
   *
   *
   * <h1>사용자 회원가입 API</h1>
   *
   * @param req 회원가입 폼
   * @return 생성된 사용자
   */
  @PostMapping("/api/v1/register")
  public ResponseEntity<ResUser> register(@RequestBody ReqCreateUser req) {

    User user = userService.register(req);

    ResUser resUser = UserMapper.toResUser(user);

    return new ResponseEntity<>(resUser, HttpStatus.CREATED);
  }
}
