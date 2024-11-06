package com.tjx.lew00305.slimstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.tjx.lew00305.slimstore.model.Flow;

@Configuration
@ImportResource({
    "classpath:flows/home.xml",
    "classpath:flows/login.xml",
    "classpath:flows/return.xml",
    "classpath:flows/sale.xml",
    "classpath:flows/system.xml",
    "classpath:flows/user-new.xml",
})
public class FlowConfig {
    
    @Autowired    
    private Flow[] flows;
    
    public Flow[] get() {
        return flows;
    }
}
