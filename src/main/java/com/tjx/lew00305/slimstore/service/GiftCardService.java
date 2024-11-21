package com.tjx.lew00305.slimstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.model.common.FormElement;

@Service
public class GiftCardService {
    
    @Autowired
    private LocationService locationService;

    public void topupQueue(String card, float value, int transactionNumber) {
        // TODO Auto-generated method stub
    }
    
    public FormElement topupByRequest(RegisterRequestDTO request) {
        if(request.getFormElements().length < 1) {
            return null;
        }
        String card = request.getFormElements()[0].getValue();
        float value = Float.parseFloat(request.getFormElements()[1].getValue());
        int transactionNumber = locationService.getStoreRegister().getLastTxnNumber() + 1;
        topupQueue(card, value, transactionNumber);
        return new FormElement("giftcard", "TJXGC", "Gift Card (" + card + ")", "1" ,null, value, null);
    }

}
