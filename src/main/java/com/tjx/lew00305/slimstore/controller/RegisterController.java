package com.tjx.lew00305.slimstore.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.dto.RegisterResponseDTO;
import com.tjx.lew00305.slimstore.model.common.Barcode;
import com.tjx.lew00305.slimstore.model.common.View;
import com.tjx.lew00305.slimstore.model.common.View.ViewName;
import com.tjx.lew00305.slimstore.service.BarcodeService;
import com.tjx.lew00305.slimstore.service.BasketService;
import com.tjx.lew00305.slimstore.service.GiftCardService;
import com.tjx.lew00305.slimstore.service.LocationService;
import com.tjx.lew00305.slimstore.service.TenderService;
import com.tjx.lew00305.slimstore.service.TransactionReportService;
import com.tjx.lew00305.slimstore.service.TransactionService;
import com.tjx.lew00305.slimstore.service.TranslationService;
import com.tjx.lew00305.slimstore.service.UserInterfaceService;
import com.tjx.lew00305.slimstore.service.UserService;
import com.tjx.lew00305.slimstore.service.ViewService;

@RestController
public class RegisterController {
    
    private BasketService basketService;
    private GiftCardService giftCardService;
    private LocationService locationService;
    private TenderService tenderService;
    private BarcodeService barcodeService;
    private UserInterfaceService userInterfaceService;
    private UserService userService;
    private ViewService viewService;
    private TransactionService transactionService;
    private TransactionReportService transactionReportService;
    private TranslationService translationService;
    
    public RegisterController(
        BasketService basketService,
        GiftCardService giftCardService,
        LocationService locationService,
        TenderService tenderService,
        BarcodeService barcodeService,
        UserInterfaceService userInterfaceService,
        UserService userService,
        ViewService viewService,
        TransactionService transactionService,
        TransactionReportService transactionReportService,
        TranslationService translationService
    ) {
        this.basketService = basketService;
        this.giftCardService = giftCardService;
        this.locationService = locationService;
        this.tenderService = tenderService;
        this.barcodeService = barcodeService;
        this.userInterfaceService = userInterfaceService;
        this.userService = userService;
        this.viewService = viewService;
        this.transactionService = transactionService;
        this.transactionReportService = transactionReportService;
        this.translationService = translationService;
    }
    
    @PostMapping(path = "/api/register")
    public @ResponseBody
    RegisterResponseDTO registerQuery(
        @RequestBody
        RegisterRequestDTO request,
        @CookieValue(name = "store-register", required = false)
        String storeRegCookie,
        String errorMessage
    ) {
        RegisterResponseDTO response = new RegisterResponseDTO();
        switch (request.getServerProcess()) {
            case null:
            default:
                break;
            case ADD_TO_BASKET:
                basketService.addBasketByForm(request);
                break;
            case CHANGE_REGISTER:
                errorMessage = locationService.setLocationByForm(request, userService.isUserAdmin());
                if (errorMessage != null) {
                    response.setError(translationService.translate("error.location_invalid"));
                    request.setTargetView(ViewName.REGISTER_CHANGE);
                }
                break;
            case EMPTY_BASKET:
                basketService.empty();
                tenderService.empty();
                break;
            case LOGIN:
                userService.validateLoginByForm(request);
                if (userService.isLoggedOut()) {
                    request.setTargetView(ViewName.LOGIN);
                    response.setError(translationService.translate("error.security_invalid_login"));
                }
                if (locationService.getStore() == null) {
                    request.setTargetView(ViewName.REGISTER_CHANGE);
                    response.setError(translationService.translate("error.location_enter_register"));
                }
                break;
            case LOGOUT:
                request.setTargetView(ViewName.LOGIN);
                userService.logout();
                break;
            case NEW_USER:
                errorMessage = userService.addUserByForm(request);
                break;
            case PROCESS_GIFTCARD:
                basketService.addFormElement(giftCardService.topupByForm(request));
                break;
            case RUN_REPORT:
                response.setReport(transactionReportService.runReportByForm(request));
                break;
            case SEARCH:
                Barcode barcode = barcodeService.getBarcodeByForm(request);
                if (barcode != null) {
                    basketService.addFormElement(barcode.getFormElement());
                    request.setTargetView(ViewName.HOME);
                }
                break;
            case SAVE_USER:
                errorMessage = userService.saveUserByForm(request);
                break;
            case STORE_SETUP:
                locationService.saveStoreByForm(request);
                break;
            case TENDER:
                errorMessage = tenderService.addTenderByForm(request);
                if (tenderService.isComplete()) {
                    request.setTargetView(ViewName.COMPLETE);
                    transactionService.addTransaction();
                }
                break;
            case TRANSACTION_COMPLETE:
                basketService.empty();
                tenderService.empty();
                break;
        }
        if (errorMessage != null) {
            response.setError(translationService.translate("error.error") + ": " + errorMessage);
        }
        return updateDTO(response, viewService.getViewByForm(request));
    }
    
    private RegisterResponseDTO updateDTO(
        RegisterResponseDTO response,
        View view
    ) {
        response.setView(view);
        response.setStore(locationService.getStore());
        response.setRegister(locationService.getStoreRegister());
        response.setBasket(basketService.getBasketArray());
        response.setTender(tenderService.getTenderArray());
        response.setUser(userService.getUser());
        response.setUiTranslations(userInterfaceService.getUserInterfaceTranslations());
        return response;
    }
    
}
