package com.tjx.lew00305.slimstore.barcode;

import java.math.BigDecimal;

import com.tjx.lew00305.slimstore.register.FormElement;

import lombok.Data;

@Data
public class Barcode {

    Integer division;
    Integer department;
    Integer category;
    Integer style;
    BigDecimal price;
    Integer week;
    
    public String getDepartmentCategory() {
        String output = "";
        if (getDivision() != null) {
            output += getDivision() + "/";
        }
        output += getDepartment() + "/";
        if (getCategory() != null) {
            output += getCategory();
        }
        return output;
    }
    
    public FormElement getFormElement() {
        FormElement element = new FormElement();
        element.setType(FormElement.Type.PRODUCT_SCAN);
        element.setKey(getName());
        element.setLabel(getDepartmentCategory());
        element.setQuantity(1);
        element.setPrice(getPrice());
        return element;
    }
    
    public String getName() {
        String output = "";
        output += getStyle();
        return output;
    }
    
    @Override
    public String toString() {
        String output = "";
        if (getDivision() != null) {
            output += getDivision() + "/";
        }
        output += getDepartment() + "/";
        if (getCategory() != null) {
            output += getCategory() + "/";
        }
        output += getStyle();
        return output;
    }
}
