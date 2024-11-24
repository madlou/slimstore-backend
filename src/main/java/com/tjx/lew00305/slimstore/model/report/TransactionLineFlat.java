package com.tjx.lew00305.slimstore.model.report;

import lombok.Data;

@Data
public class TransactionLineFlat {

    private Integer store;
    private String storeName;
    private String date;
    private String time;
    private Integer reg;
    private Integer txn;
    private Integer number;
    private String product;
    private String type;
    private Integer quantity;
    private Float lineValue;
    
}
