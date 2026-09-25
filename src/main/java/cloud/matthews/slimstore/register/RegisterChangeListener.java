package cloud.matthews.slimstore.register;

import cloud.matthews.slimstore.register.Register.RegisterStatus;

/**
 * Implemented by components that need to react whenever a register's
 * status changes (opened / closed / transacted), without the
 * {@code register} package needing to depend on the {@code display}
 * package that actually pushes the update to connected customer displays.
 */
public interface RegisterChangeListener {

    void onRegisterStatusChanged(
        RegisterStatus status,
        Integer transactionNumber
    );

}
