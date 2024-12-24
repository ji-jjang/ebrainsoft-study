package com.juny.finalboard.domain.post.announcement.common.dto;

import java.util.List;

public record ResAnnouncementPostList(
    List<ResAnnouncementPost> resPinnedPostList,
    List<ResAnnouncementPost> resUnPinnedPostList,
    SearchCondition searchCondition,
    PageInfo pageInfo) {}
