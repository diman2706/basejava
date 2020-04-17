package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.SqlStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ResumeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        List<String> properties = null;
        try {
            properties = Config.getProperties();
        } catch (ClassNotFoundException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        assert properties != null;
        SqlStorage sqlStorage = new SqlStorage(properties.get(0), properties.get(1), properties.get(2));

        List<Resume> list = sqlStorage.getAllSorted();
        try (PrintWriter writer = response.getWriter()) {

            writer.println("<table bgcolor=\"#008000\" width=\"600\" frame = \"border\"> \n" +
                    "<tbody>\n" +
                    "<tr>\n" +
                    "<th>UUID</th>\n" +
                    "<th>Full Name</th>\n" +
                    "</tr>");

            list.forEach(r -> {
                writer.println("<tr>");
                writer.println("<td>" + r.getUuid() + "</td>");
                writer.println("<td>" + r.getFullName() + "</td>");
                writer.println("</tr>");
            });

            writer.println("</tbody\n" +
                    "</table>");
        }
    }
}
