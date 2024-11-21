package com.tjx.lew00305.slimstore.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.config.BarcodeConfig;
import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.enums.Banner;
import com.tjx.lew00305.slimstore.enums.Region;
import com.tjx.lew00305.slimstore.model.common.Barcode;
import com.tjx.lew00305.slimstore.model.common.BarcodeSpecification;

@Service
public class BarcodeService {
    
    @Autowired
    private BarcodeConfig barcodeConfig;
    
    public BarcodeSpecification getSpecifiction(Region region, Banner banner) {
        for(BarcodeSpecification spec : barcodeConfig.getBarcodeSpecifications()) {
            if(spec.getRegion().equals(region.name()) && spec.getBanner().equals(banner.name())) {
                return spec;
            }
        }
        return null;
    }
    
    public boolean barcodeCheck(String value) {
        BarcodeSpecification spec = getSpecifiction(Region.EU, Banner.TKMAXX);
        String regExpn = "^[0-9]{" + spec.getLength() + "}$";
        Pattern pattern = Pattern.compile(regExpn);
        Matcher matcher = pattern.matcher(value);
        if(matcher.matches()) {
            return true;
        }
        return false;
    }
    
    public Barcode getBarcode(String value) {
        BarcodeSpecification spec = getSpecifiction(Region.EU, Banner.TKMAXX);
        if(!barcodeCheck(value)) {
            return null;
        }
        int counter = 0;
        int length = 0;
        Barcode barcode = new Barcode();
        length = spec.getDivision();
        if(length > 0) {
            barcode.setDivision(Integer.parseInt(value.substring(counter, counter + length)));
            counter += length;            
        }
        length = spec.getDepartment();
        if(length > 0) {
            barcode.setDepartment(Integer.parseInt(value.substring(counter, counter + length)));
            counter += length;
        }
        length = spec.getCategory();
        if(length > 0) {
            barcode.setCategory(Integer.parseInt(value.substring(counter, counter + length)));
            counter += length;
        }
        length = spec.getStyle();
        if(length > 0) {barcode.setStyle(Integer.parseInt(value.substring(counter, counter + length)));
            counter += length;
            barcode.setPrice(Float.parseFloat(value.substring(counter, counter + length)) / 100);
        }
        length = spec.getWeek();
        if(length > 0) {counter += length;
            barcode.setWeek(Integer.parseInt(value.substring(counter, counter + length)));        
        }
        return barcode;
    }

    public Barcode getBarcodeByRequest(RegisterRequestDTO request) {
        if(request.getFormElements().length == 0) {
            return null;
        }
        return getBarcode(request.getFormElements()[0].getValue());
    }
        
}
