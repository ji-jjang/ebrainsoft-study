package com.juny.jspboardwithmybatis;

import com.juny.jspboardwithmybatis.domain.user.entity.User;
import com.juny.jspboardwithmybatis.domain.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.juny.jspboardwithmybatis.domain.user.mapper")
public class MybatisTest {

  @Autowired
  private UserMapper userMapper;

  @Test
  void findById() {
    User byId = userMapper.findById(1);
    System.out.println(byId);
  }
}
