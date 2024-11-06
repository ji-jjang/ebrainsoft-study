package com.juny.jspboard.dto;

import java.util.List;

public record ResBoardDetail(Long id, String title, String createdBy, String createdAt, String updatedAt, int viewCount, String content, String categoryName, List<ResBoardImage> boardImages, List<ResAttachment> attachments, List<ResComment> comments) {

}
