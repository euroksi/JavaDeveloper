package org.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String timezoneParam = request.getParameter("timezone");
        LocalDateTime currentTime;
        String timezone;

        try {
            if (timezoneParam != null) {
                TimeZone tz = TimeZone.getTimeZone(timezoneParam);
                if (tz.getID().equals("GMT")) {
                    throw new IllegalArgumentException("Invalid timezone");
                }
                timezone = tz.getID();
                currentTime = LocalDateTime.now(ZoneOffset.of(timezoneParam.replace("UTC", "")));
            } else {
                timezone = "UTC";
                currentTime = LocalDateTime.now(ZoneOffset.UTC);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            try (PrintWriter out = response.getWriter()) {
                out.println("<html>");
                out.println("<head><title>Current Time</title></head>");
                out.println("<body>");
                out.println("<h1>Current Time</h1>");
                out.println("<p>" + currentTime.format(formatter) + " " + timezone + "</p>");
                out.println("</body>");
                out.println("</html>");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid timezone");
        }
    }
}
