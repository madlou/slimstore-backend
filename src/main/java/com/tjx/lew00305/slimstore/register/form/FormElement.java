package com.tjx.lew00305.slimstore.register.form;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormElement {

    public enum FormElementType {
        BUTTON,
        DATE,
        DECIMAL,
        ERROR,
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
        RETURN_MANUAL,
        SELECT,
        SUBMIT,
        TEXT,
    }

    private FormElementType type;
    private String key;
    private String label;
    private String value;
    private String[] options;
    private Boolean disabled = false;
    private Boolean hidden = false;
    private Boolean required = false;
    private String image;
    private BigDecimal price;
    private Integer quantity;
    private FormElementButton button;

}
