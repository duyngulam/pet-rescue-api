package com.uit.petrescueapi.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Redirects common incorrect Swagger paths to the correct springdoc UI URL.
 * <p>
 * Correct URL: {@code /swagger-ui.html} → renders Swagger UI.<br>
 * Also accessible at: {@code /swagger-ui/index.html}
 */
@Configuration
public class SwaggerRedirectConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/swagger-ui.html");
        registry.addRedirectViewController("/swagger", "/swagger-ui.html");
        registry.addRedirectViewController("/swagger/", "/swagger-ui.html");
        registry.addRedirectViewController("/swagger/index.html", "/swagger-ui.html");
    }
}
