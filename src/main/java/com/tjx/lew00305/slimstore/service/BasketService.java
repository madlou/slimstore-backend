package com.tjx.lew00305.slimstore.service;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.session.Basket;
import com.tjx.lew00305.slimstore.model.session.BasketLine;

@Service
public class BasketService {
    
    @Autowired
    private Basket basket;
    
    public void addBasketByForm(
        Form requestForm
    ) {
        addFormElements(requestForm.getElements());
    }
    
    public void addFormElements(
        FormElement[] elements
    ) {
        for (FormElement element : elements) {
            addFormElement(element);
        }
    }
    
    public void addFormElement(
        FormElement element
    ) {
        if (element == null) {
            return;
        }
        BasketLine basketLine = new BasketLine();
        basketLine.setCode(element.getKey());
        basketLine.setName(element.getLabel());
        basketLine.setType(element.getType());
        basketLine.setQuantity(element.getQuantity());
        basketLine.setUnitValue(element.getPrice());
        basket.add(basketLine);
    }
    
    public ArrayList<BasketLine> getBasketArrayList() {
        return basket.getArrayList();
    }
    
    public BasketLine[] getBasketArray() {
        return basket.getArray();
    }
    
    public void empty() {
        basket.empty();
    }
    
    public BigDecimal getTotal() {
        return basket.getTotal();
    }
    
}
