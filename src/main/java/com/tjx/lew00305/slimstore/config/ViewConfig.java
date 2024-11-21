package com.tjx.lew00305.slimstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.tjx.lew00305.slimstore.model.common.View;

@Configuration
@ImportResource("classpath:view/**/*.xml")
public class ViewConfig {
        
    @Autowired    
    private View[] views;
        
    public View[] getViews() {
        return views;
    }
}
