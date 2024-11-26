package com.tjx.lew00305.slimstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.config.ViewConfig;
import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.common.View;
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
    
    public View getViewByRequest(RegisterRequestDTO request) {
        String action = request.getAction();
        action = action.isEmpty() ? "home" : action;
        View view = getViewByName(action);
        return enrichView(view, action, request.getForm());
    }
    
    public View getViewByName(String name) {
        View pageNotFound = new View();
        for(View view: flowConfig.getViews()) {
            if(view.getName().equals(name)) {
                return view;
            }
            if(view.getName().equals("404")) {
                pageNotFound = view;
            }
        }
        return pageNotFound;        
    }
    
    private View enrichView(View view, String action, Form form) {
        switch (action) {
            case "search":
                String searchQuery = form.getValueByKey("search");
                view.setFormElements(productService.search(searchQuery));
                break;
            case "user-list":
                view.setFormElements(userService.getUsersAsFormElements());
                break;
            case "register-change":
                FormElement[] regListElements = view.getFormElements();
                regListElements[0].setValue("" + locationService.getStore().getNumber());
                regListElements[1].setValue("" + locationService.getStoreRegister().getNumber());
                view.setFormElements(regListElements);
                break;
            case "store-setup":
                FormElement[] storeListElements = view.getFormElements();
                storeListElements[0].setValue("" + locationService.getStore().getName());
                view.setFormElements(storeListElements);
                break;
            case "user-edit":
                FormElement[] userListElements = view.getFormElements();
                User editUser = userService.getUser(form.getValueByKey("code"));
                userListElements[0].setValue(editUser.getCode());
                userListElements[1].setValue(editUser.getName());
                userListElements[2].setValue(editUser.getEmail());
                userListElements[3].setValue("");
                view.setFormElements(userListElements);
                break;
        }
        return view;
    }
    
}
