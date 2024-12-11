package com.tjx.lew00305.slimstore.model.common;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FormElement {
    
    public enum Type {
        BUTTON,
        DATE,
        DECIMAL,
        EMAIL,
        IMAGE,
        NUMBER,
        PASSWORD,
        PRODUCT,
        PRODUCT_DRINK,
        PRODUCT_WEB,
        PRODUCT_SCAN,
        PRODUCT_GIFTCARD,
        RETURN,
        SELECT,
        SUBMIT,
        TEXT,
    }
    
    private Type type;
    private String key;
    private String label;
    private String value;
    private String[] options;
    private Boolean disabled = false;
    private Boolean hidden = false;
    private String image;
    private BigDecimal price;
    private Integer quantity;
    private FormElementButton button;
    
}
