package com.example;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.ZoneId;

public class TimezoneValidateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String timezone = httpRequest.getParameter("timezone");

        if (timezone != null && !timezone.isEmpty()) {
            timezone = timezone.replace(" ", "+");
            try {
                ZoneId.of(timezone);
            } catch (Exception e) {
                httpResponse.setContentType("text/html");
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.getWriter().println("<html><body>");
                httpResponse.getWriter().println("<h1>Invalid timezone</h1>");
                httpResponse.getWriter().println("<p>Please provide a valid timezone.</p>");
                httpResponse.getWriter().println("</body></html>");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
