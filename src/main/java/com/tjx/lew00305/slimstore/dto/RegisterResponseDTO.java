package com.tjx.lew00305.slimstore.dto;

import com.tjx.lew00305.slimstore.entity.Store;
import com.tjx.lew00305.slimstore.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.BasketLine;
import com.tjx.lew00305.slimstore.model.View;

import lombok.Data;

@Data
public class RegisterResponseDTO {

    private Store store = new Store();
    private StoreRegister register = new StoreRegister();
    private View view = new View();
    private BasketLine[] basket = new BasketLine[0];
    private UserDTO user = new UserDTO();
    private String error = new String();
    
}