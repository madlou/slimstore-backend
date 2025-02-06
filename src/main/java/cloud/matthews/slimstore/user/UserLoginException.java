package cloud.matthews.slimstore.user;

public class UserLoginException extends Exception {

    private static final long serialVersionUID = 8122620910773110834L;
    
    public UserLoginException(
        String errorMessage
    ) {
        super(errorMessage);
    }
}
