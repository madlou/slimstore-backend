package cloud.matthews.slimstore.tender.card;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CardRequestDTO {

    private String cardNumber;
    private BigDecimal amount;
    private Integer pin;

}
