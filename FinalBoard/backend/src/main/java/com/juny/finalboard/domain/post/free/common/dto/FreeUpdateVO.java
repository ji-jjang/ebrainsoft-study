package com.juny.finalboard.domain.post.free.common.dto;

import com.juny.finalboard.domain.post.free.common.entity.FreeAttachment;
import com.juny.finalboard.domain.post.free.common.entity.FreePost;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record FreeUpdateVO(
    FreePost updateFreePost,
    List<FreeAttachment> addAttachmentList,
    List<MultipartFile> addMultipartFileList,
    List<FreeAttachment> deleteAttachmentList) {}
