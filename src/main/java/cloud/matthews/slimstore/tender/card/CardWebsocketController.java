package cloud.matthews.slimstore.tender.card;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import cloud.matthews.slimstore.tender.TenderService;
import cloud.matthews.slimstore.transaction.TransactionTender.TenderType;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CardWebsocketController {
    
    private final TenderService tenderService;

    @MessageMapping("/authorisation")
    @SendTo("/topic/card")
    public CardResponseDTO connect(
        @Payload
        CardRequestDTO request
    ) throws Exception {
        CardResponseDTO response = new CardResponseDTO();
        tenderService.getTenderArrayList().forEach(tenderLine -> {
            if (tenderLine.getType().equals(TenderType.CARD)) {
                tenderLine.getCard().setReference("PED: Authorised");
            }
        });
        return response;
    }

}
