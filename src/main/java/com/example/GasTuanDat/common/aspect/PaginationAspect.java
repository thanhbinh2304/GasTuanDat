package com.example.GasTuanDat.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PaginationAspect {

    @Around("execution(* com.example.GasTuanDat..*Controller.*(..))")
    public Object limitPagination(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        if (parameterNames == null) {
            return joinPoint.proceed(args);
        }

        for (int i = 0; i < parameterNames.length; i++) {
            if ("limit".equalsIgnoreCase(parameterNames[i]) || "pageSize".equalsIgnoreCase(parameterNames[i])) {
                if (args[i] instanceof Integer) {
                    int limit = (Integer) args[i];
                    if (limit > 100) {
                        args[i] = 100; // Force limit to max 100
                    }
                }
            }
        }
        
        return joinPoint.proceed(args);
    }
}
