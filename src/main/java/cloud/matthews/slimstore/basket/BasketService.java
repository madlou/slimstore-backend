package cloud.matthews.slimstore.basket;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import cloud.matthews.slimstore.register.RegisterRequestDTO;
import cloud.matthews.slimstore.register.form.Form;
import cloud.matthews.slimstore.register.form.FormElement;
import cloud.matthews.slimstore.register.form.FormElement.FormElementType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final Basket basket;

    public void addFormElement(
        FormElement element
    ) {
        if (element == null) {
            return;
        }
        // TODO: will refactor manual returns to use this
        // if(element.getType().equals(FormElementType.RETURN_MANUAL)) {}
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

    public Basket addManualReturnToBasketByForm(
        Form requestForm
    ) {
        BasketLine basketLine = new BasketLine();
        basketLine.setCode(requestForm.getValueByKey("sku"));
        basketLine.setName(requestForm.getValueByKey("sku"));
        basketLine.setType(FormElementType.RETURN_MANUAL);
        basketLine.setQuantity(requestForm.getIntegerValueByKey("quantity"));
        basketLine.setUnitValue(new BigDecimal(requestForm.getValueByKey("price")));
        basket.add(basketLine);
        return basket;
    }

    public Basket addToBasketByForm(
        Form requestForm
    ) {
        addFormElements(requestForm.getElements());
        return basket;
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
        if(sessionBasket == null) {
            return new BasketLine[0];
        }
        return sessionBasket.getArray();
    }

    public ArrayList<BasketLine> getBasketArrayList() {
        return basket.getArrayList();
    }

    public BigDecimal getTotal() {
        return basket.getTotal();
    }

    public Basket voidLineByForm(RegisterRequestDTO request) {
        ArrayList<BasketLine> tempBasket = basket.getArrayList();
        BasketLine line = tempBasket.get(request.getIntegerValueByKey("void"));
        tempBasket.remove(line);
        basket.setBasket(tempBasket);
        return basket;
    }

}
