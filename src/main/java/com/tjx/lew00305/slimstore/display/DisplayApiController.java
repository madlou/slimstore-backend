package com.tjx.lew00305.slimstore.display;

import java.util.Locale;

import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tjx.lew00305.slimstore.basket.BasketLine;
import com.tjx.lew00305.slimstore.basket.BasketService;
import com.tjx.lew00305.slimstore.location.LocationService;
import com.tjx.lew00305.slimstore.location.Store;
import com.tjx.lew00305.slimstore.location.StoreRegister;
import com.tjx.lew00305.slimstore.tender.TenderLine;
import com.tjx.lew00305.slimstore.tender.TenderService;
import com.tjx.lew00305.slimstore.translation.Language;
import com.tjx.lew00305.slimstore.translation.UserInterfaceService;
import com.tjx.lew00305.slimstore.translation.UserInterfaceTranslationDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DisplayApiController {
    
    private final BasketService basketService;
    private final LocationService locationService;
    private final TenderService tenderService;
    private final UserInterfaceService userInterfaceService;
    
    @GetMapping(path = "/api/location/{storeNumber}")
    public Store getAllUsers(
        @PathVariable("storeNumber")
        Integer storeNumber
    ) {
        return locationService.getStore(storeNumber);
    }

    @GetMapping(path = "/api/location/{storeNumber}/{registerNumber}")
    public StoreRegister getAllUsers(
        @PathVariable("storeNumber")
        Integer storeNumber,
        @PathVariable("registerNumber")
        Integer registerNumber
    ) {
        Store store = locationService.getStore(storeNumber);
        if (store == null) {
            return null;
        }
        return store.getRegisterByNumber(registerNumber);
    }

    @GetMapping(path = "/api/basket/{storeNumber}/{registerNumber}")
    public BasketLine[] getBasket(
        @PathVariable("storeNumber")
        Integer storeNumber,
        @PathVariable("registerNumber")
        Integer registerNumber
    ) {
        Session session = locationService.getSessionByStoreRegister(storeNumber, registerNumber);
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
        Session session = locationService.getSessionByStoreRegister(storeNumber, registerNumber);
        return tenderService.getTenderArray(session);
    }
    
    @GetMapping(path = "/api/ui/translations/{languageCode}")
    public UserInterfaceTranslationDTO getUiTranslations(
        @PathVariable("languageCode")
        String languageCode
    ) {
        return userInterfaceService.getUserInterfaceTranslations(Locale.of(languageCode));
    }
    
}
