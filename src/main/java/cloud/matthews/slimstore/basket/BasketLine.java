package cloud.matthews.slimstore.basket;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cloud.matthews.slimstore.register.form.FormElement.FormElementType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BasketLine implements Serializable {

    private static final long serialVersionUID = 733821570830194269L;
    private String code;
    private String name;
    private FormElementType type;
    private Integer quantity;
    private BigDecimal unitValue;
    
    @JsonIgnore
    public BigDecimal getLineValue() {
        return unitValue.multiply(new BigDecimal(getSignedQuantity())).setScale(2, RoundingMode.CEILING);
    }

    @JsonIgnore
    public Integer getSignedQuantity() {
        Integer multiplier = ((type == FormElementType.RETURN) ||
            (type == FormElementType.RETURN_MANUAL)) ? -1 : 1;
        return quantity * multiplier;
    }

    public BigDecimal getUnitValue() {
        if (unitValue == null) {
            return null;
        }
        return unitValue.setScale(2, RoundingMode.CEILING);
    }

}
