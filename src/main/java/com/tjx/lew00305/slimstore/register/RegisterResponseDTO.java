package com.tjx.lew00305.slimstore.register;

import java.util.Collections;
import java.util.List;

import com.tjx.lew00305.slimstore.basket.BasketLine;
import com.tjx.lew00305.slimstore.location.Store;
import com.tjx.lew00305.slimstore.location.StoreRegister;
import com.tjx.lew00305.slimstore.tender.TenderLine;
import com.tjx.lew00305.slimstore.translation.Language;
import com.tjx.lew00305.slimstore.translation.UserInterfaceTranslationDTO;
import com.tjx.lew00305.slimstore.user.User;
import com.tjx.lew00305.slimstore.view.View;

import lombok.Data;

@Data
public class RegisterResponseDTO {
    
    private BasketLine[] basket = new BasketLine[0];
    private String error = new String();
    private Language[] languages = Language.values();
    private StoreRegister register = new StoreRegister();
    @SuppressWarnings("rawtypes")
    private List report = Collections.emptyList();
    private Store store = new Store();
    private TenderLine[] tender = new TenderLine[0];
    private UserInterfaceTranslationDTO uiTranslations;
    private User user = new User();
    private View view = new View();
    // TODO: Pull out initial data from view
    // Private ArrayList<FormElement> viewValues = new ArrayList<FormElement>();
    
}
