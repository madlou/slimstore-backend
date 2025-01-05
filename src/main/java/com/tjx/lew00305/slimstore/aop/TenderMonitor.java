package com.tjx.lew00305.slimstore.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.tjx.lew00305.slimstore.controller.CustomerDisplayController;
import com.tjx.lew00305.slimstore.model.session.Tender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class TenderMonitor {
    
    private final CustomerDisplayController customerDisplayController;

    @Pointcut("execution(public * com.tjx.lew00305.slimstore.service.TenderService.addTenderByForm(..))")
    private void aPointCutFromAddTender() {}

    @Pointcut("execution(public * com.tjx.lew00305.slimstore.service.TenderService.empty(..))")
    private void aPointCutFromEmptyTender() {}
    
    @AfterReturning(pointcut = "aPointCutFromEmptyTender()")
    public void sendEmptyTenderToWebsocket() {
        customerDisplayController.sendTender(new Tender());
    }

    @AfterReturning(pointcut = "aPointCutFromAddTender()", returning = "tender")
    public void sendTenderToWebsocket(
        Tender tender
    ) {
        System.out.println(tender.getArrayList().size());
        customerDisplayController.sendTender(tender);
    }

}
