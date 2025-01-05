package com.tjx.lew00305.slimstore.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.tjx.lew00305.slimstore.dto.CustomerDisplayRequestDTO;
import com.tjx.lew00305.slimstore.dto.CustomerDisplayResponseDTO;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister.RegisterStatus;
import com.tjx.lew00305.slimstore.model.session.Basket;
import com.tjx.lew00305.slimstore.model.session.Tender;
import com.tjx.lew00305.slimstore.service.LocationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CustomerDisplayController {

    private final SimpMessagingTemplate messagingTemplate;
    private final LocationService locationService;
    
    @MessageMapping("/connect")
    @SendTo("/topic/connected")
    public CustomerDisplayResponseDTO connect(
        @Payload
        CustomerDisplayRequestDTO request
    ) throws Exception {
        CustomerDisplayResponseDTO response = new CustomerDisplayResponseDTO();
        response.setStore(request.getStore());
        response.setRegister(request.getRegister());
        System.out.println("Connected: " + response);
        return response;
    }
    
    @MessageMapping("/disconnect")
    @SendTo("/topic/disconnected")
    public CustomerDisplayResponseDTO disconnect(
        @Payload
        CustomerDisplayRequestDTO request
    ) throws Exception {
        CustomerDisplayResponseDTO response = new CustomerDisplayResponseDTO();
        response.setStore(request.getStore());
        response.setRegister(request.getRegister());
        System.out.println("Disconnected: " + response);
        return response;
    }
    
    public void sendBasket(
        Basket basket
    ) {
        CustomerDisplayResponseDTO response = new CustomerDisplayResponseDTO();
        response.setBasket(basket.getArrayList());
        Integer storeNumber = locationService.getStore().getNumber();
        Integer registerNumber = locationService.getStoreRegister().getNumber();
        String topic = "/topic/" + storeNumber + "/" + registerNumber;
        messagingTemplate.convertAndSend(topic, response);
    }

    public void sendRegisterStatus(
        RegisterStatus status
    ) {
        CustomerDisplayResponseDTO response = new CustomerDisplayResponseDTO();
        response.setStatus(status);
        Integer storeNumber = locationService.getStore().getNumber();
        Integer registerNumber = locationService.getStoreRegister().getNumber();
        String topic = "/topic/" + storeNumber + "/" + registerNumber;
        messagingTemplate.convertAndSend(topic, response);
    }
    
    public void sendTender(
        Tender tender
    ) {
        CustomerDisplayResponseDTO response = new CustomerDisplayResponseDTO();
        response.setTender(tender.getArrayList());
        Integer storeNumber = locationService.getStore().getNumber();
        Integer registerNumber = locationService.getStoreRegister().getNumber();
        String topic = "/topic/" + storeNumber + "/" + registerNumber;
        messagingTemplate.convertAndSend(topic, response);
    }
    
}
