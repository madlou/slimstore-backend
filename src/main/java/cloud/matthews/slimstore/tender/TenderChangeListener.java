package cloud.matthews.slimstore.tender;

/**
 * Implemented by components that need to react whenever the tender's
 * contents change (add / empty), without the {@code tender} package
 * needing to depend on the {@code display} package that actually
 * pushes the update to connected customer displays.
 */
public interface TenderChangeListener {

    void onTenderChanged(Tender tender);

}
