package cloud.matthews.slimstore.display;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.basket.Basket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DisplayBasketMonitor {

    private final DisplayWebsocketController customerDisplayController;
    
    @Pointcut("execution(public * cloud.matthews.slimstore.basket.BasketService.*ByForm(..))")
    private void aPointCutFromAddBasket() {}
    
    @Pointcut("execution(public * cloud.matthews.slimstore.basket.BasketService.empty(..))")
    private void aPointCutFromEmptyBasket() {}

    @AfterReturning(pointcut = "aPointCutFromAddBasket()", returning = "basket")
    public void sendBasketToWebsocket(
        Basket basket
    ) {
        customerDisplayController.sendBasket(basket);
    }
    
    @AfterReturning(pointcut = "aPointCutFromEmptyBasket()")
    public void sendEmptyBasketToWebsocket() {
        customerDisplayController.sendBasket(new Basket());
    }
    
}
