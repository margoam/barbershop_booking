package com.teachmeskills.tms_booking_project.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@Order(2)
public class SwaggerSecurityConfig {

    @Qualifier("basicAuthUserDetailsService")
    private final InMemoryUserDetailsManager basicAuthUsers;

    public SwaggerSecurityConfig(InMemoryUserDetailsManager basicAuthUsers) {
        this.basicAuthUsers = basicAuthUsers;
    }

    @Bean
    public SecurityFilterChain swaggerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(basic -> basic
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .userDetailsService(basicAuthUsers)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.addHeader("WWW-Authenticate", "Basic realm=\"Swagger\"");
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
