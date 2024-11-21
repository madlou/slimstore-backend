package com.tjx.lew00305.slimstore.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.session.Tender;
import com.tjx.lew00305.slimstore.model.session.TenderLine;

@Service
public class TenderService {
    
    @Autowired
    private BasketService basketService;

    @Autowired
    private Tender tender;
    
    public void addTenderByRequest(RegisterRequestDTO request) {
        addFormElements(request.getFormElements());
        if(tender.getTotal() >= basketService.getTotal()) {
            tender.add(new TenderLine("cash-change", "Cash Change", basketService.getTotal() - tender.getTotal(), null));
            tender.setComplete();
        }
    }

    public void addFormElements(FormElement[] elements) {
        for(FormElement element : elements) {
            addFormElement(element);
        }
    }
    
    public void addFormElement(FormElement element) {
        float value;
        if(element.getValue().equals("full")) {
            value = basketService.getTotal() - tender.getTotal();
        } else {
            value = Float.parseFloat(element.getValue());            
        }
        if(value > 0) {
            tender.add(new TenderLine(element.getKey(), element.getLabel(), value, ""));
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
    
    public boolean isComplete() {
        return tender.isComplete();
    }

}
