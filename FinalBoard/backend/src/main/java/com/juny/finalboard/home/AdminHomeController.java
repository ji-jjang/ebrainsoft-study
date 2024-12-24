package com.juny.finalboard.home;

import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Spring Security logoutFilter, 로그아웃 기능 제공 (세션 무효화) */
@Controller
public class AdminHomeController {

  @GetMapping("/")
  public String home() {

    return "redirect:/admin/management";
  }

  @GetMapping("/admin/management")
  public String adminManagement() {

    return "admin/management";
  }

  @GetMapping("/admin/login")
  public String login(@AuthenticationPrincipal CustomUserDetails userDetails) {

    if (userDetails != null && userDetails.getId() != null) {
      return "redirect:/admin/management";
    }

    return "admin/login";
  }
}
