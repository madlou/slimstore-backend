package com.tjx.lew00305.slimstore.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.model.Basket;
import com.tjx.lew00305.slimstore.model.BasketLine;
import com.tjx.lew00305.slimstore.model.FormElement;

@Service
public class BasketService {
    
    @Autowired
    private Basket basket;
    
    public void addFormElements(FormElement[] elements) {
        for(FormElement element : elements) {
            addFormElement(element);
        }
    }
    
    public void addFormElement(FormElement element) {
        int quantity = Integer.parseInt(element.getValue());
        if(quantity > 0) {
            basket.add(new BasketLine(element.getKey(), element.getLabel(), element.getType(), quantity, element.getPrice()));
        }
    }
    
    public ArrayList<BasketLine> getBasketArrayList() {
        return basket.get();
    }

    public BasketLine[] getBasketArray() {
        return basket.getArray();
    }

    public void empty() {
        basket.empty();
    }

}
