package com.tjx.lew00305.slimstore.register;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RegisterSecurity {

    @Pointcut("execution(public * com.tjx.lew00305.slimstore.register.RegisterController.apiRegister(..))")
    private void aPointCutFromRegisterController() {}
    
    @Before(value = "aPointCutFromRegisterController()")
    public void logBefore(
        JoinPoint joinPoint
    ) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info(">> {}() - {}", methodName, Arrays.toString(args));
    }
    
}
