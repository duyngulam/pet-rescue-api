package com.uit.petrescueapi.presentation.advice;

import com.uit.petrescueapi.domain.exception.BaseException;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler producing a uniform {@link ApiResponse} error envelope.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    // ── Domain / business exceptions ─────────────
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBase(BaseException ex) {
        log.warn("{}: {}", ex.getErrorCode(), ex.getMessage());
        return build(ex.getStatus(), ex.getMessage());
    }

    // ── Bean-validation failures ─────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> handleValidation(
            MethodArgumentNotValidException ex) {

        List<Map<String, Object>> errors = ex.getFieldErrors().stream()
                .map(f -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("field", f.getField());
                    m.put("message", f.getDefaultMessage());
                    m.put("rejected", f.getRejectedValue());
                    return m;
                }).toList();

        ApiResponse<List<Map<String, Object>>> body = ApiResponse.error(400, "Validation failed");
        body.setData(errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraint(ConstraintViolationException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ── Illegal state (e.g. bad status transition) ─
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ── Missing static resources (favicon, etc.) ─
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResource(NoResourceFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ── Catch-all ────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.");
    }

    // ── Helper ───────────────────────────────────
    private ResponseEntity<ApiResponse<Void>> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ApiResponse.error(status.value(), message));
    }
}
