package com.tjx.lew00305.slimstore.register;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.tjx.lew00305.slimstore.basket.BasketLine;
import com.tjx.lew00305.slimstore.register.view.View;
import com.tjx.lew00305.slimstore.store.StoreDTO;
import com.tjx.lew00305.slimstore.tender.TenderLine;
import com.tjx.lew00305.slimstore.translation.Language;
import com.tjx.lew00305.slimstore.translation.UserInterfaceTranslationDTO;
import com.tjx.lew00305.slimstore.user.UserDTO;

import lombok.Data;

@Data
public class RegisterResponseDTO {
    
    private BasketLine[] basket = new BasketLine[0];
    private String error = new String();
    private Language[] languages = Language.values();
    private RegisterDTO register = new RegisterDTO();
    @JsonRawValue
    private String report;
    private StoreDTO store = new StoreDTO();
    private TenderLine[] tender = new TenderLine[0];
    private UserInterfaceTranslationDTO uiTranslations;
    private UserDTO user;
    private View view = new View();
    // TODO: Pull out initial data from view
    // Private ArrayList<FormElement> viewValues = new ArrayList<FormElement>();
    
}
