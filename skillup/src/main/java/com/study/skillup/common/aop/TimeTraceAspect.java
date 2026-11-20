package com.study.skillup.common.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimeTraceAspect {

    @Around("execution(* com.study.skillup..service.*.*(..))")
    public Object trace(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            log.info("[TIME] {} {}ms", pjp.getSignature().toShortString(), System.currentTimeMillis() - start);
        }
    }
}
