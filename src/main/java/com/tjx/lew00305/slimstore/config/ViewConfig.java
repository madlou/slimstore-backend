package com.tjx.lew00305.slimstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.tjx.lew00305.slimstore.model.View;
import com.tjx.lew00305.slimstore.service.ProductService;

@Configuration
@ImportResource({
    "classpath:view/home.xml",
    "classpath:view/login.xml",
    "classpath:view/reports.xml",
    "classpath:view/return.xml",
    "classpath:view/return-manual.xml",
    "classpath:view/sale.xml",
    "classpath:view/search.xml",
    "classpath:view/system.xml",
    "classpath:view/tender.xml",
    "classpath:view/tender/cash.xml",
    "classpath:view/user-new.xml",
    "classpath:view/void.xml",
})
public class ViewConfig {
        
    @Autowired    
    private View[] views;
        
    public View[] getViews() {
        return views;
    }
}
