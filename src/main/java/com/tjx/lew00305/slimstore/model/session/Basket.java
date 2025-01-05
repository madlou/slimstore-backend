package com.tjx.lew00305.slimstore.model.session;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;

@SuppressWarnings("serial")
@Component
@SessionScope
@Data
public class Basket implements Serializable {

    private ArrayList<BasketLine> basket = new ArrayList<BasketLine>();

    public void add(
        BasketLine basketLine
    ) {
        basket.add(basketLine);
    }

    public void empty() {
        basket = new ArrayList<BasketLine>();
    }

    public BasketLine[] getArray() {
        return basket.toArray(new BasketLine[0]);
    }

    public ArrayList<BasketLine> getArrayList() {
        return basket;
    }

    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0);
        for (BasketLine line : basket) {
            total = total.add(line.getLineValue());
        }
        return total;
    }

}
