package cloud.matthews.slimstore.tender.card;

import java.math.BigDecimal;

import cloud.matthews.slimstore.tender.card.Card.Status;
import lombok.Data;

@Data
public class CardResponseDTO {

    private Status status;
    private Integer reference;
    private BigDecimal amount;

}
