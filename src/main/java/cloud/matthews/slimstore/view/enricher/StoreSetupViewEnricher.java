package cloud.matthews.slimstore.view.enricher;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.store.Store;
import cloud.matthews.slimstore.user.UserService;
import cloud.matthews.slimstore.view.View;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewEnricher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreSetupViewEnricher implements ViewEnricher {

    private final UserService userService;
    private final Store store;

    @Override
    public ViewName supports() {
        return ViewName.STORE_SETUP;
    }

    @Override
    public void enrich(
        View view,
        Form requestForm
    ) throws Exception {
        userService.managerCheck();
        Form responseForm = view.getForm();
        responseForm.setValueByKey("name", store.getName());
        responseForm.setValueByKey("countryCode", store.getCountryCode().toString());
        responseForm.setValueByKey("currencyCode", store.getCurrencyCode().toString());
        responseForm.setValueByKey("languageCode", store.getLanguageCode().toString());
        responseForm.setValueByKey("address1", store.getAddress1());
        responseForm.setValueByKey("address2", store.getAddress2());
        responseForm.setValueByKey("city", store.getCity());
        responseForm.setValueByKey("postCode", store.getPostCode());
        responseForm.setValueByKey("phoneNumber", store.getPhoneNumber());
    }

}
