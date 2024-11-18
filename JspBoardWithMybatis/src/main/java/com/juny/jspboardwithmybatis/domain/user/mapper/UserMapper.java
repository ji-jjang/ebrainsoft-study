package com.juny.jspboardwithmybatis.domain.user.mapper;

import com.juny.jspboardwithmybatis.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

  User findById(Long id);
}
