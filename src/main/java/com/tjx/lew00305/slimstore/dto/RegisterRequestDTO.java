package com.tjx.lew00305.slimstore.dto;

import com.tjx.lew00305.slimstore.model.common.FormElement;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    
    private int storeNumber;
    private int registerNumber;
    private int userNumber;
    private String action;
    private String formProcess;
    private FormElement[] formElements;
    
}