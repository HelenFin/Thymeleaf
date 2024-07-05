package com.example;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class TimeServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = this.getServletContext();
        this.templateEngine = new ThymeleafConfig(servletContext).getTemplateEngine();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String timezone = request.getParameter("timezone");
        ZoneId zoneId;

        if (timezone == null || timezone.isEmpty()) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("lastTimezone".equals(cookie.getName())) {
                        timezone = cookie.getValue();
                        break;
                    }
                }
            }
        }

        try {
            if (timezone != null && !timezone.isEmpty()) {
                timezone = timezone.replace(" ", "+");
                zoneId = ZoneId.of(timezone);

                Cookie cookie = new Cookie("lastTimezone", timezone);
                cookie.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(cookie);
            } else {
                zoneId = ZoneId.of("UTC");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid timezone");
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        String currentTime = ZonedDateTime.now(zoneId).format(dtf);

        Context context = new Context();
        context.setVariable("timezone", zoneId);
        context.setVariable("currentTime", currentTime);

        templateEngine.process("time", context, response.getWriter());
    }
}
