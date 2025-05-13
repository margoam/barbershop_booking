package com.teachmeskills.tms_booking_project.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

import static com.teachmeskills.tms_booking_project.constant.constant.EXCLUDED_PATHS;
import static com.teachmeskills.tms_booking_project.constant.constant.SWAGGER_PATHS;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class IdValidationFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        for (String swaggerPath : SWAGGER_PATHS) {
            if (path.startsWith(swaggerPath)) {
                return true;
            }
        }
        return EXCLUDED_PATHS.stream().anyMatch(path::contains);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String[] pathParts = request.getRequestURI().split("/");
        if (pathParts.length >= 4) {
            String pathId = pathParts[3];
            if (pathId.equals("null") || pathId.equals("0") || pathId.isEmpty() || !pathId.matches("^\\d+$")) {
                sendErrorResponse(response, "Invalid path ID");
                return;
            }
        }

        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if (entry.getKey().endsWith("Id")) {
                for (String value : entry.getValue()) {
                    if (value == null || !value.matches("^\\d+$")) {
                        sendErrorResponse(response, String.format("Invalid %s value", entry.getKey()));
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\":\"%s\"}", message));
    }
}