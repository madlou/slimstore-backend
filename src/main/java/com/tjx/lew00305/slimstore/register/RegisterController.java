package com.tjx.lew00305.slimstore.register;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tjx.lew00305.slimstore.basket.BasketService;
import com.tjx.lew00305.slimstore.location.LocationService;
import com.tjx.lew00305.slimstore.register.view.ViewService;
import com.tjx.lew00305.slimstore.tender.TenderService;
import com.tjx.lew00305.slimstore.translation.UserInterfaceService;
import com.tjx.lew00305.slimstore.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;
    private final BasketService basketService;
    private final LocationService locationService;
    private final TenderService tenderService;
    private final UserInterfaceService userInterfaceService;
    private final UserService userService;
    private final ViewService viewService;

    @PostMapping(path = "/api/register")
    public @ResponseBody
    RegisterResponseDTO registerQuery(
        @RequestBody
        RegisterRequestDTO request,
        String errorMessage
    ) {
        RegisterResponseDTO response;
        if (errorMessage != null) {
            response = new RegisterResponseDTO();
            response.setError(errorMessage);
        } else {
            response = registerService.process(request);
        }
        return updateDTO(request, response);
    }

    private RegisterResponseDTO updateDTO(
        RegisterRequestDTO request,
        RegisterResponseDTO response
    ) {
        response.setView(viewService.getViewByForm(request));
        response.setStore(locationService.getStore());
        response.setRegister(locationService.getStoreRegister());
        response.setBasket(basketService.getBasketArray());
        response.setTender(tenderService.getTenderArray());
        response.setUser(userService.getUser());
        response.setUiTranslations(userInterfaceService.getUserInterfaceTranslations());
        return response;
    }
    
}
