package com.tjx.lew00305.slimstore.dto;

import com.tjx.lew00305.slimstore.entity.Store;
import com.tjx.lew00305.slimstore.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.BasketLine;
import com.tjx.lew00305.slimstore.model.View;

import lombok.Data;

@Data
public class RegisterResponseDTO {

    private Store store;
    private StoreRegister register;
    private View view;
    private BasketLine[] basket;
    private UserDTO user;
    
}