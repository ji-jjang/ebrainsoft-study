package com.juny.finalboard.global.security.common.service;

import com.juny.finalboard.domain.user.User;
import com.juny.finalboard.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   *
   *
   * <h1>인증 시 유저 email DB 조회</h1>
   *
   * @param email email
   * @return UserDetails
   * @throws UsernameNotFoundException UsernameNotFoundException
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));

    return new CustomUserDetails(user);
  }
}
