package com.tjx.lew00305.slimstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.config.FlowConfig;
import com.tjx.lew00305.slimstore.model.Flow;

@Service
public class FlowService {

    @Autowired
    private FlowConfig flowConfig;
    
    public Flow[] getAll() {
        return flowConfig.get();
    }
    
    public Flow get(String name) {
        for(Flow flow: flowConfig.get()) {
            if(flow.getName().equals(name)) {
                return flow;
            }
        }
        return null;
    }
}
