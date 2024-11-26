package com.tjx.lew00305.slimstore.model.common;

import lombok.Data;

@Data
public class Barcode {

    Integer division;
    Integer department;
    Integer category;
    Integer style;
    Float price;
    Integer week;
    
    public String getDepartmentCategory() {
        String output = "";
        if(getDivision() != null) {
            output += getDivision() + "/";
        }
        output += getDepartment() + "/";
        if(getCategory() != null) {
            output += getCategory();
        }
        return output;
    }
    
    public String getName() {
        String output = "";
        output += getStyle();
        return output;
    }
    
    @Override
    public String toString() {
        String output = "";
        if(getDivision() != null) {
            output += getDivision() + "/";
        }
        output += getDepartment() + "/";
        if(getCategory() != null) {
            output += getCategory() + "/";
        }
        output += getStyle();
        return output;
    }

    public FormElement getFormElement() {
        return new FormElement("product", getName(), getDepartmentCategory(), "1", "", getPrice(), null, null);
    }
}
