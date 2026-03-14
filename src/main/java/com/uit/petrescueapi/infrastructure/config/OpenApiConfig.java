package com.uit.petrescueapi.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger configuration — exposes interactive docs at
 * {@code /swagger-ui.html} and the spec at {@code /api-docs}.
 *
 * <p>Adds a global "Bearer Authentication" security scheme so the
 * Swagger UI shows an "Authorize" button for JWT tokens.</p>
 *
 * <p>Server URL uses relative path so Swagger UI fetches from the
 * same host it's currently served from (works in dev, test, production).</p>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Pet Rescue API",
                version = "1.0.0",
                description = "RESTful API for Pet Rescue and Adoption Platform",
                contact = @Contact(name = "Pet Rescue Team", email = "support@petrescue.com")
        ),
        servers = @Server(url = "/", description = "Current server"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Paste your JWT access token here"
)
public class OpenApiConfig {
}
