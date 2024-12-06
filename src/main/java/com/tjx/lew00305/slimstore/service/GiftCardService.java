package com.tjx.lew00305.slimstore.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.common.FormElement;

@Service
public class GiftCardService {
    
    @Autowired
    private LocationService locationService;
    
    public void topupQueue(
        String card,
        BigDecimal value,
        Integer transactionNumber
    ) {
        // TODO Auto-generated method stub
    }
    
    public FormElement topupByForm(
        Form requestForm
    ) {
        String card = requestForm.getValueByKey("card");
        BigDecimal value = requestForm.getBigDecimalValueByKey("amount");
        topup(card, value);
        FormElement element = new FormElement();
        element.setType(FormElement.Type.PRODUCT_GIFTCARD);
        element.setKey("TJXGC");
        element.setLabel("Gift Card (" + card + ")");
        element.setQuantity(1);
        element.setPrice(value);
        return element;
    }
    
    public void topup(
        String card,
        BigDecimal value
    ) {
        Integer transactionNumber = locationService.getStoreRegister().getLastTxnNumber() +
            1;
        topupQueue(card, value, transactionNumber);
    }
    
}
