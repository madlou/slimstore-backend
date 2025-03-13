package cloud.matthews.slimstore.tender.card;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import cloud.matthews.slimstore.tender.TenderService;
import cloud.matthews.slimstore.tender.TenderWebsocket;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CardService {

    private final TenderService tenderService;
    private final TenderWebsocket tenderWebsocket;

    public Card getCard(){
        return tenderService.getTenderArrayList().getLast().getCard();
    }

    public void sendToPed(BigDecimal amount) {
        System.out.println("Sending to PED: " + amount);
    }

    public void messageFromPed(String message) {
        System.out.println("Message from PED: " + message);
    }

    public void updateCard(String reference, Card.Status status) {
        tenderService.updateCard(reference, status);
        tenderWebsocket.sendTender(tenderService.getTender());
    }

}
