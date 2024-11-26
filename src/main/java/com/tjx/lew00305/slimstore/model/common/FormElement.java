package com.tjx.lew00305.slimstore.model.common;

import lombok.Data;

@Data
public class FormElement {
    
    public enum Type {
        TEXT,
        EMAIL,
        NUMBER,
        DECIMAL,
        DATE,
        PASSWORD,
        SELECT,
        SUBMIT,
        BUTTON,
        IMAGE,
        PRODUCT,
        PRODUCT_DRINK,
        PRODUCT_WEB,
        PRODUCT_SCAN,
        PRODUCT_GIFTCARD,
    }
    
    private Type type;
    private String key;
    private String label;
    private String value;
    private String[] options;
    private Boolean disabled = false;
    private String image;
    private Float price;
    private Integer quantity;
    private FormElementButton button;

}
