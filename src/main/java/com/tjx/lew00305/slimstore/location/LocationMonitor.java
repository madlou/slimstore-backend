package com.tjx.lew00305.slimstore.location;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.tjx.lew00305.slimstore.display.DisplayWebsocketController;
import com.tjx.lew00305.slimstore.location.StoreRegister.RegisterStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LocationMonitor {

    private final DisplayWebsocketController customerDisplayController;
    
    @Pointcut("execution(public * com.tjx.lew00305.slimstore.location.LocationService.updateRegisterWithClose(..))")
    private void aPointCutFromRegisterClose() {}
    
    @Pointcut("execution(public * com.tjx.lew00305.slimstore.location.LocationService.updateRegisterWithOpen(..))")
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
