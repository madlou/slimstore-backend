package cloud.matthews.slimstore.display;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.tender.Tender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DisplayTenderMonitor {
    
    private final DisplayWebsocketController customerDisplayController;

    @Pointcut("execution(public * cloud.matthews.slimstore.tender.TenderService.addTenderByForm(..))")
    private void aPointCutFromAddTender() {}

    @Pointcut("execution(public * cloud.matthews.slimstore.tender.TenderService.empty(..))")
    private void aPointCutFromEmptyTender() {}
    
    @AfterReturning(pointcut = "aPointCutFromEmptyTender()")
    public void sendEmptyTenderToWebsocket() {
        customerDisplayController.sendTender(new Tender());
    }

    @AfterReturning(pointcut = "aPointCutFromAddTender()", returning = "tender")
    public void sendTenderToWebsocket(
        Tender tender
    ) {
        customerDisplayController.sendTender(tender);
    }

}
