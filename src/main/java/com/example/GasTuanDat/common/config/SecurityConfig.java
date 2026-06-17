package com.example.GasTuanDat.common.config;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.common.constants.RoleConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/forgot-password-approval.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/login/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/forgot-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/forgot-password/approve").hasRole(RoleConstants.ADMIN)
                        .requestMatchers(HttpMethod.GET, "/auth/forgot-password/approve").hasRole(RoleConstants.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/auth/change-password")
                        .hasAnyRole(RoleConstants.ADMIN, RoleConstants.SALES, RoleConstants.WAREHOUSE)
                        .requestMatchers(HttpMethod.POST, "/accounts").hasRole(RoleConstants.ADMIN)
                        .requestMatchers(HttpMethod.GET, "/roles/**").authenticated()
                        .requestMatchers("/roles/**").hasRole(RoleConstants.ADMIN)
                        .requestMatchers(HttpMethod.GET, "/positions/**").authenticated()
                        .requestMatchers("/positions/**").hasRole(RoleConstants.ADMIN)
                        .requestMatchers(HttpMethod.GET, "/employees/**").authenticated()
                        .requestMatchers("/employees/**").hasRole(RoleConstants.ADMIN)
                        .requestMatchers("/accounts/**")
                        .hasAnyRole(RoleConstants.ADMIN, RoleConstants.WAREHOUSE, RoleConstants.SALES)
                        .anyRequest().authenticated())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> {
                });

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> writeSecurityError(response, ErrorCode.UNAUTHORIZED);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> writeSecurityError(response, ErrorCode.FORBIDDEN);
    }

    private void writeSecurityError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = Map.of(
                "status", errorCode.getStatus(),
                "error", errorCode.name(),
                "message", errorCode.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(
                List.of("http://localhost:3000", "https://nppgastuandat.vercel.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
