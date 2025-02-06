package cloud.matthews.slimstore.transaction.report;

import java.math.BigDecimal;

import cloud.matthews.slimstore.transaction.TransactionLine.TransactionLineType;

import lombok.Data;

@Data
public class TransactionLineFlat {

    private Integer store;
    private String storeName;
    private String date;
    private String time;
    private Integer register;
    private Integer transaction;
    private Integer number;
    private String product;
    private TransactionLineType type;
    private Integer quantity;
    private BigDecimal lineValue;
    private Integer returnedQuantity;

}
