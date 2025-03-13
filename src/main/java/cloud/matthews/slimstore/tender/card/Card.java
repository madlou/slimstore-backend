package cloud.matthews.slimstore.tender.card;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Card {
    
    public enum Status {
        AUTHORISED,
        CANCELLED,
        DECLINED,
        ERROR,
        INITIAL,
        PENDING,
        TIMEOUT,
    }

    private String cardNumber;
    private BigDecimal amount;
    private Integer pin;
    private Status status;
    private String reference;

}
