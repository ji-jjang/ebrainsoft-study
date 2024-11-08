package com.juny.jspboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteBoardServlet implements BoardControllerServlet {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    String[] attachmentsFilePath = req.getParameterValues("attachmentFilePath");
    String[] attachmentsStoredName = req.getParameterValues("attachmentStoredName");
    String[] attachmentExtensions = req.getParameterValues("attachmentExtension");

    String[] imageFilePath = req.getParameterValues("imageFilePath");
    String[] imageStoredName = req.getParameterValues("imageStoredName");
    String[] imageExtensions = req.getParameterValues("imageExtension");

    StringBuilder sb = new StringBuilder();
    List<String> deleteAttachments = new ArrayList<>();
    List<String> deleteImages = new ArrayList<>();

    if (imageStoredName != null) {
      for (int i = 0; i < imageFilePath.length; ++i) {
        sb.append(imageFilePath[i]);
        sb.append(imageStoredName[i]);
        sb.append(imageExtensions[i]);
        System.out.println("sb = " + sb);
        deleteImages.add(sb.toString());
        sb.setLength(0);
      }
    }

    if (attachmentsStoredName != null) {
      for (int i = 0; i < attachmentsStoredName.length; ++i) {
        sb.append(attachmentsFilePath[i]);
        sb.append(attachmentsStoredName[i]);
        sb.append(attachmentExtensions[i]);
        System.out.println("sb = " + sb);
        deleteAttachments.add(sb.toString());
        sb.setLength(0);
      }
    }

    req.setAttribute("deleteImages", deleteImages);
    req.setAttribute("deleteAttachments", deleteAttachments);
    req.getRequestDispatcher("/deleteBoardPasswordCheck.jsp").forward(req, res);
  }
}
