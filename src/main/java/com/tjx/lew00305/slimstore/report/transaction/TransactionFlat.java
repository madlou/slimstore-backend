package com.tjx.lew00305.slimstore.report.transaction;

import java.math.BigDecimal;

import com.tjx.lew00305.slimstore.product.Currency;

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
