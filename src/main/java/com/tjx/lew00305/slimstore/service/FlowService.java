package com.tjx.lew00305.slimstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.config.ViewConfig;
import com.tjx.lew00305.slimstore.model.View;

@Service
public class FlowService {

    @Autowired
    private ViewConfig flowConfig;
    
    public View[] getAll() {
        return flowConfig.get();
    }
    
    public View get(String name) {
        for(View flow: flowConfig.get()) {
            if(flow.getName().equals(name)) {
                return flow;
            }
        }
        return null;
    }
}
