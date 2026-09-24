package cloud.matthews.slimstore.view.enricher;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.store.StoreService;
import cloud.matthews.slimstore.view.View;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewEnricher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReturnViewEnricher implements ViewEnricher {

    private final StoreService storeService;

    @Override
    public ViewName supports() {
        return ViewName.RETURN;
    }

    @Override
    public void enrich(
        View view,
        Form requestForm
    ) throws Exception {
        view.getForm().setValueByKey("store", storeService.getStore().getNumber());
    }

}
