package cloud.matthews.slimstore.display;

import java.util.Locale;

import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cloud.matthews.slimstore.basket.BasketLine;
import cloud.matthews.slimstore.basket.BasketService;
import cloud.matthews.slimstore.register.Register;
import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.store.Store;
import cloud.matthews.slimstore.store.StoreService;
import cloud.matthews.slimstore.tender.TenderLine;
import cloud.matthews.slimstore.tender.TenderService;
import cloud.matthews.slimstore.transaction.TransactionService;
import cloud.matthews.slimstore.translation.Language;
import cloud.matthews.slimstore.translation.UserInterfaceService;
import cloud.matthews.slimstore.translation.UserInterfaceTranslationDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DisplayApiController {
    
    private final BasketService basketService;
    private final RegisterService registerService;
    private final StoreService storeService;
    private final TenderService tenderService;
    private final TransactionService transactionService;
    private final UserInterfaceService userInterfaceService;

    private final DisplaySession displaySession;
    
    @GetMapping(path = "/api/public/display/authentication/{storeNumber}/{registerNumber}/{pin}")
    public DisplayResponseDTO authenticate(
        @PathVariable("storeNumber")
        Integer storeNumber,
        @PathVariable("registerNumber")
        Integer registerNumber,
        @PathVariable("pin")
        Integer pin
    ) {
        DisplayResponseDTO response = new DisplayResponseDTO();
        response.setStore(storeNumber);
        response.setRegister(registerNumber);
        Register register = registerService.getRegister(storeNumber, registerNumber);
        if(register !=null && register.getCustomerDisplayPin().equals(pin)){
            displaySession.setStoreNumber(storeNumber);
            displaySession.setRegisterNumber(registerNumber);
            displaySession.setRegisterSessionId(register.getSessionId());
            displaySession.setAuthenticated(true);
            response.setToken(registerService.generateDisplayToken(storeNumber, registerNumber));
            return response;
        }
        response.setError("Authentication Failed.");
        return response;
    }

    @GetMapping(path = "/api/location/{storeNumber}")
    public Store getAllUsers(
        @PathVariable("storeNumber")
        Integer storeNumber
    ) {
        return storeService.getStore(storeNumber);
    }

    @GetMapping(path = "/api/location/{storeNumber}/{registerNumber}")
    public Register getAllUsers(
        @PathVariable("storeNumber")
        Integer storeNumber,
        @PathVariable("registerNumber")
        Integer registerNumber
    ) {
        return registerService.getRegister(storeNumber, registerNumber);
    }

    @GetMapping(path = "/api/basket/{storeNumber}/{registerNumber}")
    public BasketLine[] getBasket(
        @PathVariable("storeNumber")
        Integer storeNumber,
        @PathVariable("registerNumber")
        Integer registerNumber
    ) {
        Session session = registerService.getSessionByRegister(storeNumber, registerNumber);
        return basketService.getBasketArray(session);
    }

    @GetMapping(path = "/api/public/languages")
    public Language[] getLanguages() {
        return Language.values();
    }

    @GetMapping(path = "/api/tender/{storeNumber}/{registerNumber}")
    public TenderLine[] getTender(
        @PathVariable("storeNumber")
        Integer storeNumber,
        @PathVariable("registerNumber")
        Integer registerNumber
    ) {
        Session session = registerService.getSessionByRegister(storeNumber, registerNumber);
        return tenderService.getTenderArray(session);
    }
    
    @GetMapping(path = "/api/public/translations/{languageCode}")
    public UserInterfaceTranslationDTO getUiTranslations(
        @PathVariable("languageCode")
        String languageCode
    ) {
        return userInterfaceService.getUserInterfaceTranslations(Locale.of(languageCode));
    }
    
    @GetMapping(path = "/api/review/add/{storeNumber}/{registerNumber}/{transactionNumber}/{score}")
    public void updateReviewScore(
        @PathVariable("storeNumber")
        Integer storeNumber,
        @PathVariable("registerNumber")
        Integer registerNumber,
        @PathVariable("transactionNumber")
        Integer transactionNumber,
        @PathVariable("score")
        Integer score
    ) {
        transactionService.addReview(storeNumber, registerNumber, transactionNumber, score);
    }

}
