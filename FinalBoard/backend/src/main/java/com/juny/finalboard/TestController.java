package com.juny.finalboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

  @GetMapping("/api/v1/hello")
  @ResponseBody
  public String getHello() {

    return "hello";
  }

  @GetMapping("/admin/v1/hello")
  @ResponseBody
  public String getAdminHello() {

    return "admin hello";
  }

  @GetMapping("/admin/get-hello")
  @ResponseBody
  public String adminGetHello() {

    return "admin-get-hello";
  }

  @PostMapping("/admin/post-hello")
  @ResponseBody
  public String adminPostHello() {

    return "admin-post-hello";
  }
}
