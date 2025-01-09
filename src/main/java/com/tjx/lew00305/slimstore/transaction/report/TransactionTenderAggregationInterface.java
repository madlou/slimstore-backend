package com.tjx.lew00305.slimstore.transaction.report;

import java.math.BigDecimal;

public interface TransactionTenderAggregationInterface {

    String getDate();

    Integer getRegister();

    Integer getStore();

    String getStoreName();

    String getType();

    BigDecimal getValue();

}
