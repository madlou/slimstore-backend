package com.tjx.lew00305.slimstore.model.session;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.web.context.annotation.SessionScope;

import com.tjx.lew00305.slimstore.model.common.FormElement.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SessionScope
public class BasketLine implements Serializable {
    
    private String code;
    private String name;
    private Type type;
    private Integer quantity;
    private BigDecimal unitValue;
    
    public Integer getSignedQuantity() {
        Integer multiplier = (type == Type.RETURN) ? -1 : 1;
        return quantity * multiplier;
    }
    
    public BigDecimal getLineValue() {
        return unitValue.multiply(new BigDecimal(getSignedQuantity()));
    }

}
