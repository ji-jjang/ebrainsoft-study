package com.juny.jspboard.utility.dto;

import com.juny.jspboard.board.entity.Attachment;
import com.juny.jspboard.board.entity.BoardImage;
import java.util.List;

public record ResFileParsing(List<Attachment> attachments, List<BoardImage> images) {}
