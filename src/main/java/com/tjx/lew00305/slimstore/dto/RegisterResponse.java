package com.tjx.lew00305.slimstore.dto;

import com.tjx.lew00305.slimstore.entity.Store;
import com.tjx.lew00305.slimstore.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.Flow;

import lombok.Data;

@Data
public class RegisterResponse {

    private Store store;
    private StoreRegister register;
    private Flow flow;
    private String[] auditLog;
    private UserDTO user;
    
}