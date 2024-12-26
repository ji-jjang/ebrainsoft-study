package com.juny.finalboard.domain.user.common;

import com.juny.finalboard.global.constant.Constants;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   *
   *
   * <h1>사용자 정보 조회 </h1>
   *
   * @param id 사용자 아이디
   * @return 사용자
   */
  public User getUserProfile(Long id) {

    return userRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException(String.format("user id invalid: %d", id)));
  }

  /**
   *
   *
   * <h1>사용자 회원가입 </h1>
   *
   * <br>
   * - 패스워드 불일치, 중복 이메일 사용, 연속된 세자리 문자 비밀번호로 사용, 이메일을 패스워드로 사용하는지 검증
   *
   * @param req 회원가입 폼
   * @return 생성된 사용자
   */
  public User register(ReqCreateUser req) {

    if (!req.password().equals(req.passwordConfirm())) {
      throw new RuntimeException("password does not match confirm password");
    }

    if (req.password().equals(req.email()) || req.password().equals(req.email().split("@")[0])) {
      throw new RuntimeException("password is same email");
    }

    char[] password = req.password().toCharArray();
    for (int i = 0; i < password.length - 2; ++i) {
      char first = password[i];
      char second = password[i + 1];
      char third = password[i + 2];
      if (first == (char) (second + 1) && first == (char) (third + 2)) {
        throw new RuntimeException("3 consecutive characters in password");
      }

      if (first == (char) (second - 1) && first == (char) (third - 2)) {
        throw new RuntimeException("3 consecutive characters in password");
      }
    }

    Optional<User> dupUser = userRepository.findByEmail(req.email());
    if (dupUser.isPresent()) {
      throw new RuntimeException("email already in use");
    }

    User user =
        User.builder()
            .email(req.email())
            .password(bCryptPasswordEncoder.encode(req.password()))
            .name(req.name())
            .role(Constants.USER_ROLE)
            .createdAt(LocalDateTime.now())
            .build();

    userRepository.save(user);

    return user;
  }
}
