package com.tjx.lew00305.slimstore.tender;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.basket.BasketService;
import com.tjx.lew00305.slimstore.register.form.Form;
import com.tjx.lew00305.slimstore.register.form.FormElement;
import com.tjx.lew00305.slimstore.transaction.TransactionTender.TenderType;
import com.tjx.lew00305.slimstore.translation.TranslationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenderService {
    
    private final BasketService basketService;
    private final Tender tender;
    private final TranslationService translationService;

    public void addFormElement(
        FormElement element
    ) throws Exception {
        BigDecimal value;
        TenderType type = TenderType.valueOf(element.getKey());
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
                tender.add(new TenderLine(type, element.getLabel(), value, ""));
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
                    tender.add(new TenderLine(type, element.getLabel(), value, ""));
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
    }
    
    public Tender addTenderByForm(
        Form requestForm
    ) throws Exception {
        addFormElements(requestForm.getElements());
        if (isSaleTxn() &&
            (getRemaining().compareTo(BigDecimal.ZERO) <= 0)) {
            if (getRemaining().compareTo(BigDecimal.ZERO) < 0) {
                tender.add(new TenderLine(TenderType.CASH, "Cash Change", getRemaining(), ""));
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
    }
    
    public BigDecimal getRemaining() {
        BigDecimal remaining = basketService.getTotal().subtract(tender.getTotal());
        return remaining;
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
    
}
