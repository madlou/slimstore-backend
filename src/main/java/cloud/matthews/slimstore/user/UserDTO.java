package cloud.matthews.slimstore.user;

import lombok.Data;

@Data
public class UserDTO {

    public String code;
    public String email;
    public String name;
    public UserRole role;
    
}
