package com.juny.finalboard.global.security.admin.service;

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

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));

    return new CustomUserDetails(user);
  }
}
