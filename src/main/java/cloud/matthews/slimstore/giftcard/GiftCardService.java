package cloud.matthews.slimstore.giftcard;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.register.form.Form;
import cloud.matthews.slimstore.register.form.FormElement;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GiftCardService {
    
    private final RegisterService registerService;

    public void topup(
        String card,
        BigDecimal value
    ) {
        Integer transactionNumber = registerService.getRegister().getLastTxnNumber() + 1;
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
        element.setType(FormElement.FormElementType.PRODUCT_GIFTCARD);
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
