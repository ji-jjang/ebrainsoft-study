package com.juny.finalboard.domain.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

  @GetMapping("/admin/login")
  public String login() {

    return "admin/login";
  }
}
