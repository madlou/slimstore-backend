package cloud.matthews.slimstore.register;

import java.sql.Timestamp;
import java.util.List;

import cloud.matthews.slimstore.register.Register.RegisterStatus;
import cloud.matthews.slimstore.transaction.Transaction;
import lombok.Data;

@Data
public class RegisterDTO {
    
    private Integer id;
    private Integer number;
    private RegisterStatus status;
    private Integer lastTxnNumber;
    private List<Transaction> transactions;
    private String sessionId;
    private Timestamp lastTxnTime;

}
