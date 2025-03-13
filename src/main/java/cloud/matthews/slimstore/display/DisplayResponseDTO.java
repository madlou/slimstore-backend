package cloud.matthews.slimstore.display;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

import cloud.matthews.slimstore.basket.BasketLine;
import cloud.matthews.slimstore.register.RegisterDTO;
import cloud.matthews.slimstore.tender.TenderLine;
import cloud.matthews.slimstore.tender.card.CardResponseDTO;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisplayResponseDTO {

    private Integer storeNumber;
    private Integer registerNumber;
    private ArrayList<BasketLine> basket;
    private ArrayList<TenderLine> tender;
    private RegisterDTO register;
    private CardResponseDTO cardResponse;
    private String token;
    private String error;

}
