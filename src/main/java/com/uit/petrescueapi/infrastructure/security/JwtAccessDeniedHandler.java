package com.uit.petrescueapi.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Returns a JSON 403 response when an authenticated user lacks required permissions.
 *
 * <p>HTTP 403 Forbidden is returned when:
 * <ul>
 *   <li>User is authenticated but lacks required role (e.g., @PreAuthorize("hasRole('ADMIN')"))</li>
 *   <li>User is authenticated but lacks required permission</li>
 * </ul>
 *
 * <p>This differs from 401 Unauthorized which is for unauthenticated requests.</p>
 */
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<Void> body = ApiResponse.error(403, "Forbidden: " + accessDeniedException.getMessage());
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
