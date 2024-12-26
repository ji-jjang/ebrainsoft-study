package com.juny.finalboard.domain.post.free.common.dto;

import java.util.List;

public record ResFreePostList(
    List<ResFreePost> postList, FreeSearchCondition searchCondition, FreePageInfo pageInfo) {}
