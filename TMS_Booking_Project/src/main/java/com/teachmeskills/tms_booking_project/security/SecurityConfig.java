package com.teachmeskills.tms_booking_project.security;

import com.teachmeskills.tms_booking_project.utils.IdValidationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new IdValidationFilter(), JwtFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/create", "/api/barbers/all", "/api/services/all",
                                "/api/schedules/available-slots/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/api/users/admin", "/api/schedules/all").hasRole("ADMIN")
                        .requestMatchers("/api/barbers/**", "/api/schedules/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/bookings/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/schedules/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/schedules/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/schedules/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/services/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/services/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/services/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/barbers/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/barbers/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/barbers/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic
                        .realmName("Swagger API")
                        .authenticationEntryPoint(new BasicAuthenticationEntryPoint())
                )
                .formLogin(form -> form
                        .loginPage("/swagger-login")
                        .permitAll()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails swaggerUser = User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(swaggerUser);
    }

    private static class BasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) throws IOException {
            response.addHeader("WWW-Authenticate", "Basic realm=\"Swagger API\"");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
    }
}