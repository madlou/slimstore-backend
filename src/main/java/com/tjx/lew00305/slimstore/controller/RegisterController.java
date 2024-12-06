package com.tjx.lew00305.slimstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.dto.RegisterResponseDTO;
import com.tjx.lew00305.slimstore.model.common.Barcode;
import com.tjx.lew00305.slimstore.model.common.Form.ServerProcess;
import com.tjx.lew00305.slimstore.model.common.View;
import com.tjx.lew00305.slimstore.model.common.View.ViewName;
import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.service.UserService;
import com.tjx.lew00305.slimstore.service.ViewService;

import com.tjx.lew00305.slimstore.service.BarcodeService;
import com.tjx.lew00305.slimstore.service.BasketService;
import com.tjx.lew00305.slimstore.service.GiftCardService;
import com.tjx.lew00305.slimstore.service.LocationService;
import com.tjx.lew00305.slimstore.service.TenderService;
import com.tjx.lew00305.slimstore.service.TransactionReportService;
import com.tjx.lew00305.slimstore.service.TransactionService;
import com.tjx.lew00305.slimstore.service.UserInterfaceService;

@RestController
public class RegisterController {

    @Autowired
    private BasketService basketService;
    @Autowired
    private GiftCardService giftCardService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private TenderService tenderService;
    @Autowired
    private BarcodeService barcodeService;
    @Autowired
    private UserInterfaceService userInterfaceService;
    @Autowired
    private UserService userService;
    @Autowired
    private ViewService viewService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionReportService transactionReportService;

    @PostMapping(path = "/api/register")
    public @ResponseBody RegisterResponseDTO registerQuery(
        @RequestBody RegisterRequestDTO requestForm,
        @CookieValue(name = "store-register", required = false) String storeRegCookie
    ) {
        String errorMessage = null;
        RegisterResponseDTO response = new RegisterResponseDTO();
        if(userService.isLoggedOut() && requestForm.getServerProcess() != ServerProcess.LOGIN) {
            return updateDTO(response, viewService.getViewByName(ViewName.LOGIN));
        }
        if(requestForm.getServerProcess() != null) {
            switch (requestForm.getServerProcess()) {
                case ADD_TO_BASKET:
                    basketService.addBasketByForm(requestForm);
                    break;
                case CHANGE_REGISTER:
                    errorMessage = locationService.validateLocationByForm(requestForm);
                    if(errorMessage != null) {
                        response.setError("Invalid location details.");
                        requestForm.setTargetView(ViewName.REGISTER_CHANGE);
                    }
                    break;
                case EMPTY_BASKET:
                    basketService.empty();
                    tenderService.empty();
                    break;
                case LOGIN:
                    userService.validateLoginByForm(requestForm);
                    if(userService.isLoggedOut()) {
                        response.setError("Invalid login attempt.");
                        return updateDTO(response, viewService.getViewByName(ViewName.LOGIN));
                    }
                    break;
                case LOGOUT:
                    userService.logout();
                    return updateDTO(response, viewService.getViewByName(ViewName.LOGIN));
                case NEW_USER:
                    errorMessage = userService.addUserByForm(requestForm);
                    break;
                case PROCESS_GIFTCARD:
                    basketService.addFormElement(giftCardService.topupByForm(requestForm));
                    break;
                case RUN_REPORT:
                    response.setReport(transactionReportService.runReportByForm(requestForm));
                    break;
                case SEARCH:
                    Barcode barcode = barcodeService.getBarcodeByForm(requestForm);
                    if(barcode != null) {
                        basketService.addFormElement(barcode.getFormElement());
                        requestForm.setTargetView(ViewName.HOME);
                    }
                    break;
                case SAVE_USER:
                    errorMessage = userService.saveUserByForm(requestForm);
                    break;
                case STORE_SETUP:
                    locationService.updateStoreByForm(requestForm);
                    break;
                case TENDER:
                    errorMessage = tenderService.addTenderByForm(requestForm);
                    if(tenderService.isComplete()) {
                        transactionService.addTransaction();
                        return updateDTO(response, viewService.getViewByName(ViewName.COMPLETE));                
                    }
                    break;
                case TRANSACTION_COMPLETE:
                    basketService.empty();
                    tenderService.empty();
                    break;
            }
        }
        Store store = locationService.getStore();
        if(store == null && storeRegCookie != null) {
            String[] storeRegCookieSplit = storeRegCookie.split("-");
            locationService.setLocation(
                Integer.parseInt(storeRegCookieSplit[0]),
                Integer.parseInt(storeRegCookieSplit[1])
            );
            store = locationService.getStore();
        }
        if(store == null && requestForm.getServerProcess() != ServerProcess.CHANGE_REGISTER) {
            requestForm.setTargetView(ViewName.REGISTER_CHANGE);
            response.setError("Store and register setup required.");
        }
        if(errorMessage != null) {
            response.setError("ERROR: " + errorMessage);
        }
        return updateDTO(response, viewService.getViewByForm(requestForm));
    }
    
    private RegisterResponseDTO updateDTO(RegisterResponseDTO response, View view) {
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
