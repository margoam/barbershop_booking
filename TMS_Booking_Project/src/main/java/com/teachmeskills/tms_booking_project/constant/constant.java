package com.teachmeskills.tms_booking_project.constant;

import java.util.List;

public interface constant {

    int WORKING_HOUR_START = 10;
    int WORKING_HOUR_END = 20;
    List<String> EXCLUDED_PATHS = List.of(
            "create",
            "all",
            "available-slots",
            "swagger-ui",
            "api-docs",
            "webjars",
            "login",
            "admin"
    );

    List<String> SWAGGER_PATHS = List.of(
            "/swagger-ui.html",
            "/swagger-ui/",
            "/v3/api-docs",
            "/webjars/"
    );
}
