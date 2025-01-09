package com.tjx.lew00305.slimstore.register;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tjx.lew00305.slimstore.barcode.Barcode;
import com.tjx.lew00305.slimstore.barcode.BarcodeService;
import com.tjx.lew00305.slimstore.basket.BasketService;
import com.tjx.lew00305.slimstore.giftcard.GiftCardService;
import com.tjx.lew00305.slimstore.location.LocationService;
import com.tjx.lew00305.slimstore.report.ReportService;
import com.tjx.lew00305.slimstore.tender.TenderService;
import com.tjx.lew00305.slimstore.transaction.TransactionService;
import com.tjx.lew00305.slimstore.translation.TranslationService;
import com.tjx.lew00305.slimstore.translation.UserInterfaceService;
import com.tjx.lew00305.slimstore.user.UserService;
import com.tjx.lew00305.slimstore.view.View;
import com.tjx.lew00305.slimstore.view.ViewService;
import com.tjx.lew00305.slimstore.view.View.ViewName;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RegisterController {
    
    private final BasketService basketService;
    private final GiftCardService giftCardService;
    private final LocationService locationService;
    private final TenderService tenderService;
    private final BarcodeService barcodeService;
    private final UserInterfaceService userInterfaceService;
    private final UserService userService;
    private final ViewService viewService;
    private final TransactionService transactionService;
    private final ReportService transactionReportService;
    private final TranslationService translationService;
    
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
                userService.loginByForm(request);
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
                basketService.addBasketByForm(giftCardService.topupByForm(request));
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
                // todo: fix error message
                // errorMessage = tenderService.addTenderByForm(request);
                tenderService.addTenderByForm(request);
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
