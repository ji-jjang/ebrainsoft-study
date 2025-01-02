package com.juny.finalboard.global.security.common.service;

import com.juny.finalboard.domain.user.common.User;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> collection = new ArrayList<>();

    collection.add((GrantedAuthority) user::getRole);

    return collection;
  }

  @Override
  public String getPassword() {

    return user.getPassword();
  }

  @Override
  public String getUsername() {

    return user.getName();
  }

  @Override
  public boolean isAccountNonExpired() {

    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {

    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {

    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {

    return UserDetails.super.isEnabled();
  }

  public Long getId() {

    return user.getId();
  }

  public String getEmail() {

    return user.getEmail();
  }

  public String getRole() {

    return user.getRole();
  }
}
