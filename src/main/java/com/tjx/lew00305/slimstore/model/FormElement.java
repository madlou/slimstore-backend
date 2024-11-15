package com.tjx.lew00305.slimstore.model;

import org.springframework.web.context.annotation.RequestScope;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequestScope
public class FormElement {
    
    private String type;
    private String key;
    private String label;
    private String value;
    private String image;
    private float price;
    private String[] options;

}
