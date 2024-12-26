package com.juny.finalboard.domain.user.common;

import lombok.Builder;

@Builder
public record ResUser(Long id, String email, String name, String role, String createdAt) {}
