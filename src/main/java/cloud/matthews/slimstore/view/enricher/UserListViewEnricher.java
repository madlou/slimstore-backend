package cloud.matthews.slimstore.view.enricher;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.form.FormElement;
import cloud.matthews.slimstore.store.StoreOptionsProvider;
import cloud.matthews.slimstore.user.UserService;
import cloud.matthews.slimstore.view.View;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewEnricher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserListViewEnricher implements ViewEnricher {

    private final UserService userService;
    private final StoreOptionsProvider storeOptionsProvider;

    @Override
    public ViewName supports() {
        return ViewName.USER_LIST;
    }

    @Override
    public void enrich(
        View view,
        Form requestForm
    ) throws Exception {
        userService.managerCheck();
        Form responseForm = view.getForm();
        responseForm.deleteElementsAfter(1);
        if (userService.isUserAdmin()) {
            responseForm.findByKey("stores").setHidden(false);
            responseForm.findByKey("submit").setHidden(false);
            String[] stores = storeOptionsProvider.getStoreOptions(false);
            responseForm.findByKey("stores").setOptions(stores);
            if (stores.length > 0) {
                String[] storeSplit = stores[0].split("\\|");
                responseForm.setValueByKey("stores", storeSplit[0]);
            }
            Integer storeNumber = requestForm.getIntegerValueByKey("stores");
            if (storeNumber != null) {
                responseForm.setValueByKey("stores", storeNumber.toString());
                for (FormElement element : userService.getUsersAsFormElements(storeNumber)) {
                    responseForm.addElement(element);
                }
            }
        } else {
            responseForm.findByKey("stores").setHidden(true);
            responseForm.findByKey("submit").setHidden(true);
            for (FormElement element : userService.getUsersAsFormElements(null)) {
                responseForm.addElement(element);
            }
        }
    }

}
