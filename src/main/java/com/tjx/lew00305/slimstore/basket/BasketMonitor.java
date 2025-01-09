package com.tjx.lew00305.slimstore.basket;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.tjx.lew00305.slimstore.display.DisplayWebsocketController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class BasketMonitor {

    private final DisplayWebsocketController customerDisplayController;
    
    @Pointcut("execution(public * com.tjx.lew00305.slimstore.basket.BasketService.addBasketByForm(..))")
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
