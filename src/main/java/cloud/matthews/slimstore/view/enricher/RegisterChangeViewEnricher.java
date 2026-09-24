package cloud.matthews.slimstore.view.enricher;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.store.Store;
import cloud.matthews.slimstore.store.StoreService;
import cloud.matthews.slimstore.user.UserService;
import cloud.matthews.slimstore.view.View;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewEnricher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegisterChangeViewEnricher implements ViewEnricher {

    private final UserService userService;
    private final StoreService storeService;
    private final RegisterService registerService;

    @Override
    public ViewName supports() {
        return ViewName.REGISTER_CHANGE;
    }

    @Override
    public void enrich(
        View view,
        Form requestForm
    ) throws Exception {
        Form responseForm = view.getForm();
        Store store = userService.getUser().getStore();
        if (storeService.getStore().isSet()) {
            store = storeService.getStore();
        }
        String registerNumber = (registerService.getRegister().isSet()) ? registerService.getRegister().getNumber().toString() : "";
        responseForm.setValueByKey("storeNumber", store.isSet() ? store.getNumber().toString() : "");
        responseForm.setValueByKey("registerNumber", registerNumber);
        if (userService.isUserManagerOrAdmin()) {
            responseForm.findByKey("storeNumber").setDisabled(false);
        }
    }

}
