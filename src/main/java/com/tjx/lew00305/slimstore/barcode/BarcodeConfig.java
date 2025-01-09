package com.tjx.lew00305.slimstore.barcode;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import lombok.RequiredArgsConstructor;

@Configuration
@ImportResource("classpath:barcode/*.xml")
@RequiredArgsConstructor
public class BarcodeConfig {
    
    private final BarcodeSpecification[] barcodeSpecifications;

    public BarcodeSpecification[] getBarcodeSpecifications() {
        return barcodeSpecifications;
    }
}
