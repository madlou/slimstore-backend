package com.tjx.lew00305.slimstore.display;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.tjx.lew00305.slimstore.register.Register.RegisterStatus;
import com.tjx.lew00305.slimstore.register.RegisterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DisplayLocationMonitor {
    
    private final DisplayWebsocketController customerDisplayController;
    private final RegisterService registerService;

    @Pointcut("execution(public * com.tjx.lew00305.slimstore.register.RegisterService.updateRegisterWithClose(..))")
    private void aPointCutFromRegisterClose() {}

    @Pointcut("execution(public * com.tjx.lew00305.slimstore.register.RegisterService.updateRegisterWithOpen(..))")
    private void aPointCutFromRegisterOpen() {}
    
    @Pointcut("execution(public * com.tjx.lew00305.slimstore.register.RegisterService.updateRegisterWithTransaction(..))")
    private void aPointCutFromRegisterTransaction() {}
    
    @AfterReturning(pointcut = "aPointCutFromRegisterClose()")
    public void sendRegisterCloseToWebsocket() {
        customerDisplayController.sendRegisterStatus(RegisterStatus.CLOSED, registerService.getRegister().getLastTxnNumber());
    }

    @AfterReturning(pointcut = "aPointCutFromRegisterOpen()")
    public void sendRegisterOpenToWebsocket() {
        customerDisplayController.sendRegisterStatus(RegisterStatus.OPEN, registerService.getRegister().getLastTxnNumber() + 1);
    }

    @AfterReturning(pointcut = "aPointCutFromRegisterTransaction()", returning = "transactionNumber")
    public void sendRegisterTransactionToWebsocket(
        Integer transactionNumber
    ) {
        customerDisplayController.sendRegisterStatus(RegisterStatus.OPEN, transactionNumber);
    }

}
