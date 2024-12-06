package com.tjx.lew00305.slimstore.service;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.session.Tender;
import com.tjx.lew00305.slimstore.model.session.TenderLine;

@Service
public class TenderService {
    
    @Autowired
    private BasketService basketService;
    @Autowired
    private Tender tender;
    
    public String addTenderByForm(
        Form requestForm
    ) {
        try {
            addFormElements(requestForm.getElements());
            if (isSale() &&
                getRemaining().compareTo(BigDecimal.ZERO) <= 0) {
                if (getRemaining().compareTo(BigDecimal.ZERO) < 0) {
                    tender.add(new TenderLine("cash", "Cash Change", getRemaining(), ""));
                }
                tender.setComplete();
            }
            if (isRefund() &&
                getRemaining().compareTo(BigDecimal.ZERO) == 0) {
                tender.setComplete();
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }
    
    public void addFormElements(
        FormElement[] elements
    ) throws Exception {
        for (FormElement element : elements) {
            addFormElement(element);
        }
    }
    
    public void addFormElement(
        FormElement element
    ) throws Exception {
        BigDecimal value;
        if (element.getValue().equals("full")) {
            value = basketService.getTotal().subtract(tender.getTotal());
        } else {
            value = new BigDecimal(element.getValue());
            if (isRefund()) {
                value = value.negate();
            }
        }
        if (isRefund() &&
            getRemaining().compareTo(value) > 0) {
            throw new Exception("Refund too high.");
        }
        if ((isSale() &&
            value.compareTo(BigDecimal.ZERO) > 0) ||
            (isRefund() &&
                value.compareTo(BigDecimal.ZERO) <= 0 &&
                getRemaining().compareTo(value) <= 0)) {
            tender.add(new TenderLine(element.getKey(), element.getLabel(), value, ""));
        } else {
            throw new Exception("Value not allowed");
        }
        
    }
    
    public ArrayList<TenderLine> getTenderArrayList() {
        return tender.getArrayList();
    }
    
    public TenderLine[] getTenderArray() {
        return tender.getArray();
    }
    
    public void empty() {
        tender.empty();
    }
    
    public BigDecimal getRemaining() {
        System.out.println(isSale());
        System.out.println(isRefund());
        System.out.println(basketService.getTotal());
        System.out.println(tender.getTotal());
        System.out.println(basketService.getTotal().subtract(tender.getTotal()));
        BigDecimal remaining = basketService.getTotal().subtract(tender.getTotal());
        return remaining;
    }
    
    public boolean isComplete() {
        return tender.isComplete();
    }
    
    public boolean isSale() {
        return basketService.getTotal().compareTo(BigDecimal.ZERO) >= 0;
    }
    
    public boolean isRefund() {
        return basketService.getTotal().compareTo(BigDecimal.ZERO) < 0;
    }
    
}
