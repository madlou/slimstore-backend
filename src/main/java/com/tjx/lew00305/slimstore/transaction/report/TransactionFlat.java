package com.tjx.lew00305.slimstore.transaction.report;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.tjx.lew00305.slimstore.store.Store.Currency;

import lombok.Data;

@Data
@JsonTypeInfo(use = Id.CLASS, visible = false, include = As.PROPERTY)
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
    private Integer review;

}
