package com.tjx.lew00305.slimstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormElement {
    
    private String type;
    private String key;
    private String label;
    private String value;
    private float price;
    private String[] options;

}
