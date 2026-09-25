package cloud.matthews.slimstore.broadcast;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.ScopeNotActiveException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cloud.matthews.slimstore.basket.BasketService;
import cloud.matthews.slimstore.register.Register;
import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.store.Store;
import cloud.matthews.slimstore.store.StoreService;
import cloud.matthews.slimstore.tender.TenderService;
import cloud.matthews.slimstore.user.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BroadcastController {

    private final BasketService basketService;
    private final RegisterService registerService;
    private final StoreService storeService;
    private final TenderService tenderService;
    private final ObjectProvider<User> userProvider;

    private void setCurrentUser(
        BroadcastResponseDTO response
    ) {
        try {
            response.setUser(userProvider.getObject());
        } catch (ScopeNotActiveException e) {
            return;
        }
    }

    @MessageMapping("/connect")
    @SendTo("/topic/connected")
    public BroadcastResponseDTO connect(
        @Payload
        BroadcastRequestDTO request
    ) throws Exception {
        BroadcastResponseDTO response = new BroadcastResponseDTO();
        Store store = storeService.getStore(request.getStore());
        response.setStore(store);
        response.setRegister(registerService.getRegister(store, request.getRegister()));
        setCurrentUser(response);
        return response;
    }

    @MessageMapping("/disconnect")
    @SendTo("/topic/disconnected")
    public BroadcastResponseDTO disconnect(
        @Payload
        BroadcastRequestDTO request
    ) throws Exception {
        BroadcastResponseDTO response = new BroadcastResponseDTO();
        Store store = storeService.getStore(request.getStore());
        response.setStore(store);
        response.setRegister(registerService.getRegister(store, request.getRegister()));
        setCurrentUser(response);
        return response;
    }

    @GetMapping(path = "/api/broadcast/{store}/{register}")
    public BroadcastResponseDTO resendCurrentState(
        @PathVariable("store")
        Integer store,
        @PathVariable("register")
        Integer register
    ) {
        BroadcastResponseDTO response = new BroadcastResponseDTO();
        Store storeState = storeService.getStore(store);
        response.setStore(storeState);
        setCurrentUser(response);

        Register registerState = registerService.getRegister(storeState, register);
        if (registerState != null) {
            response.setRegister(registerState);
        }

        Session session = registerService.getSessionByRegister(store, register);
        if (session != null) {
            response.setBasket(new ArrayList<>(Arrays.asList(basketService.getBasketArray(session))));
            response.setTender(new ArrayList<>(Arrays.asList(tenderService.getTenderArray(session))));
        } else {
            response.setBasket(new ArrayList<>());
            response.setTender(new ArrayList<>());
        }

        return response;
    }

}
