package com.tjx.lew00305.slimstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.dto.RegisterResponseDTO;
import com.tjx.lew00305.slimstore.dto.UserDTO;
import com.tjx.lew00305.slimstore.model.View;
import com.tjx.lew00305.slimstore.model.FormElement;
import com.tjx.lew00305.slimstore.service.ProductService;
//import com.tjx.lew00305.slimstore.service.TransactionService;
import com.tjx.lew00305.slimstore.service.UserService;
import com.tjx.lew00305.slimstore.service.ViewService;

import com.tjx.lew00305.slimstore.service.BasketService;
import com.tjx.lew00305.slimstore.service.GiftCardService;

import com.tjx.lew00305.slimstore.service.LocationService;

@RestController
public class RegisterController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private BasketService basketService;

    @Autowired
    private ProductService productService;

//    @Autowired
//    private TransactionService transactionService;

    @Autowired
    private GiftCardService giftCardService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/api/register")
    public @ResponseBody RegisterResponseDTO registerQuery(@RequestBody RegisterRequestDTO registerRequest) {
        RegisterResponseDTO response = new RegisterResponseDTO();
        UserDTO user = userService.getUserFromSession();
        if(user == null && !registerRequest.getFormProcess().equals("Login")) {
            response.setView(viewService.getView("login"));
            return response;
        }
        if(!registerRequest.getFormProcess().isEmpty()) {
            switch (registerRequest.getFormProcess()) {
                case "Login":
                    String username = registerRequest.getFormElements()[0].getValue();
                    String password = registerRequest.getFormElements()[1].getValue();
                    try {
                        user = userService.validateLogin(username, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }                        
                    if(user == null) {
                        response.setView(viewService.getView("login"));
                        response.setError("Invalid login attempt.");
                        return response;
                    }
                    break;
                case "Logout":
                    userService.logout();
                    response.setView(viewService.getView("login"));
                    return response;
                case "NewUser":
                    String a = registerRequest.getFormElements()[0].getValue();
                    String b = registerRequest.getFormElements()[1].getValue();
                    String c = registerRequest.getFormElements()[2].getValue();
                    String d = registerRequest.getFormElements()[3].getValue();
                    try {
                        userService.addUser(a, b, c, d);
                    } catch (Exception e) {
                        response.setView(viewService.getView("user-new"));
                        if(e.getMessage().contains("Duplicate entry")) {
                            response.setError("Unable to create user: Employee number already being used.");                            
                        } else {
                            response.setError("Unable to create user: " +  e.getMessage());
                        }
                        return response;
                    }
                    break;
                case "AddToBasket":
                    basketService.addFormElements(registerRequest.getFormElements());
                    break;
                case "EmptyBasket":
                    basketService.empty();
                    break;
                case "ProcessGiftcard":
                    String card = registerRequest.getFormElements()[0].getValue();
                    float value = Float.parseFloat(registerRequest.getFormElements()[1].getValue());
                    basketService.addFormElement(new FormElement("sale", "TJXGC", "Gift Card (" + card + ")", "1" ,null, value, null));
                    giftCardService.topup(card, value);
                    break;
            }
        }
        String action = registerRequest.getAction();
        action = action.isEmpty() ? "home" : action;
        View view = viewService.getView(action);
        response.setView(view);
        response.setStore(locationService.getStore(123));
        response.setRegister(locationService.getRegister(123, 1));
        response.setBasket(basketService.getBasketArray());
        response.setUser(user);            
        switch (action) {
            case "search":
                String search = registerRequest.getFormElements()[0].getValue();
                view.setFormElements(productService.onlineSearch(search));
                break;
            case "user-list":
                view.setFormElements(userService.getUsersAsFormElements());
                break;
        }
        return response;
    }

}
