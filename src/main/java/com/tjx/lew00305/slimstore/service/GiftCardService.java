package com.tjx.lew00305.slimstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.common.FormElement;

@Service
public class GiftCardService {
    
    @Autowired
    private LocationService locationService;

    public void topupQueue(String card, float value, int transactionNumber) {
        // TODO Auto-generated method stub
    }
    
    public FormElement topupByForm(Form requestForm) {
        String card = requestForm.getValueByKey("card");
        Float value = requestForm.getFloatValueByKey("amount");
        topup(card, value);
        return new FormElement("giftcard", "TJXGC", "Gift Card (" + card + ")", "1" ,null, value, null, null);
    }
    
    public void topup(String card, Float value) {
        Integer transactionNumber = locationService.getStoreRegister().getLastTxnNumber() + 1;
        topupQueue(card, value, transactionNumber);
    }

}
