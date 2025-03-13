package cloud.matthews.slimstore.tender.card;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CardController {

    private final CardService cardService;
    private final ModelMapper modelMapper;

    @GetMapping("/api/card/authorisation")
    public CardResponseDTO getMethodName(@RequestParam CardRequestDTO cardRequest) {
        cardService.updateCard(null, Card.Status.PENDING);
        try {
            System.out.println("Processing card payment: " + cardRequest.getCardNumber() + '@' + cardRequest.getAmount());
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        var cardResponse = new CardResponseDTO();
        if(cardRequest.getPin().toString().substring(3, 3) == "9"){
            cardService.updateCard(null, Card.Status.DECLINED);
            cardResponse.setStatus(Card.Status.DECLINED);
        } else {
            cardService.updateCard("123456", Card.Status.AUTHORISED);
            cardResponse.setStatus(Card.Status.AUTHORISED);
        }
        cardResponse.setAmount(cardRequest.getAmount());
        cardResponse.setReference(123456);
        cardResponse = modelMapper.map(cardService.getCard(), CardResponseDTO.class);
        return cardResponse;
    }
    
}
