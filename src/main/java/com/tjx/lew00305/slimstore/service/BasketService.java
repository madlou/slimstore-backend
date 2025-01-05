package com.tjx.lew00305.slimstore.service;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.session.Basket;
import com.tjx.lew00305.slimstore.model.session.BasketLine;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final Basket basket;

    public Basket addBasketByForm(
        Form requestForm
    ) {
        addFormElements(requestForm.getElements());
        return basket;
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

    public void addFormElements(
        FormElement[] elements
    ) {
        for (FormElement element : elements) {
            addFormElement(element);
        }
    }

    public void empty() {
        basket.empty();
    }

    public BasketLine[] getBasketArray() {
        return basket.getArray();
    }
    
    public BasketLine[] getBasketArray(
        Session session
    ) {
        if (session == null) {
            return new BasketLine[0];
        }
        Basket sessionBasket = (Basket) session.getAttribute("scopedTarget.basket");
        return sessionBasket.getArray();
    }

    public ArrayList<BasketLine> getBasketArrayList() {
        return basket.getArrayList();
    }

    public BigDecimal getTotal() {
        return basket.getTotal();
    }

}
