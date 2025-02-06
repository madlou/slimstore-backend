package cloud.matthews.slimstore.display;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import cloud.matthews.slimstore.basket.BasketLine;
import cloud.matthews.slimstore.register.Register.RegisterStatus;
import cloud.matthews.slimstore.tender.TenderLine;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisplayResponseDTO {

    private Integer store;
    private Integer register;
    @Enumerated(EnumType.STRING)
    private RegisterStatus status;
    private Integer transactionNumber;
    private ArrayList<BasketLine> basket;
    private ArrayList<TenderLine> tender;

}
