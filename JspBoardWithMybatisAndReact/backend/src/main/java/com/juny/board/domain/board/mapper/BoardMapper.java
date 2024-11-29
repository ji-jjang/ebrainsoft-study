package com.juny.board.domain.board.mapper;

import com.juny.board.domain.board.dto.ReqBoardCreate;
import com.juny.board.domain.board.dto.ResBoardDetail;
import com.juny.board.domain.board.dto.ResBoardList;
import com.juny.board.domain.board.dto.ResBoardViewList;
import com.juny.board.domain.board.entity.Attachment;
import com.juny.board.domain.board.entity.Board;
import com.juny.board.domain.board.entity.BoardImage;
import com.juny.board.domain.board.entity.Category;
import com.juny.board.domain.board.entity.vo.BoardListVO;
import com.juny.board.domain.utils.CategoryMapperUtils;
import com.juny.board.domain.utils.FileService;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

@Mapper(
    componentModel = "spring",
    uses = {
      BoardImageMapper.class,
      AttachmentMapper.class,
      CommentMapper.class,
      BoardVOMapper.class
    })
public interface BoardMapper {

  @Mapping(source = "category.name", target = "categoryName")
  @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
  @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
  ResBoardDetail toResBoardDetail(Board board);

  @Mapping(source = "boards", target = "boards")
  @Mapping(source = "searchCondition", target = "searchCondition")
  @Mapping(source = "pageInfo", target = "pageInfo")
  ResBoardList toResBoardList(BoardListVO boardListVO);

  @Mapping(source = "category.name", target = "categoryName")
  @Mapping(source = "hasAttachment", target = "hasAttachment")
  ResBoardViewList toResBoardViewList(Board board);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "hasAttachment", ignore = true)
  @Mapping(target = "comments", ignore = true)
  @Mapping(target = "viewCount", constant = "0")
  @Mapping(target = "category", source = "categoryName", qualifiedByName = "toCategory")
  @Mapping(target = "boardImages", source = "images", qualifiedByName = "toBoardImages")
  @Mapping(target = "attachments", source = "attachments", qualifiedByName = "toAttachments")
  @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "categoryId", source = "categoryName", qualifiedByName = "mapToCategoryId")
  Board toBoardEntity(ReqBoardCreate req, @Context FileService fileService);

  @Named("mapToCategoryId")
  default Long mapToCategoryId(String categoryName) {
    return CategoryMapperUtils.getCategoryIdByName(categoryName);
  }

  @Named("toCategory")
  default Category toCategory(String categoryName) {
    return Category.builder().name(categoryName).build();
  }

  @Named("toBoardImages")
  default List<BoardImage> toBoardImages(
      List<MultipartFile> images, @Context FileService fileService) {
    if (images == null || images.isEmpty()) {
      return new ArrayList<>();
    }

    return fileService.parseFileDetails(images, "images").stream()
        .map(
            detail ->
                BoardImage.builder()
                    .storedName(detail.getStoredName())
                    .storedPath(detail.getStoredPath())
                    .extension(detail.getExtension())
                    .build())
        .toList();
  }

  @Named("toAttachments")
  default List<Attachment> toAttachments(
      List<MultipartFile> attachments, @Context FileService fileService) {
    if (attachments == null || attachments.isEmpty()) {
      return new ArrayList<>();
    }

    return fileService.parseFileDetails(attachments, "attachments").stream()
        .map(
            detail ->
                Attachment.builder()
                    .logicalName(detail.getLogicalName())
                    .storedName(detail.getStoredName())
                    .storedPath(detail.getStoredPath())
                    .extension(detail.getExtension())
                    .size(detail.getSize())
                    .build())
        .toList();
  }
}
