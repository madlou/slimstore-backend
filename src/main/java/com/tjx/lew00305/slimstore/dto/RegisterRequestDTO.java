package com.tjx.lew00305.slimstore.dto;

import com.tjx.lew00305.slimstore.model.common.Form;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    
    private String action;
    private Form form;
    
}