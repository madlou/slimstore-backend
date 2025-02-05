package com.tjx.lew00305.slimstore.display;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.tjx.lew00305.slimstore.basket.Basket;
import com.tjx.lew00305.slimstore.register.Register.RegisterStatus;
import com.tjx.lew00305.slimstore.register.RegisterService;
import com.tjx.lew00305.slimstore.store.StoreService;
import com.tjx.lew00305.slimstore.tender.Tender;

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
