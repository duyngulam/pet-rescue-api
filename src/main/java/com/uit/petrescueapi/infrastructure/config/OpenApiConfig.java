package com.uit.petrescueapi.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger configuration — exposes interactive docs at
 * {@code /swagger-ui.html} and the spec at {@code /api-docs}.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Pet Rescue API",
                version = "1.0.0",
                description = "RESTful API for Pet Rescue and Adoption Platform — skeleton project",
                contact = @Contact(name = "Pet Rescue Team", email = "support@petrescue.com")
        ),
        servers = @Server(url = "http://localhost:8080", description = "Local dev")
)
public class OpenApiConfig {
}
