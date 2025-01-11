package com.tjx.lew00305.slimstore.register;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.basket.BasketService;
import com.tjx.lew00305.slimstore.giftcard.GiftCardService;
import com.tjx.lew00305.slimstore.location.LocationService;
import com.tjx.lew00305.slimstore.product.barcode.Barcode;
import com.tjx.lew00305.slimstore.product.barcode.BarcodeService;
import com.tjx.lew00305.slimstore.register.view.View.ViewName;
import com.tjx.lew00305.slimstore.tender.TenderService;
import com.tjx.lew00305.slimstore.transaction.TransactionService;
import com.tjx.lew00305.slimstore.transaction.report.TransactionReportService;
import com.tjx.lew00305.slimstore.translation.TranslationService;
import com.tjx.lew00305.slimstore.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final BasketService basketService;
    private final GiftCardService giftCardService;
    private final LocationService locationService;
    private final TenderService tenderService;
    private final BarcodeService barcodeService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final TransactionReportService transactionReportService;
    private final TranslationService translationService;
    
    public RegisterResponseDTO process(
        RegisterRequestDTO request
    ) {
        RegisterResponseDTO response = new RegisterResponseDTO();
        try {
            switch (request.getServerProcess()) {
                case null:
                default:
                    break;
                case ADD_TO_BASKET:
                    basketService.addBasketByForm(request);
                    break;
                case CHANGE_REGISTER:
                    locationService.setLocationByForm(request, userService.isUserAdmin());
                    request.setTargetView(ViewName.REGISTER_CHANGE);
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
                    userService.addUserByForm(request);
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
                    userService.saveUserByForm(request);
                    break;
                case STORE_SETUP:
                    locationService.saveStoreByForm(request);
                    break;
                case TENDER:
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
        } catch (Exception e) {
            response.setError(translationService.translate("error.error") + ": " + e.getMessage());
        }
        return response;
    }

}
