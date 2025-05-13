package com.teachmeskills.tms_booking_project.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Order(1)
@RequiredArgsConstructor
public class ActuatorSecurityConfig {

    private final InMemoryUserDetailsManager basicAuthUsers;

    @Bean
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/actuator/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasRole("ACTUATOR")
                )
                .httpBasic(withDefaults())
                .userDetailsService(basicAuthUsers)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.addHeader("WWW-Authenticate", "Basic realm=\"Actuator\"");
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
