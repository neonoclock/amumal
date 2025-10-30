package com.example.ktbapi.common.auth;

import com.example.ktbapi.common.exception.UnauthorizedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RequireUserIdAspect {

    @Around("@annotation(com.example.ktbapi.common.auth.RequireUserId)")
    public Object checkUserId(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RequireUserId ann = method.getAnnotation(RequireUserId.class);

        Object[] args = pjp.getArgs();
        Long userId = null;

        if (ann.paramIndex() >= 0) {
            Object v = args[ann.paramIndex()];
            if (v instanceof Long) userId = (Long) v;
        } else {
            String[] paramNames = ((MethodSignature) pjp.getSignature()).getParameterNames();
            int firstLongIdx = -1;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Long) {
                    if (paramNames != null && i < paramNames.length && "userId".equals(paramNames[i])) {
                        userId = (Long) args[i];
                        break;
                    }
                    if (firstLongIdx == -1) firstLongIdx = i;
                }
            }
            if (userId == null && firstLongIdx >= 0) userId = (Long) args[firstLongIdx];
        }

        if (userId == null) throw new UnauthorizedException();
        return pjp.proceed();
    }
}
