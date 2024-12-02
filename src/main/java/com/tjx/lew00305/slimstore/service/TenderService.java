package com.tjx.lew00305.slimstore.service;

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
    
    public String addTenderByForm(Form requestForm) {
        try {
            addFormElements(requestForm.getElements());
            if(isSale() && getRemaining() <= 0) {
                if(getRemaining() < 0) {
                    tender.add(new TenderLine("cash", "Cash Change", getRemaining(), ""));                
                }
                tender.setComplete();
            }
            if(isRefund() && getRemaining() == 0) {
                tender.setComplete();
            }            
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }

    public void addFormElements(FormElement[] elements) throws Exception {
        for(FormElement element : elements) {
            addFormElement(element);
        }
    }
    
    public void addFormElement(FormElement element) throws Exception {
        float value;
        if(element.getValue().equals("full")) {
            value = basketService.getTotal() - tender.getTotal();
        } else {
            value = Float.parseFloat(element.getValue());
            if(isRefund()) {
                value *= -1;
            }
        }
        System.out.println(isRefund() ? "Refund " + value + " " + getRemaining(): "Sale");
        if(isRefund() && value < getRemaining()) {
            throw new Exception("Refund too high.");
        }
        if((isSale() && value > 0) || (isRefund() && value < 0)) {
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
    
    public Float getRemaining() {
        Float remaining = basketService.getTotal() - tender.getTotal();
        return remaining;
    }
    
    public boolean isComplete() {
        return tender.isComplete();
    }

    public boolean isSale() {
        return basketService.getTotal() >= 0;
    }

    public boolean isRefund() {
        return basketService.getTotal() < 0;
    }

}
