package com.tjx.lew00305.slimstore.giftcard;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.location.LocationService;
import com.tjx.lew00305.slimstore.register.form.Form;
import com.tjx.lew00305.slimstore.register.form.FormElement;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GiftCardService {

    private final LocationService locationService;
    
    public void topup(
        String card,
        BigDecimal value
    ) {
        Integer transactionNumber = locationService.getStoreRegister().getLastTxnNumber() + 1;
        topupQueue(card, value, transactionNumber);
    }

    public Form topupByForm(
        Form requestForm
    ) {
        String card = requestForm.getValueByKey("card");
        BigDecimal value = requestForm.getBigDecimalValueByKey("amount");
        topup(card, value);
        requestForm.deleteElements();
        FormElement element = new FormElement();
        element.setType(FormElement.Type.PRODUCT_GIFTCARD);
        element.setKey("TJXGC");
        element.setLabel("Gift Card (" + card + ")");
        element.setQuantity(1);
        element.setPrice(value);
        requestForm.addElement(element);
        return requestForm;
    }

    public void topupQueue(
        String card,
        BigDecimal value,
        Integer transactionNumber
    ) {
        // TODO Auto-generated method stub
    }

}
