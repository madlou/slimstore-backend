package com.tjx.lew00305.slimstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.tjx.lew00305.slimstore.model.common.BarcodeSpecification;

@Configuration
@ImportResource("classpath:barcode/*.xml")
public class BarcodeConfig {
    
    private BarcodeSpecification[] barcodeSpecifications;
    
    public BarcodeConfig(
        BarcodeSpecification[] barcodeSpecifications
    ) {
        this.barcodeSpecifications = barcodeSpecifications;
    }

    public BarcodeSpecification[] getBarcodeSpecifications() {
        return barcodeSpecifications;
    }
}
