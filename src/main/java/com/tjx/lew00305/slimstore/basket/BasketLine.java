package com.tjx.lew00305.slimstore.basket;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tjx.lew00305.slimstore.register.form.FormElement.FormElementType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BasketLine implements Serializable {

    private String code;
    private String name;
    private FormElementType type;
    private Integer quantity;
    private BigDecimal unitValue;

    @JsonIgnore
    public BigDecimal getLineValue() {
        return unitValue.multiply(new BigDecimal(getSignedQuantity()));
    }

    @JsonIgnore
    public Integer getSignedQuantity() {
        Integer multiplier = (type == FormElementType.RETURN) ? -1 : 1;
        return quantity * multiplier;
    }

}
