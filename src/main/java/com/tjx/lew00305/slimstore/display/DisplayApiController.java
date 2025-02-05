package com.tjx.lew00305.slimstore.display;

import java.util.Locale;

import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tjx.lew00305.slimstore.basket.BasketLine;
import com.tjx.lew00305.slimstore.basket.BasketService;
import com.tjx.lew00305.slimstore.register.Register;
import com.tjx.lew00305.slimstore.register.RegisterService;
import com.tjx.lew00305.slimstore.store.Store;
import com.tjx.lew00305.slimstore.store.StoreService;
import com.tjx.lew00305.slimstore.tender.TenderLine;
import com.tjx.lew00305.slimstore.tender.TenderService;
import com.tjx.lew00305.slimstore.transaction.TransactionService;
import com.tjx.lew00305.slimstore.translation.Language;
import com.tjx.lew00305.slimstore.translation.UserInterfaceService;
import com.tjx.lew00305.slimstore.translation.UserInterfaceTranslationDTO;

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

    @GetMapping(path = "/api/languages")
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
    
    @GetMapping(path = "/api/ui/translations/{languageCode}")
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
