package cloud.matthews.slimstore.register;

import java.sql.Timestamp;

import cloud.matthews.slimstore.register.Register.RegisterStatus;
import lombok.Data;

@Data
public class RegisterDTO {
    
    private Integer id;
    private Integer number;
    private RegisterStatus status;
    private Integer lastTxnNumber;
    private String sessionId;
    private Timestamp lastTxnTime;

}
