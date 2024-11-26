package com.tjx.lew00305.slimstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.config.ViewConfig;
import com.tjx.lew00305.slimstore.model.common.Form;
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
    
    public View getViewByForm(Form requestForm) {
        String viewNameString = requestForm.getTargetView().toUpperCase().replace("-", "_");
        ViewName viewName = viewNameString.isEmpty() ? ViewName.HOME : ViewName.valueOf(viewNameString);
        View view = getViewByName(viewName);
        return enrichView(view, requestForm);
    }
    
    public View getViewByName(ViewName viewName) {
        View pageNotFound = new View();
        for(View view: flowConfig.getViews()) {
            if(view.getName().equals(viewName)) {
                return view;
            }
            if(view.getName().equals(ViewName.PAGE_NOT_FOUND)) {
                pageNotFound = view;
            }
        }
        return pageNotFound;        
    }
    
    private View enrichView(View view, Form requestForm) {
        Form responseForm = view.getForm();
        switch (view.getName()) {
            case SEARCH:
                String searchQuery = requestForm.getValueByKey("search");
                responseForm.setElements(productService.search(searchQuery));
                break;
            case USER_LIST:
                responseForm.setElements(userService.getUsersAsFormElements());
                break;
            case REGISTER_CHANGE:
                String storeNumber = (locationService.getStore() != null)
                    ? locationService.getStore().getNumber().toString()
                    : "";
                String registerNumber = (locationService.getStoreRegister() != null)
                    ? locationService.getStoreRegister().getNumber().toString()
                    : "";
                responseForm.setValueByKey("storeNumber", storeNumber);
                responseForm.setValueByKey("registerNumber", registerNumber);
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
            default:
                break;
        }
        return view;
    }
    
}
