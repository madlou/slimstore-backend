package cloud.matthews.slimstore.display;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import cloud.matthews.slimstore.basket.Basket;
import cloud.matthews.slimstore.register.Register.RegisterStatus;
import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.store.StoreService;
import cloud.matthews.slimstore.tender.Tender;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DisplayWebsocketController {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final RegisterService registerService;
    private final StoreService storeService;

    @MessageMapping("/connect")
    @SendTo("/topic/connected")
    public DisplayResponseDTO connect(
        @Payload
        DisplayRequestDTO request
    ) throws Exception {
        DisplayResponseDTO response = new DisplayResponseDTO();
        response.setStore(request.getStore());
        response.setRegister(request.getRegister());
        return response;
    }

    @MessageMapping("/disconnect")
    @SendTo("/topic/disconnected")
    public DisplayResponseDTO disconnect(
        @Payload
        DisplayRequestDTO request
    ) throws Exception {
        DisplayResponseDTO response = new DisplayResponseDTO();
        response.setStore(request.getStore());
        response.setRegister(request.getRegister());
        return response;
    }

    public void sendBasket(
        Basket basket
    ) {
        DisplayResponseDTO response = new DisplayResponseDTO();
        response.setBasket(basket.getArrayList());
        Integer storeNumber = storeService.getStore().getNumber();
        Integer registerNumber = registerService.getRegister().getNumber();
        String topic = "/topic/" + storeNumber + "/" + registerNumber;
        messagingTemplate.convertAndSend(topic, response);
    }
    
    public void sendRegisterStatus(
        RegisterStatus status,
        Integer transactionNumber
    ) {
        DisplayResponseDTO response = new DisplayResponseDTO();
        response.setStatus(status);
        response.setTransactionNumber(transactionNumber);
        Integer storeNumber = storeService.getStore().getNumber();
        Integer registerNumber = registerService.getRegister().getNumber();
        String topic = "/topic/" + storeNumber + "/" + registerNumber;
        messagingTemplate.convertAndSend(topic, response);
    }

    public void sendTender(
        Tender tender
    ) {
        DisplayResponseDTO response = new DisplayResponseDTO();
        response.setTender(tender.getArrayList());
        Integer storeNumber = storeService.getStore().getNumber();
        Integer registerNumber = registerService.getRegister().getNumber();
        String topic = "/topic/" + storeNumber + "/" + registerNumber;
        messagingTemplate.convertAndSend(topic, response);
    }

}
