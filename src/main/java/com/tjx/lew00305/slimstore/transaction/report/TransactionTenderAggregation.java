package com.tjx.lew00305.slimstore.transaction.report;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionTenderAggregation {

    Integer store;
    String storeName;
    Integer register;
    String date;
    String type;
    BigDecimal value;

}
