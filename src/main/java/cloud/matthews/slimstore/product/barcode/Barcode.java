package cloud.matthews.slimstore.product.barcode;

import java.math.BigDecimal;

import cloud.matthews.slimstore.register.form.FormElement;
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
        element.setType(FormElement.FormElementType.PRODUCT_SCAN);
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
