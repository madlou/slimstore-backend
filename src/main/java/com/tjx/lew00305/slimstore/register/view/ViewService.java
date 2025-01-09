package com.tjx.lew00305.slimstore.register.view;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.location.LocationService;
import com.tjx.lew00305.slimstore.location.Store;
import com.tjx.lew00305.slimstore.product.ProductService;
import com.tjx.lew00305.slimstore.register.form.Form;
import com.tjx.lew00305.slimstore.register.form.FormElement;
import com.tjx.lew00305.slimstore.register.form.FormElement.Type;
import com.tjx.lew00305.slimstore.register.view.View.ViewName;
import com.tjx.lew00305.slimstore.transaction.Transaction;
import com.tjx.lew00305.slimstore.transaction.TransactionLine;
import com.tjx.lew00305.slimstore.transaction.TransactionService;
import com.tjx.lew00305.slimstore.translation.TranslationService;
import com.tjx.lew00305.slimstore.user.User;
import com.tjx.lew00305.slimstore.user.UserRole;
import com.tjx.lew00305.slimstore.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewService {
    
    private final LocationService locationService;
    private final ProductService productService;
    private final HttpServletRequest request;
    private final TransactionService transactionService;
    private final TranslationService translationService;
    private final UserService userService;
    private final ViewConfig viewConfig;

    private View enrichView(
        View view,
        Form requestForm
    ) {
        Form responseForm = view.getForm();
        Store store;
        switch (view.getName()) {
            case REGISTER_CHANGE:
                store = userService.getUser().getStore();
                if (locationService.getStore() != null) {
                    store = locationService.getStore();
                }
                String registerNumber = (locationService.getStoreRegister() != null) ? locationService.getStoreRegister().getNumber().toString() : "";
                responseForm.setValueByKey("storeNumber", store != null ? store.getNumber().toString() : "");
                responseForm.setValueByKey("registerNumber", registerNumber);
                if (userService.isUserManagerOrAdmin()) {
                    responseForm.findByKey("storeNumber").setDisabled(false);;
                }
                break;
            case REPORTS:
                if (requestForm.findByKey("scope") != null) {
                    responseForm.setValueByKey("scope", requestForm.getValueByKey("scope"));
                    responseForm.setValueByKey("report", requestForm.getValueByKey("report"));
                    responseForm.setValueByKey("days", requestForm.getValueByKey("days"));
                } else {
                    responseForm.setValueByKey("scope", "Register");
                    responseForm.setValueByKey("report", "Transactions");
                    responseForm.setValueByKey("days", "1");
                }
                break;
            case RETURN:
                responseForm.setValueByKey("store", locationService.getStore().getNumber());
                break;
            case RETURN_VIEW:
                responseForm.deleteElements();
                Integer strNumber = requestForm.getIntegerValueByKey("store");
                Integer regNumber = requestForm.getIntegerValueByKey("register");
                Integer txnNumber = requestForm.getIntegerValueByKey("transactionNumber");
                String date = requestForm.getValueByKey("date");
                Transaction txn = transactionService.getTransaction(strNumber, regNumber, txnNumber, date);
                if (txn == null) {
                    FormElement error = new FormElement();
                    error.setType(Type.ERROR);
                    error.setLabel(translationService.translate("error.txn_not_found"));
                    responseForm.addElement(error);
                    break;
                }
                for (TransactionLine line : txn.getLines()) {
                    String key = txn.getStore().getNumber().toString() +
                        ":" +
                        txn.getRegister().getNumber().toString() +
                        ":" +
                        txn.getNumber().toString() +
                        ":" +
                        line.getNumber().toString() +
                        ":" +
                        line.getId().toString();
                    FormElement element = new FormElement();
                    element.setType(Type.RETURN);
                    element.setKey(key);
                    element.setValue("" + (line.getQuantity() - line.getReturnedQuantity()));
                    element.setQuantity(0);
                    element.setPrice(line.getUnitValue());
                    element.setLabel(line.getProductCode());
                    responseForm.addElement(element);
                }
                break;
            case SEARCH:
                String searchQuery = requestForm.getValueByKey("search");
                responseForm.setElements(productService.search(searchQuery));
                break;
            case STORE_SETUP:
                responseForm.setValueByKey("name", locationService.getStore().getName());
                responseForm.setValueByKey("countryCode", locationService.getStore().getCountryCode().toString());
                responseForm.setValueByKey("currencyCode", locationService.getStore().getCurrencyCode().toString());
                responseForm.setValueByKey("languageCode", locationService.getStore().getLanguageCode().toString());
                responseForm.setValueByKey("address1", locationService.getStore().getAddress1());
                responseForm.setValueByKey("address2", locationService.getStore().getAddress2());
                responseForm.setValueByKey("city", locationService.getStore().getCity());
                responseForm.setValueByKey("postCode", locationService.getStore().getPostCode());
                responseForm.setValueByKey("phoneNumber", locationService.getStore().getPhoneNumber());
                break;
            case USER_EDIT:
                User editUser = userService.getUser(requestForm.getValueByKey("code"));
                responseForm.setValueByKey("code", editUser.getCode());
                FormElement storeElement = responseForm.findByKey("store");
                if (userService.isUserAdmin()) {
                    storeElement.setDisabled(false);
                }
                store = userService.getUser(editUser.getCode()).getStore();
                storeElement.setValue(store == null ? "0" : store.getNumber().toString());
                storeElement.setOptions(getStoreOptions(true));
                responseForm.setValueByKey("name", editUser.getName());
                responseForm.setValueByKey("email", editUser.getEmail());
                responseForm.setValueByKey("password", "");
                FormElement roleElement = responseForm.findByKey("role");
                String[] roleOptions = roleElement.getOptions();
                ArrayList<String> newRoleOptions = new ArrayList<String>();
                for (int i = 0; i < 2; i++) {
                    newRoleOptions.add(roleOptions[i]);
                }
                if (userService.isUserAdmin()) {
                    String adminTranslation = translationService.translate("ui.administrator");
                    newRoleOptions.add(UserRole.ADMIN.toString() + "|" + adminTranslation);
                }
                roleElement.setOptions(new String[0]);
                roleElement.setOptions(newRoleOptions.toArray(new String[0]));
                responseForm.setValueByKey("role", editUser.getRole().toString());
                break;
            case USER_LIST:
                responseForm.deleteElementsAfter(1);
                if (userService.isUserAdmin()) {
                    responseForm.findByKey("stores").setHidden(false);
                    responseForm.findByKey("submit").setHidden(false);
                    String[] stores = getStoreOptions(false);
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
                break;
            default:
                break;
        }
        return view;
    }

    private String[] getStoreOptions(
        Boolean showNoStoreOption
    ) {
        Iterable<Store> stores = locationService.getStores();
        ArrayList<String> storeOptions = new ArrayList<String>();
        if (showNoStoreOption) {
            storeOptions.add("0|" + translationService.translate("ui.no_store"));
        }
        for (Store str : stores) {
            storeOptions.add(str.getNumber() + "|" + str.getNumber() + ": " + str.getName());
        }
        return storeOptions.toArray(new String[0]);
    }

    public View getViewByForm(
        Form requestForm
    ) {
        ViewName viewName = requestForm.getTargetView() == null ? ViewName.HOME : requestForm.getTargetView();
        View view = getViewByName(viewName);
        return enrichView(view, requestForm);
    }
    
    public View getViewByName(
        ViewName viewName
    ) {
        View view = viewConfig.getView(viewName);
        view.setLocale(request.getLocale());
        view.setCacheKey(view.getName() + ":" + view.getLocale().toString());
        view = translationService.translateView(view);
        return view;
    }
    
}
