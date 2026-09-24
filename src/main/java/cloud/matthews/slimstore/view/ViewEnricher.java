package cloud.matthews.slimstore.view;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.view.View.ViewName;

/**
 * Implemented by domain packages to populate a {@link View}'s form with
 * view-specific data before it is returned to the UI, so {@link ViewService}
 * does not need to depend directly on every domain service.
 */
public interface ViewEnricher {

    ViewName supports();

    void enrich(
        View view,
        Form requestForm
    ) throws Exception;

}
