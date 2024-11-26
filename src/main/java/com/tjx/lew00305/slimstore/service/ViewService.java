package com.tjx.lew00305.slimstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.config.ViewConfig;
import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.common.View;
import com.tjx.lew00305.slimstore.model.common.View.ViewName;
import com.tjx.lew00305.slimstore.model.entity.User;

@Service
public class ViewService {

    @Autowired
    private ViewConfig flowConfig;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    public View[] getAll() {
        return flowConfig.getViews();
    }
    
    public View getViewByForm(Form form) {
        String action = form.getTargetView().toUpperCase().replace("-", "_");
        ViewName actionView = action.isEmpty() ? ViewName.HOME : ViewName.valueOf(action);
        View view = getViewByName(actionView);
        return enrichView(view, actionView, form);
    }
    
    public View getViewByName(ViewName name) {
        View pageNotFound = new View();
        for(View view: flowConfig.getViews()) {
            if(view.getName().equals(name)) {
                return view;
            }
            if(view.getName().equals(ViewName.PAGE_NOT_FOUND)) {
                pageNotFound = view;
            }
        }
        return pageNotFound;        
    }
    
    private View enrichView(View view, ViewName action, Form form) {
        switch (action) {
            case SEARCH:
                String searchQuery = form.getValueByKey("search");
                view.setFormElements(productService.search(searchQuery));
                break;
            case USER_LIST:
                view.setFormElements(userService.getUsersAsFormElements());
                break;
            case REGISTER_CHANGE:
                String storeNumber = (locationService.getStore() != null)
                    ? locationService.getStore().getNumber().toString()
                    : "";
                String registerNumber = (locationService.getStoreRegister() != null)
                    ? locationService.getStoreRegister().getNumber().toString()
                    : "";
                FormElement[] regListElements = view.getFormElements();
                regListElements[0].setValue(storeNumber);
                regListElements[1].setValue(registerNumber);
                view.setFormElements(regListElements);                    
                break;
            case STORE_SETUP:
                FormElement[] storeListElements = view.getFormElements();
                storeListElements[0].setValue("" + locationService.getStore().getName());
                view.setFormElements(storeListElements);
                break;
            case USER_EDIT:
                FormElement[] userListElements = view.getFormElements();
                User editUser = userService.getUser(form.getValueByKey("code"));
                userListElements[0].setValue(editUser.getCode());
                userListElements[1].setValue(editUser.getName());
                userListElements[2].setValue(editUser.getEmail());
                userListElements[3].setValue("");
                view.setFormElements(userListElements);
                break;
            default:
                break;
        }
        return view;
    }
    
}
