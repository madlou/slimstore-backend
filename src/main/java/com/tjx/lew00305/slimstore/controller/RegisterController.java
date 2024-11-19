package com.tjx.lew00305.slimstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.dto.RegisterResponseDTO;
import com.tjx.lew00305.slimstore.dto.UserDTO;
import com.tjx.lew00305.slimstore.model.common.View;
import com.tjx.lew00305.slimstore.service.UserService;
import com.tjx.lew00305.slimstore.service.ViewService;

import com.tjx.lew00305.slimstore.service.BasketService;
import com.tjx.lew00305.slimstore.service.GiftCardService;

import com.tjx.lew00305.slimstore.service.LocationService;
import com.tjx.lew00305.slimstore.service.TenderService;

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
//    @Autowired
//    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private ViewService viewService;

    @PostMapping(path = "/api/register")
    public @ResponseBody RegisterResponseDTO registerQuery(@RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO response = new RegisterResponseDTO();
        UserDTO user = userService.getUserFromSession();
        if(user == null && !request.getFormProcess().equals("Login")) {
            response.setView(viewService.getViewByName("login"));
            return response;
        }
        if(!request.getFormProcess().isEmpty()) {
            switch (request.getFormProcess()) {
                case "Login":
                    user = userService.validateLoginByRequest(request);
                    if(user == null) {
                        response.setView(viewService.getViewByName("login"));
                        response.setError("Invalid login attempt.");
                        return response;
                    }
                    break;
                case "Logout":
                    userService.logout();
                    response.setView(viewService.getViewByName("login"));
                    return response;
                case "NewUser":
                    response = userService.addUserFromRequest(request, response);
                    if(!response.getError().isEmpty()) {
                        response.setView(viewService.getViewByName("user-new"));                        
                    }
                    break;
                case "AddToBasket":
                    basketService.addBasketByRequest(request);
                    break;
                case "EmptyBasket":
                    basketService.empty();
                    break;
                case "Tender":
                    tenderService.addTenderByRequest(request);
                    break;
                case "ProcessGiftcard":
                    basketService.addFormElement(giftCardService.topupByRequest(request));
                    break;
                case "TransactionComplete":
                    basketService.empty();
                    tenderService.empty();
                    break;
            }
        }
        View view = viewService.getViewByRequest(request);
        response.setView(view);
        response.setStore(locationService.getStore(123));
        response.setRegister(locationService.getRegister(123, 1));
        response.setBasket(basketService.getBasketArray());
        response.setTender(tenderService.getTenderArray());
        response.setUser(user);
        if(tenderService.isComplete()) {
            response.setView(viewService.getViewByName("complete"));
        }
        return response;
    }
    
}
