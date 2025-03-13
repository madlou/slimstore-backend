package cloud.matthews.slimstore.tender;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import cloud.matthews.slimstore.basket.BasketService;
import cloud.matthews.slimstore.register.form.Form;
import cloud.matthews.slimstore.register.form.FormElement;
import cloud.matthews.slimstore.tender.card.Card;
import cloud.matthews.slimstore.transaction.TransactionTender.TenderType;
import cloud.matthews.slimstore.translation.TranslationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenderService {
    
    private final BasketService basketService;
    private final Tender tender;
    private final TranslationService translationService;
    private final TenderWebsocket tenderWebsocket;

    public void addFormElement(
        FormElement element
    ) throws Exception {
        BigDecimal value;
        TenderType type = TenderType.valueOf(element.getKey());
        Card card = null;
        if (element.getValue().equals("full")) {
            value = basketService.getTotal().subtract(tender.getTotal());
        } else {
            value = new BigDecimal(element.getValue());
            if (isRefundTxn()) {
                value = value.negate();
            }
        }
        if (isSaleTxn()) {
            if (value.compareTo(BigDecimal.ZERO) > 0) {
                if (!allowOverTender(type) &&
                    (getRemaining().compareTo(value) < 0)) {
                    throw new Exception(translationService.translate("error.tender_value_not_allowed"));
                }
                if(type.equals(TenderType.CARD)){
                    card = new Card();
                    card.setAmount(value);
                    card.setStatus(Card.Status.INITIAL);
                } 
                tender.add(new TenderLine(type, element.getLabel(), value, card));
            } else {
                throw new Exception(translationService.translate("error.tender_value_not_allowed"));
            }
        }
        if (isRefundTxn()) {
            if (getRemaining().compareTo(value) > 0) {
                throw new Exception(translationService.translate("error.tender_refund_too_high"));
            }
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                if (getRemaining().compareTo(value) <= 0) {
                    tender.add(new TenderLine(type, element.getLabel(), value, card));
                } else {
                    throw new Exception(translationService.translate("error.tender_value_not_allowed"));
                }
            } else {
                throw new Exception(translationService.translate("error.tender_value_not_allowed"));
            }
        }
    }
    
    public void addFormElements(
        FormElement[] elements
    ) throws Exception {
        for (FormElement element : elements) {
            addFormElement(element);
        }
        tenderWebsocket.sendTender(tender);
    }
    
    public Tender addTenderByForm(
        Form requestForm
    ) throws Exception {
        addFormElements(requestForm.getElements());
        if (isSaleTxn() &&
            (getRemaining().compareTo(BigDecimal.ZERO) <= 0)) {
            if (getRemaining().compareTo(BigDecimal.ZERO) < 0) {
                tender.add(new TenderLine(TenderType.CASH, "Cash Change", getRemaining(), null));
            }
            tender.setComplete();
        }
        if (isRefundTxn() &&
            (getRemaining().compareTo(BigDecimal.ZERO) == 0)) {
            tender.setComplete();
        }
        return tender;
    }
    
    public Boolean allowOverTender(
        TenderType type
    ) {
        switch (type) {
            default:
                return false;
            case TenderType.CASH:
            case TenderType.VOUCHER:
                return true;
        }
    }
    
    public void empty() {
        tender.empty();
        tenderWebsocket.sendTender(tender);
    }
    
    public BigDecimal getRemaining() {
        BigDecimal remaining = basketService.getTotal().subtract(tender.getTotal());
        return remaining;
    }
    
    public Tender getTender() {
        return tender;
    }
    
    public TenderLine[] getTenderArray() {
        return tender.getArray();
    }
    
    public TenderLine[] getTenderArray(
        Session session
    ) {
        if (session == null) {
            return new TenderLine[0];
        }
        Tender sessionTender = (Tender) session.getAttribute("scopedTarget.tender");
        if(sessionTender == null) {
            return new TenderLine[0];
        }
        return sessionTender.getArray();
    }
    
    public ArrayList<TenderLine> getTenderArrayList() {
        return tender.getArrayList();
    }
    
    public boolean isComplete() {
        return tender.isComplete();
    }
    
    public boolean isRefundTxn() {
        return basketService.getTotal().compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isSaleTxn() {
        return basketService.getTotal().compareTo(BigDecimal.ZERO) >= 0;
    }

    public void updateCard(
        String reference,
        Card.Status status
    ) {
        Card card = getTenderArrayList().getLast().getCard();
        card.setReference(reference);
        card.setStatus(status);
        tenderWebsocket.sendTender(tender);
    }
    
}
