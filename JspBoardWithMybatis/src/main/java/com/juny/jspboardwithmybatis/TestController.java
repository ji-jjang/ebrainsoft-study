package com.juny.jspboardwithmybatis;

import com.juny.jspboardwithmybatis.domain.user.entity.User;
import com.juny.jspboardwithmybatis.domain.user.mapper.UserMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

  private final UserMapper userMapper;

  public TestController(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @GetMapping("/hello")
  public String hello() {

    return "board";
  }

  @GetMapping("/user")
  @ResponseBody
  public String getUser() {
    User user = userMapper.findById(1L);

    return user.toString();
  }
}
