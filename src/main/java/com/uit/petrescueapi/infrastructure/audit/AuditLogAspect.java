package com.uit.petrescueapi.infrastructure.audit;

import com.uit.petrescueapi.domain.service.AuditLogDomainService;
import com.uit.petrescueapi.domain.valueobject.AuditAction;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

/**
 * Audit logging aspect that intercepts domain service methods.
 *
 * <p>Disabled by default — set {@code app.audit.enabled=true} in application.properties to activate.</p>
 */
@Aspect
@Component
@ConditionalOnProperty(name = "app.audit.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@Slf4j
public class AuditLogAspect {

    private final AuditLogDomainService auditLogService;

    @Around("execution(* com.uit.petrescueapi.domain.service.*.create*(..)) || " +
            "execution(* com.uit.petrescueapi.domain.service.*.update*(..)) || " +
            "execution(* com.uit.petrescueapi.domain.service.*.delete*(..)) || " +
            "execution(* com.uit.petrescueapi.domain.service.*.report*(..)) || " +
            "execution(* com.uit.petrescueapi.domain.service.*.submit*(..)) || " +
            "execution(* com.uit.petrescueapi.domain.service.*.approve*(..)) || " +
            "execution(* com.uit.petrescueapi.domain.service.*.reject*(..)) || " +
            "execution(* com.uit.petrescueapi.domain.service.*.cancel*(..)) || " +
            "execution(* com.uit.petrescueapi.domain.service.*.changeStatus*(..))")
    public Object auditDomainWrite(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName().replace("DomainService", "");

        AuditAction action = resolveAction(methodName);
        UUID actorId = getCurrentUserId();
        String ip = getClientIp();

        Object result = joinPoint.proceed();

        try {
            String entityId = extractEntityId(result);
            auditLogService.log(className, entityId, action, actorId, null, null, ip);
        } catch (Exception e) {
            log.warn("Failed to write audit log for {}.{}: {}", className, methodName, e.getMessage());
        }

        return result;
    }

    private AuditAction resolveAction(String method) {
        if (method.startsWith("create") || method.startsWith("report") || method.startsWith("submit") || method.startsWith("register")) return AuditAction.CREATE;
        if (method.startsWith("update") || method.startsWith("approve") || method.startsWith("reject") || method.startsWith("cancel") || method.startsWith("changeStatus")) return AuditAction.UPDATE;
        if (method.startsWith("delete")) return AuditAction.DELETE;
        return AuditAction.UPDATE;
    }

    private UUID getCurrentUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !"anonymousUser".equals(auth.getName())) {
                return UUID.fromString(auth.getName());
            }
        } catch (Exception ignored) {}
        return null;
    }

    private String getClientIp() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String forwarded = request.getHeader("X-Forwarded-For");
                return forwarded != null ? forwarded.split(",")[0].trim() : request.getRemoteAddr();
            }
        } catch (Exception ignored) {}
        return null;
    }

    private String extractEntityId(Object result) {
        if (result == null) return "unknown";
        try {
            var method = result.getClass().getMethod("getId");
            return String.valueOf(method.invoke(result));
        } catch (NoSuchMethodException e) {
            for (String name : new String[]{"getCaseId", "getApplicationId", "getPostId", "getTagId", "getMediaId", "getLogId"}) {
                try {
                    var m = result.getClass().getMethod(name);
                    return String.valueOf(m.invoke(result));
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return result.toString();
    }
}
