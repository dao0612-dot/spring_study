package com.study.skillup.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CallTraceAspect {

    @Before("execution(* com.study.skillup..controller.*.*(..))")
    public void trace(JoinPoint jp) {
        log.info("[CALL] {}", jp.getSignature().toShortString());
    }
}
