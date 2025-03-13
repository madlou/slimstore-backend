package cloud.matthews.slimstore.basket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.display.DisplayResponseDTO;
import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.store.StoreService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BasketWebsocket {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final RegisterService registerService;
    private final StoreService storeService;

    public void sendBasket(
        Basket basket
    ) {
        DisplayResponseDTO response = new DisplayResponseDTO();
        response.setBasket(basket.getArrayList());
        String topic = "/topic/" + 
            storeService.getStore().getNumber() + 
            "/" + 
            registerService.getRegister().getNumber();
        messagingTemplate.convertAndSend(topic, response);
    }
    
}
