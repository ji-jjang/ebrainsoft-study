package com.juny.board.domain.board.mapper;

import com.juny.board.domain.board.dto.ResAttachment;
import com.juny.board.domain.board.entity.Attachment;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

  ResAttachment toResAttachment(Attachment attachment);

  List<ResAttachment> toResAttachmentList(List<Attachment> attachmentList);
}
