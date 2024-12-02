package com.tjx.lew00305.slimstore.model.report;

public interface TransactionAuditInterface {

    Integer getStore();
    String getStoreName();
    String getDate();
    String getTime();
    Integer getReg();
    Integer getTxn();
    Float getTxnTotal();
    Float getLineTotal();
    Float getTenderTotal();
    String getCheck();
    
}

