package com.tjx.lew00305.slimstore.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.entity.TransactionTender.TenderType;
import com.tjx.lew00305.slimstore.model.session.Tender;
import com.tjx.lew00305.slimstore.model.session.TenderLine;

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
        if (element.getValue().equals("full")) {
            value = basketService.getTotal().subtract(tender.getTotal());
        } else {
            value = new BigDecimal(element.getValue());
            if (isRefund()) {
                value = value.negate();
            }
        }
        if (isRefund()) {
            if (getRemaining().compareTo(value) > 0) {
                throw new Exception(translationService.translate("error.tender_refund_too_high"));
            }
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                if (getRemaining().compareTo(value) <= 0) {
                    tender.add(new TenderLine(TenderType.valueOf(element.getKey()), element.getLabel(), value, ""));
                } else {
                    throw new Exception(translationService.translate("error.tender_value_not_allowed"));
                }
            } else {
                throw new Exception(translationService.translate("error.tender_value_not_allowed"));
            }
        }
        if (isSale()) {
            if (value.compareTo(BigDecimal.ZERO) > 0) {
                tender.add(new TenderLine(TenderType.valueOf(element.getKey()), element.getLabel(), value, ""));
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
    ) {
        try {
            addFormElements(requestForm.getElements());
            if (isSale() &&
                (getRemaining().compareTo(BigDecimal.ZERO) <= 0)) {
                if (getRemaining().compareTo(BigDecimal.ZERO) < 0) {
                    tender.add(new TenderLine(TenderType.CASH, "Cash Change", getRemaining(), ""));
                }
                tender.setComplete();
            }
            if (isRefund() &&
                (getRemaining().compareTo(BigDecimal.ZERO) == 0)) {
                tender.setComplete();
            }
        } catch (Exception e) {
            return null;
        }
        return tender;
    }
    
    public boolean allowOverTender(
        TenderType tenderType
    ) {
        List<TenderType> allowedOverTenders = new ArrayList<TenderType>();
        allowedOverTenders.add(TenderType.CASH);
        return allowedOverTenders.contains(tenderType);
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
    
    public boolean isRefund() {
        return basketService.getTotal().compareTo(BigDecimal.ZERO) < 0;
    }
    
    public boolean isSale() {
        return basketService.getTotal().compareTo(BigDecimal.ZERO) >= 0;
    }
    
}
