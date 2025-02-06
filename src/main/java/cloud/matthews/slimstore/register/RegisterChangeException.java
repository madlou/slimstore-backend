package cloud.matthews.slimstore.register;

public class RegisterChangeException extends Exception {

    private static final long serialVersionUID = -234141543579069546L;

    public RegisterChangeException(
        String errorMessage
    ) {
        super(errorMessage);
    }
}
