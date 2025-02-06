package cloud.matthews.slimstore.transaction.report;

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
