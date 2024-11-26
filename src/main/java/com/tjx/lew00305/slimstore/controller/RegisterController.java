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
        if(userService.isLoggedOut() && !requestForm.getServerProcess().equals("Login")) {
            response.setView(viewService.getViewByName(ViewName.LOGIN));
            return response;
        }
        if(!requestForm.getServerProcess().isEmpty()) {
            switch (requestForm.getServerProcess()) {
                case "Login":
                    userService.validateLoginByForm(requestForm);
                    if(userService.isLoggedOut()) {
                        response.setView(viewService.getViewByName(ViewName.LOGIN));
                        response.setError("Invalid login attempt.");
                        return response;
                    }
                    break;
                case "Logout":
                    userService.logout();
                    response.setView(viewService.getViewByName(ViewName.LOGIN));
                    return response;
                case "StoreSetup":
                    locationService.updateStoreByForm(requestForm);
                    break;
                case "ChangeRegister":
                    errorMessage = locationService.validateLocationByForm(requestForm);
                    if(errorMessage != null) {
                        response.setError("Invalid location details.");
                        requestForm.setTargetView(ViewName.REGISTER_CHANGE.toString());
                    }
                    break;
                case "Search":
                    Barcode barcode = barcodeService.getBarcodeByForm(requestForm);
                    if(barcode != null) {
                        basketService.addFormElement(barcode.getFormElement());
                        requestForm.setTargetView("home");
                    }
                    break;
                case "NewUser":
                    errorMessage = userService.addUserByForm(requestForm);
                    break;
                case "SaveUser":
                    errorMessage = userService.saveUserByForm(requestForm);
                    break;
                case "AddToBasket":
                    basketService.addBasketByForm(requestForm);
                    break;
                case "Tender":
                    tenderService.addTenderByForm(requestForm);
                    break;
                case "ProcessGiftcard":
                    basketService.addFormElement(giftCardService.topupByForm(requestForm));
                    break;
                case "EmptyBasket":
                case "TransactionComplete":
                    basketService.empty();
                    tenderService.empty();
                    break;
                case "RunReport":
                    response.setReport(transactionReportService.runReportByForm(requestForm));
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
        if(store == null && !requestForm.getServerProcess().equals("ChangeRegister")) {
            requestForm.setTargetView(ViewName.REGISTER_CHANGE.toString());
            response.setError("Store and register setup required.");
        }
        View view = viewService.getViewByForm(requestForm);
        response.setView(view);
        response.setStore(locationService.getStore());
        response.setRegister(locationService.getStoreRegister());
        response.setBasket(basketService.getBasketArray());
        response.setTender(tenderService.getTenderArray());
        response.setUser(userService.getUser());
        if(tenderService.isComplete()) {
            transactionService.addTransaction();
            response.setView(viewService.getViewByName(ViewName.COMPLETE));                
        }
        if(errorMessage != null) {
            response.setError("ERROR: " + errorMessage);
        }
        return response;
    }
    
}
