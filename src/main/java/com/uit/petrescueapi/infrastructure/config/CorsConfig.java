package com.uit.petrescueapi.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.docker.compose.lifecycle.DockerComposeProperties.Start;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CorsConfig {es — add a dev-only security config that permits all requests.

Add this file: src/main/java/com/uit/petrescueapi/infrastructure/config/DevSecurityConfig.java

Code:
@Configuration
@Profile("dev")
public class DevSecurityConfig {
@Bean
public SecurityFilterChain devSecurity(HttpSecurity http) throws Exception {
http
.csrf(csrf -> csrf.disable())
.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
.requestCache(rc -> rc.disable())
.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
return http.build();
}
}

How to use:

Start app with profile dev (IDE Run config or env): -Dspring.profiles.active=dev or set ENV SPRING_PROFILES_ACTIVE=dev.
This bean is only active in dev and will bypass JWT/auth globally.

    @Value("${app.cors.allowed-origins:http://localhost:5173,https://pet-rescue-community.vercel.app}")
    private String allowedOrigins;

    private List<String> parseOrigins() {
        return Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        List<String> origins = parseOrigins();
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(origins.toArray(new String[0]))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(parseOrigins());
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
