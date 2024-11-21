package com.tjx.lew00305.slimstore.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.session.Basket;
import com.tjx.lew00305.slimstore.model.session.BasketLine;

@Service
public class BasketService {
    
    @Autowired
    private Basket basket;
    
    public void addBasketByRequest(RegisterRequestDTO request) {
        addFormElements(request.getFormElements());
    }
    
    public void addFormElements(FormElement[] elements) {
        for(FormElement element : elements) {
            addFormElement(element);
        }
    }
    
    public void addFormElement(FormElement element) {
        if(element == null) {
            return;
        }
        int quantity = Integer.parseInt(element.getValue());
        if(quantity > 0) {
            basket.add(new BasketLine(element.getKey(), element.getLabel(), element.getType(), quantity, element.getPrice()));
        }
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
    
    public float getTotal() {
        return basket.getTotal();
    }

}
