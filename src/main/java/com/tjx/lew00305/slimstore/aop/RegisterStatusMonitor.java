package com.tjx.lew00305.slimstore.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.tjx.lew00305.slimstore.controller.CustomerDisplayController;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister.RegisterStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RegisterStatusMonitor {
    
    private final CustomerDisplayController customerDisplayController;

    @Pointcut("execution(public * com.tjx.lew00305.slimstore.service.LocationService.updateRegisterWithClose(..))")
    private void aPointCutFromRegisterClose() {}

    @Pointcut("execution(public * com.tjx.lew00305.slimstore.service.LocationService.updateRegisterWithOpen(..))")
    private void aPointCutFromRegisterOpen() {}
    
    @AfterReturning(pointcut = "aPointCutFromRegisterClose()")
    public void sendRegisterCloseToWebsocket() {
        customerDisplayController.sendRegisterStatus(RegisterStatus.CLOSED);
    }

    @AfterReturning(pointcut = "aPointCutFromRegisterOpen()")
    public void sendRegisterOpenToWebsocket() {
        customerDisplayController.sendRegisterStatus(RegisterStatus.OPEN);
    }

}
