package com.tjx.lew00305.slimstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.tjx.lew00305.slimstore.model.Flow;
import com.tjx.lew00305.slimstore.service.ProductService;

@Configuration
@ImportResource({
    "classpath:flows/home.xml",
    "classpath:flows/login.xml",
    "classpath:flows/reports.xml",
    "classpath:flows/return.xml",
    "classpath:flows/return-manual.xml",
    "classpath:flows/sale.xml",
    "classpath:flows/search.xml",
    "classpath:flows/system.xml",
    "classpath:flows/user-new.xml",
    "classpath:flows/void.xml",
})
public class FlowConfig {
        
    @Autowired    
    private Flow[] flows;
        
    public Flow[] get() {
        return flows;
    }
}
