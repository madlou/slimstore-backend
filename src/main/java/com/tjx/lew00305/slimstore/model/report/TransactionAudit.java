package com.tjx.lew00305.slimstore.model.report;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionAudit {
    
    Integer store;
    String storeName;
    Integer register;
    String date;
    String time;
    Integer transaction;
    BigDecimal transactionTotal;
    BigDecimal lineTotal;
    BigDecimal tenderTotal;
    String check;
    
}
