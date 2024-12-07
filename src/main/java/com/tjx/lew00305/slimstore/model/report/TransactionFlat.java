package com.tjx.lew00305.slimstore.model.report;

import java.math.BigDecimal;

import com.tjx.lew00305.slimstore.enums.Currency;

import lombok.Data;

@Data
public class TransactionFlat {
    
    private Integer store;
    private String storeName;
    private String date;
    private String time;
    private Integer register;
    private Integer transaction;
    private String user;
    private Currency currency;
    private BigDecimal total;
    
}
