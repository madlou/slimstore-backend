package com.tjx.lew00305.slimstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.tjx.lew00305.slimstore.model.common.BarcodeSpecification;

@Configuration
@ImportResource("classpath:barcode/*.xml")
public class BarcodeConfig {
    
    @Autowired
    private BarcodeSpecification[] barcodeSpecifications;
    
    public BarcodeSpecification[] getBarcodeSpecifications() {
        return barcodeSpecifications;
    }
}
