package cloud.matthews.slimstore.broadcast;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.basket.Basket;
import cloud.matthews.slimstore.basket.BasketChangeListener;
import cloud.matthews.slimstore.register.Register;
import cloud.matthews.slimstore.register.Register.RegisterStatus;
import cloud.matthews.slimstore.register.RegisterChangeListener;
import cloud.matthews.slimstore.tender.Tender;
import cloud.matthews.slimstore.tender.TenderChangeListener;
import cloud.matthews.slimstore.user.User;
import cloud.matthews.slimstore.user.UserChangeListener;
import lombok.RequiredArgsConstructor;

/**
 * Pushes basket, register and tender changes to the customer display
 * over websocket, as soon as they happen. Registered explicitly as a
 * listener with each domain service rather than relying on AOP, so the
 * notification path is visible directly at each call site.
 */
@Component
@RequiredArgsConstructor
public class BroadcastNotifier implements BasketChangeListener, RegisterChangeListener, TenderChangeListener, UserChangeListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final Register register;

    private String topic() {
        Integer storeNumber = register.getStore().getNumber();
        Integer registerNumber = register.getNumber();
        return "/topic/" + storeNumber + "/" + registerNumber;
    }

    private void send(
        BroadcastResponseDTO response
    ) {
        messagingTemplate.convertAndSend(topic(), response);
    }

    @Override
    public void onBasketChanged(
        Basket basket
    ) {
        BroadcastResponseDTO response = new BroadcastResponseDTO();
        response.setBasket(basket.getArrayList());
        send(response);
    }

    @Override
    public void onRegisterStatusChanged(
        RegisterStatus status,
        Integer transactionNumber
    ) {
        BroadcastResponseDTO response = new BroadcastResponseDTO();
        response.setRegister(register);
        send(response);
    }

    @Override
    public void onTenderChanged(
        Tender tender
    ) {
        BroadcastResponseDTO response = new BroadcastResponseDTO();
        response.setTender(tender.getArrayList());
        send(response);
    }

    @Override
    public void onUserChanged(
        User user
    ) {
        BroadcastResponseDTO response = new BroadcastResponseDTO();
        response.setUser(user);
        send(response);
    }

}
