package com.tjx.lew00305.slimstore.model.report;

import lombok.Data;

@Data
public class TransactionFlat {

    private Integer store;
    private String storeName;
    private String date;
    private String time;
    private Integer reg;
    private Integer txn;
    private String user;
    private Float total;
    
}
