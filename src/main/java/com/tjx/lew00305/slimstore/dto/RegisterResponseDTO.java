package com.tjx.lew00305.slimstore.dto;

import java.util.Collections;
import java.util.List;

import com.tjx.lew00305.slimstore.enums.Language;
import com.tjx.lew00305.slimstore.model.common.View;
import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.entity.User;
import com.tjx.lew00305.slimstore.model.session.BasketLine;
import com.tjx.lew00305.slimstore.model.session.TenderLine;

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
