package cloud.matthews.slimstore.view.enricher;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import cloud.matthews.slimstore.form.Form;
import cloud.matthews.slimstore.form.FormElement;
import cloud.matthews.slimstore.store.Store;
import cloud.matthews.slimstore.store.StoreOptionsProvider;
import cloud.matthews.slimstore.translation.TranslationService;
import cloud.matthews.slimstore.user.User;
import cloud.matthews.slimstore.user.UserRole;
import cloud.matthews.slimstore.user.UserService;
import cloud.matthews.slimstore.view.View;
import cloud.matthews.slimstore.view.View.ViewName;
import cloud.matthews.slimstore.view.ViewEnricher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserEditViewEnricher implements ViewEnricher {

    private final UserService userService;
    private final TranslationService translationService;
    private final StoreOptionsProvider storeOptionsProvider;

    @Override
    public ViewName supports() {
        return ViewName.USER_EDIT;
    }

    @Override
    public void enrich(
        View view,
        Form requestForm
    ) throws Exception {
        userService.managerCheck();
        Form responseForm = view.getForm();
        User editUser = userService.getUser(requestForm.getValueByKey("code"));
        responseForm.setValueByKey("code", editUser.getCode());
        FormElement storeElement = responseForm.findByKey("store");
        if (userService.isUserAdmin()) {
            storeElement.setDisabled(false);
        }
        Store store = userService.getUser(editUser.getCode()).getStore();
        storeElement.setValue(!store.isSet() ? "0" : store.getNumber().toString());
        storeElement.setOptions(storeOptionsProvider.getStoreOptions(true));
        responseForm.setValueByKey("name", editUser.getName());
        responseForm.setValueByKey("email", editUser.getEmail());
        responseForm.setValueByKey("password", "");
        FormElement roleElement = responseForm.findByKey("role");
        String[] roleOptions = roleElement.getOptions();
        ArrayList<String> newRoleOptions = new ArrayList<String>();
        for (int i = 0; i < roleOptions.length; i++) {
            newRoleOptions.add(roleOptions[i]);
        }
        if (userService.isUserAdmin()) {
            String adminTranslation = translationService.translate("ui.administrator");
            newRoleOptions.add(UserRole.ADMIN.toString() + "|" + adminTranslation);
        }
        roleElement.setOptions(new String[0]);
        roleElement.setOptions(newRoleOptions.toArray(new String[0]));
        responseForm.setValueByKey("role", editUser.getRole().toString());
    }

}
