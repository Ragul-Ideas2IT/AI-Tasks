package com.connectinghands.aspect;

import com.connectinghands.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {
    private final AuditLogService auditLogService;

    @Around("@annotation(com.connectinghands.annotation.AuditLog)")
    public Object logAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        // Get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = authentication != null && !"anonymousUser".equals(authentication.getPrincipal()) 
            ? Long.parseLong(authentication.getName()) 
            : null;

        // Get IP address
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = request.getRemoteAddr();

        // Get entity ID from method arguments
        Long entityId = extractEntityId(args);

        // Create audit log entry
        String action = String.format("%s.%s", className, methodName);
        String description = String.format("Method %s called with arguments: %s", methodName, Arrays.toString(args));
        
        try {
            Object result = joinPoint.proceed();
            auditLogService.logAction(userId, action, className, entityId, null, result != null ? result.toString() : null, description, ipAddress);
            return result;
        } catch (Exception e) {
            auditLogService.logAction(userId, action, className, entityId, null, null, "Error: " + e.getMessage(), ipAddress);
            throw e;
        }
    }

    private Long extractEntityId(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }

        // Try to find ID in the first argument
        Object firstArg = args[0];
        if (firstArg instanceof Long) {
            return (Long) firstArg;
        }

        // Try to find ID in any argument
        for (Object arg : args) {
            if (arg instanceof Long) {
                return (Long) arg;
            }
        }

        return null;
    }
} 