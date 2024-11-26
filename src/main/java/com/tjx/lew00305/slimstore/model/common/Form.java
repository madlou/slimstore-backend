package com.tjx.lew00305.slimstore.model.common;

import lombok.Data;

@Data
public class Form {

    private FormElement[] elements = new FormElement[0];
//    private String targetView;
//    private String serverProcess;
    private String process;
    
    public FormElement findByKey(String key) {
        for(FormElement element : elements) {
            if(element.getKey().equals(key)) {
                return element;
            }
        }
        return null;
    }

    public String getValueByKey(String key) {
        FormElement element = findByKey(key);
        if(element == null) {
            return null;
        }
        return element.getValue();
    }

    public Integer getIntegerValueByKey(String key) {
        FormElement element = findByKey(key);
        if(element == null) {
            return null;
        }
        return Integer.parseInt(element.getValue());
    }

    public Float getFloatValueByKey(String key) {
        FormElement element = findByKey(key);
        if(element == null) {
            return null;
        }
        return Float.parseFloat(element.getValue());
    }
    
    public void addElement(FormElement newElement) {
        Integer size = elements.length;
        FormElement[] newElements = new FormElement[size + 1];
        Integer counter = 0;
        for(FormElement element : elements) {
            newElements[counter++] = element;
        }
        newElements[size] = newElement;
        elements = newElements;
    }

}
