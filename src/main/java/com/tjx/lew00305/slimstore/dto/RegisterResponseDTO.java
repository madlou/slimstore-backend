package com.tjx.lew00305.slimstore.dto;

import java.util.Collections;
import java.util.List;

import com.tjx.lew00305.slimstore.model.common.View;
import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.entity.User;
import com.tjx.lew00305.slimstore.model.session.BasketLine;
import com.tjx.lew00305.slimstore.model.session.TenderLine;

import lombok.Data;

@Data
public class RegisterResponseDTO {

    private Store store = new Store();
    private StoreRegister register = new StoreRegister();
    private View view = new View();
    private BasketLine[] basket = new BasketLine[0];
    private TenderLine[] tender = new TenderLine[0];
    private User user = new User();
    private String error = new String();
    @SuppressWarnings("rawtypes")
    private List report = Collections.emptyList();
    private UserInterfaceTranslationDTO uiTranslations;

}