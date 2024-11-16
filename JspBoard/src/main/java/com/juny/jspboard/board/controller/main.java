package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.controller.main.User.Builder;

public class main {
  public static class User {
    private final String name;
    private final int age;
    private final String email;
    private final String phone;
    private final String address;

    // 생성자는 private으로 접근 제한
    private User(Builder builder) {
      this.name = builder.name;
      this.age = builder.age;
      this.email = builder.email;
      this.phone = builder.phone;
      this.address = builder.address;
    }

    // Getter 메서드들...

    public static class Builder {
      private String name; // 필수 매개변수
      private int age;
      private String email;
      private String phone;
      private String address;

      public Builder(String name) {
        this.name = name;
      }

      public Builder age(int age) {
        this.age = age;
        return this;
      }

      public Builder email(String email) {
        this.email = email;
        return this;
      }

      public Builder phone(String phone) {
        this.phone = phone;
        return this;
      }

      public Builder address(String address) {
        this.address = address;
        return this;
      }

      public User build() {
        return new User(this);
      }
    }
  }
  public static void main(String[] args){
    User user = new Builder("홍길동")
      .age(20)
      .email("juny@gmail.com")
      .address("seoul")
      .build();
  }
}

