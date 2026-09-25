package cloud.matthews.slimstore.store;

public class StoreSetupException extends Exception {

    private static final long serialVersionUID = -234141543579069546L;

    public StoreSetupException(
        String errorMessage
    ) {
        super(errorMessage);
    }
}
