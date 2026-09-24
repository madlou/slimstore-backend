package cloud.matthews.slimstore.store;

public class LocationSetupException extends Exception {

    private static final long serialVersionUID = -234141543579069546L;

    public LocationSetupException(
        String errorMessage
    ) {
        super(errorMessage);
    }
}
