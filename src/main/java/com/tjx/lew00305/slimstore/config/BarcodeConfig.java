package com.tjx.lew00305.slimstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.tjx.lew00305.slimstore.model.common.BarcodeSpecification;

import lombok.RequiredArgsConstructor;

@Configuration
@ImportResource("classpath:barcode/*.xml")
@RequiredArgsConstructor
public class BarcodeConfig {

    private BarcodeSpecification[] barcodeSpecifications;
    
    public BarcodeSpecification[] getBarcodeSpecifications() {
        return barcodeSpecifications;
    }
}
