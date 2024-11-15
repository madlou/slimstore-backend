package com.tjx.lew00305.slimstore.model;

import org.springframework.web.context.annotation.RequestScope;

import lombok.Data;

@Data
@RequestScope
public class View {

    private String name;
    private String title;
    private String message;
    private String formProcess;
    private String formSuccess;
    private FormElement[] formElements;
    private FunctionButton[] functionButtons;
    
}
