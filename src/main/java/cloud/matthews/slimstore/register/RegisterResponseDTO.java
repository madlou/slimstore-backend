package cloud.matthews.slimstore.register;

import com.fasterxml.jackson.annotation.JsonRawValue;
import cloud.matthews.slimstore.basket.BasketLine;
import cloud.matthews.slimstore.register.view.View;
import cloud.matthews.slimstore.store.StoreDTO;
import cloud.matthews.slimstore.tender.TenderLine;
import cloud.matthews.slimstore.translation.Language;
import cloud.matthews.slimstore.translation.UserInterfaceTranslationDTO;
import cloud.matthews.slimstore.user.UserDTO;

import lombok.Data;

@Data
public class RegisterResponseDTO {
    
    private BasketLine[] basket = new BasketLine[0];
    private String error = new String();
    private Language[] languages = Language.values();
    private RegisterDTO register = new RegisterDTO();
    @JsonRawValue
    private String report;
    private StoreDTO store = new StoreDTO();
    private TenderLine[] tender = new TenderLine[0];
    private UserInterfaceTranslationDTO uiTranslations;
    private UserDTO user;
    private View view = new View();
    // TODO: Pull out initial data from view
    // Private ArrayList<FormElement> viewValues = new ArrayList<FormElement>();
    
}
