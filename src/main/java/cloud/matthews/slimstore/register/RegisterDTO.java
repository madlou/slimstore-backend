package cloud.matthews.slimstore.register;

import java.sql.Timestamp;
import java.util.List;

import cloud.matthews.slimstore.register.Register.RegisterStatus;
import cloud.matthews.slimstore.store.Store;
import cloud.matthews.slimstore.transaction.Transaction;
import cloud.matthews.slimstore.user.User;

import lombok.Data;

@Data
public class RegisterDTO {
    private Integer id;
    private Store store;
    private Integer number;
    private RegisterStatus status;
    private Integer lastTxnNumber;
    private List<Transaction> transactions;
    private String sessionId;
    private User user;
    private Timestamp lastTxnTime;
}
