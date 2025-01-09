package com.tjx.lew00305.slimstore.tender;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@SuppressWarnings("serial")
@Component
@SessionScope
public class Tender implements Serializable {

    private ArrayList<TenderLine> tender = new ArrayList<TenderLine>();

    private boolean isComplete = false;

    public void add(
        TenderLine tenderLine
    ) {
        tender.add(tenderLine);
    }

    public void empty() {
        tender = new ArrayList<TenderLine>();
        isComplete = false;
    }

    public TenderLine[] getArray() {
        return tender.toArray(new TenderLine[0]);
    }

    public ArrayList<TenderLine> getArrayList() {
        return tender;
    }

    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0);
        for (TenderLine line : tender) {
            total = total.add(line.getValue());
        }
        return total;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete() {
        isComplete = true;
    }
    
}
