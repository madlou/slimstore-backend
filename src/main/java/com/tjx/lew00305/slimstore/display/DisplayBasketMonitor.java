package com.tjx.lew00305.slimstore.display;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.tjx.lew00305.slimstore.basket.Basket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DisplayBasketMonitor {

    private final DisplayWebsocketController customerDisplayController;
    
    @Pointcut("execution(public * com.tjx.lew00305.slimstore.basket.BasketService.add*ToBasketByForm(..))")
    private void aPointCutFromAddBasket() {}
    
    @Pointcut("execution(public * com.tjx.lew00305.slimstore.basket.BasketService.empty(..))")
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
