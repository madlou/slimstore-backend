package cloud.matthews.slimstore.view.enricher;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.store.StoreService;
import cloud.matthews.slimstore.user.UserService;
import cloud.matthews.slimstore.view.View;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewEnricher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreSetupViewEnricher implements ViewEnricher {

    private final UserService userService;
    private final StoreService storeService;

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
        responseForm.setValueByKey("name", storeService.getStore().getName());
        responseForm.setValueByKey("countryCode", storeService.getStore().getCountryCode().toString());
        responseForm.setValueByKey("currencyCode", storeService.getStore().getCurrencyCode().toString());
        responseForm.setValueByKey("languageCode", storeService.getStore().getLanguageCode().toString());
        responseForm.setValueByKey("address1", storeService.getStore().getAddress1());
        responseForm.setValueByKey("address2", storeService.getStore().getAddress2());
        responseForm.setValueByKey("city", storeService.getStore().getCity());
        responseForm.setValueByKey("postCode", storeService.getStore().getPostCode());
        responseForm.setValueByKey("phoneNumber", storeService.getStore().getPhoneNumber());
    }

}
