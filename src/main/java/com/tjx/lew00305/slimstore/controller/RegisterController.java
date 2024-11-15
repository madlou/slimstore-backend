package com.tjx.lew00305.slimstore.controller;

import org.modelmapper.ModelMapper;
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
import com.tjx.lew00305.slimstore.service.TransactionService;
import com.tjx.lew00305.slimstore.service.UserService;
import com.tjx.lew00305.slimstore.service.ViewService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private GiftCardService giftCardService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping(path = "/api/register")
    public @ResponseBody RegisterResponseDTO registerQuery(@RequestBody RegisterRequestDTO registerRequest) {
        RegisterResponseDTO response = new RegisterResponseDTO();
        HttpSession session = request.getSession();
        System.out.println("Session ID: " + session.getId());
        UserDTO user = userService.getUserFromSession();
        System.out.println("User: " + ((user != null) ? user.getCode() : "Not logged in."));
        if(user == null && !registerRequest.getFormProcess().equals("Login")) {
            View view = viewService.getView("login");
            response.setView(view);
            return response;
        }
        if(!registerRequest.getFormProcess().isEmpty()) {
            switch (registerRequest.getFormProcess()) {
                case "Login":
                    String username = registerRequest.getFormElements()[0].getValue();
                    String password = registerRequest.getFormElements()[1].getValue();
                    user = userService.validateLogin(username, password);
                    if(user == null) {
                        View view = viewService.getView("login");
                        response.setView(view);
                        response.setError("Invalid login attempt.");
                        return response;
                    }
                    break;
                case "Logout":
                    userService.logout();
                    View view = viewService.getView("login");
                    response.setView(view);
                    return response;
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
        if(user != null) {
            response.setStore(locationService.getStore(123));
            response.setRegister(locationService.getRegister(123, 1));
            response.setBasket(basketService.getBasketArray());
            response.setUser(user);            
        }
        switch (action) {
            case "search":
                FormElement[] formElements = registerRequest.getFormElements();
                view.setFormElements(productService.onlineSearch(formElements[0].getValue()));
                break;
        }
        return response;
    }

}
