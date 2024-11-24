package com.tjx.lew00305.slimstore.model.report;

import lombok.Data;

@Data
public class TransactionTenderAggregation {

    Integer store;
    String storeName;
    Integer reg;
    String date;
    String type;
    Float value;
    
}
