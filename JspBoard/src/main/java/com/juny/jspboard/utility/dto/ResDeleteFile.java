package com.juny.jspboard.utility.dto;

import java.util.List;

public record ResDeleteFile(
    List<String> deleteImages, List<String> deleteAttachments, List<String> deleteComments) {}
