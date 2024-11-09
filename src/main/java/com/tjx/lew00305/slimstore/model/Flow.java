package com.tjx.lew00305.slimstore.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Flow {

    private String name;
    private String title;
    private String message;
    private String formValidation;
    private String formSuccess;
    private FormElement[] formElements;
    private FunctionButton[] functionButtons;
    
}
