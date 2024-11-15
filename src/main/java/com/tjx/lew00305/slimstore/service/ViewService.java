package com.tjx.lew00305.slimstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import com.tjx.lew00305.slimstore.config.ViewConfig;
import com.tjx.lew00305.slimstore.model.View;

@Service
public class ViewService {

    @Autowired
    private ViewConfig flowConfig;
    
    public View[] getAll() {
        return flowConfig.getViews();
    }
    
    public View getView(String name) {
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
}
