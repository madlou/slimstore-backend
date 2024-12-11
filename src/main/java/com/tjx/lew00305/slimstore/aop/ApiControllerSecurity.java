package com.tjx.lew00305.slimstore.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.tjx.lew00305.slimstore.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ApiControllerSecurity {

    private UserService userService;

    public ApiControllerSecurity(
        UserService userService
    ) {
        this.userService = userService;
    }
    
    @Pointcut("execution(public * com.tjx.lew00305.slimstore.controller.ApiController.*(..))")
    private void aPointCutFromApiController() {}
    
    @Before(value = "aPointCutFromApiController()")
    public void logBefore(
        JoinPoint joinPoint
    ) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info(">> {}() - {}", methodName, Arrays.toString(args));
    }
    
    @Around(value = "aPointCutFromApiController()")
    public Object validateQueryAround(
        ProceedingJoinPoint joinPoint
    ) throws Throwable {
        if (!userService.isUserAdmin()) {
            String methodName = joinPoint.getSignature().getName();
            log.info(">> {}() - Not logged in as admin user.", methodName);
            return null;
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }
    
}
