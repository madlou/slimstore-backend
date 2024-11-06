package com.tjx.lew00305.slimstore.model;

import lombok.Data;

@Data
public class FormElement {
    
    private String type;
    private String key;
    private String label;
    private String value;
    private String[] options;

}
