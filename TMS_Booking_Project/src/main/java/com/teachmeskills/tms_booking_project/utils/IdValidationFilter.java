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
import java.util.List;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class IdValidationFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDED_PATHS = List.of("create", "all", "available-slots");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String[] pathParts = request.getRequestURI().split("/");

        if (pathParts.length >= 4) {
            String pathId = pathParts[3];

            // Пропускаем специальные пути
            if (!EXCLUDED_PATHS.contains(pathId)) {
                if (pathId.equals("null") || pathId.equals("0") || pathId.isEmpty() || !pathId.matches("^\\d+$")) {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    response.getWriter().write("{\"error\":\"Invalid path ID\"}");
                    return;
                }
            }
        }

        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if (entry.getKey().endsWith("Id")) {
                for (String value : entry.getValue()) {
                    if (value == null || !value.matches("^\\d+$")) {
                        response.setStatus(HttpStatus.BAD_REQUEST.value());
                        response.getWriter().write(
                                String.format("{\"error\":\"Invalid %s value\"}", entry.getKey())
                        );
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}