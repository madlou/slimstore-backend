package com.tjx.lew00305.slimstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.config.ViewConfig;
import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.common.FormElement.Type;
import com.tjx.lew00305.slimstore.model.common.View;
import com.tjx.lew00305.slimstore.model.common.View.ViewName;
import com.tjx.lew00305.slimstore.model.entity.Transaction;
import com.tjx.lew00305.slimstore.model.entity.TransactionLine;
import com.tjx.lew00305.slimstore.model.entity.User;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ViewService {
    
    @Autowired
    private ViewConfig viewConfig;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TranslationService translationService;
    @Autowired
    private HttpServletRequest request;
    
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
    
    private View enrichView(
        View view,
        Form requestForm
    ) {
        Form responseForm = view.getForm();
        switch (view.getName()) {
            case REGISTER_CHANGE:
                String storeNumber = (locationService.getStore() != null) ? locationService.getStore().getNumber().toString() : "";
                String registerNumber = (locationService.getStoreRegister() != null) ? locationService.getStoreRegister().getNumber().toString() : "";
                responseForm.setValueByKey("storeNumber", storeNumber);
                responseForm.setValueByKey("registerNumber", registerNumber);
                break;
            case RETURN:
                responseForm.setValueByKey("store", locationService.getStore().getNumber());
                break;
            case RETURN_VIEW:
                responseForm.setElements(new FormElement[0]);
                Transaction txn = transactionService.getTransaction(requestForm.getIntegerValueByKey("store"), requestForm.getIntegerValueByKey("register"),
                    requestForm.getIntegerValueByKey("transactionNumber"), requestForm.getValueByKey("date"));
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
                    element.setValue("" +
                        (line.getQuantity() -
                            line.getReturnedQuantity()));
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
                break;
            case USER_EDIT:
                User editUser = userService.getUser(requestForm.getValueByKey("code"));
                responseForm.setValueByKey("code", editUser.getCode());
                responseForm.setValueByKey("name", editUser.getName());
                responseForm.setValueByKey("email", editUser.getEmail());
                responseForm.setValueByKey("password", "");
                break;
            case USER_LIST:
                responseForm.setElements(userService.getUsersAsFormElements());
                break;
            default:
                break;
        }
        return view;
    }
    
}
