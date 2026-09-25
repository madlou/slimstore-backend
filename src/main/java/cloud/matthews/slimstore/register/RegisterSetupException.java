package cloud.matthews.slimstore.register;

public class RegisterSetupException extends Exception {

    private static final long serialVersionUID = 9136909954132998436L;

    public RegisterSetupException(
        String errorMessage
    ) {
        super(errorMessage);
    }
}
