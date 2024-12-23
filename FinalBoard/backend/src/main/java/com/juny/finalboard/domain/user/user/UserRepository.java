package com.juny.finalboard.domain.user.user;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {

  void save(User user);

  Optional<User> findByEmail(String email);

  Optional<User> findById(Long id);
}
