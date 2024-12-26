package com.juny.finalboard.domain.user.common;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReqCreateUser(
    @NotNull(message = "email not null")
        @NotEmpty(message = "email not empty")
        @Size(min = 4, max = 11, message = "email must be between 4 and 11 characters")
        String email,
    String password,
    String passwordConfirm,
    String name) {}
