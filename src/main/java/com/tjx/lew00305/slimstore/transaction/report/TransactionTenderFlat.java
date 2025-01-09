package com.tjx.lew00305.slimstore.transaction.report;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionTenderFlat {

    private Integer store;
    private String storeName;
    private String date;
    private String time;
    private Integer register;
    private Integer transaction;
    private Integer number;
    private String type;
    private BigDecimal value;
    private String reference;

}
