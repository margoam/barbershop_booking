package com.teachmeskills.tms_booking_project.utils;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<IdValidationFilter> idValidationFilterRegistration() {
        FilterRegistrationBean<IdValidationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new IdValidationFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registration;
    }
}
