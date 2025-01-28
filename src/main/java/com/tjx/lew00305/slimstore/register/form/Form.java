package com.tjx.lew00305.slimstore.register.form;

import java.math.BigDecimal;

import com.tjx.lew00305.slimstore.register.view.View.ViewName;

import lombok.Data;

@Data
public class Form {
    
    public enum ServerProcess {
        ADD_TO_BASKET,
        ADD_MANUAL_RETURN_TO_BASKET,
        CHANGE_REGISTER,
        EMPTY_BASKET,
        LOGIN,
        LOGOUT,
        NEW_USER,
        PROCESS_GIFTCARD,
        RUN_REPORT,
        SAVE_USER,
        SEARCH,
        STORE_SETUP,
        TENDER,
        TRANSACTION_COMPLETE,
    }
    
    private ViewName targetView;
    private ServerProcess serverProcess;
    private FormElement[] elements = new FormElement[0];
    
    public void addElement(
        FormElement newElement
    ) {
        Integer size = elements.length;
        FormElement[] newElements = new FormElement[size + 1];
        Integer counter = 0;
        for (FormElement element : elements) {
            newElements[counter++] = element;
        }
        newElements[size] = newElement;
        elements = newElements;
    }

    public void addElementBeginning(
        FormElement newElement
    ) {
        Integer size = elements.length;
        FormElement[] newElements = new FormElement[size + 1];
        newElements[0] = newElement;
        Integer counter = 1;
        for (FormElement element : elements) {
            newElements[counter++] = element;
        }
        elements = newElements;
    }
    
    public void deleteElements() {
        elements = new FormElement[0];
    }

    public void deleteElementsAfter(
        Integer after
    ) {
        if (after > elements.length) {
            after = elements.length;
        }
        FormElement[] newElements = new FormElement[after + 1];
        for (Integer counter = 0; counter <= after; counter++) {
            newElements[counter] = elements[counter];
        }
        elements = newElements;
    }
    
    public FormElement findByKey(
        String key
    ) {
        if (elements == null) {
            return null;
        }
        for (FormElement element : elements) {
            if (element.getKey().equals(key)) {
                return element;
            }
        }
        return null;
    }
    
    public BigDecimal getBigDecimalValueByKey(
        String key
    ) {
        FormElement element = findByKey(key);
        if (element == null) {
            return null;
        }
        return new BigDecimal(element.getValue());
    }
    
    public Float getFloatValueByKey(
        String key
    ) {
        FormElement element = findByKey(key);
        if (element == null) {
            return null;
        }
        return Float.parseFloat(element.getValue());
    }
    
    public Integer getIntegerValueByKey(
        String key
    ) {
        FormElement element = findByKey(key);
        if (element == null) {
            return null;
        }
        return Integer.parseInt(element.getValue());
    }
    
    public String getValueByKey(
        String key
    ) {
        FormElement element = findByKey(key);
        if (element == null) {
            return null;
        }
        return element.getValue();
    }
    
    public void setValueByKey(
        String key,
        BigDecimal value
    ) {
        FormElement element = findByKey(key);
        element.setValue(value.toString());
    }
    
    public void setValueByKey(
        String key,
        Float value
    ) {
        FormElement element = findByKey(key);
        element.setValue(value.toString());
    }
    
    public void setValueByKey(
        String key,
        Integer value
    ) {
        FormElement element = findByKey(key);
        element.setValue(value.toString());
    }
    
    public void setValueByKey(
        String key,
        String value
    ) {
        FormElement element = findByKey(key);
        element.setValue(value);
    }
    
}
