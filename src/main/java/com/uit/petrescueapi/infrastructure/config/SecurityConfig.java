package com.uit.petrescueapi.infrastructure.config;

import com.uit.petrescueapi.infrastructure.security.JwtAuthenticationEntryPoint;
import com.uit.petrescueapi.infrastructure.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Full security configuration — JWT stateless authentication with RBAC.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtEntryPoint;

    /** Endpoints that do not require a Bearer token. */
    private static final String[] PUBLIC_PATHS = {
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/v1/auth/verify-email",
            "/api/v1/auth/resend-verification",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/actuator/**",
            "/",
            "/swagger",
            "/swagger/",
            "/swagger/index.html",
            "/favicon.ico"
    };

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       return http
               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .exceptionHandling(e -> e.authenticationEntryPoint(jwtEntryPoint))
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers(PUBLIC_PATHS).permitAll()
                       .requestMatchers(HttpMethod.GET, "/api/v1/pets/**").permitAll()
                       .anyRequest().authenticated()
               )
               .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
               .build();
   }
    // @Bean
    // public SecurityFilterChain allowAll(HttpSecurity http) throws Exception {
    //     http
    //             .csrf(csrf -> csrf.disable())
    //             .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
    //             .requestCache(rc -> rc.disable())
    //             .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    //     return http.build();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
