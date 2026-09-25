package cloud.matthews.slimstore.view.enricher;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.register.Register;
import cloud.matthews.slimstore.store.Store;
import cloud.matthews.slimstore.user.UserService;
import cloud.matthews.slimstore.view.View;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewEnricher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegisterChangeViewEnricher implements ViewEnricher {

    private final UserService userService;
    private final Store store;
    private final Register register;

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
        Store selectedStore = userService.getUser().getStore();
        if (store.isSet()) {
            selectedStore = store;
        }
        String registerNumber = register.isSet() ? register.getNumber().toString() : "";
        responseForm.setValueByKey("storeNumber", selectedStore.isSet() ? selectedStore.getNumber().toString() : "");
        responseForm.setValueByKey("registerNumber", registerNumber);
        if (userService.isUserManagerOrAdmin()) {
            responseForm.findByKey("storeNumber").setDisabled(false);
        }
    }

}
