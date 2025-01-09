package com.tjx.lew00305.slimstore.barcode;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.location.Store.Banner;
import com.tjx.lew00305.slimstore.location.Store.Region;
import com.tjx.lew00305.slimstore.register.form.Form;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BarcodeService {
    
    private final BarcodeConfig barcodeConfig;
    
    public Barcode getBarcode(
        String value
    ) {
        BarcodeSpecification spec = getSpecifiction(Region.EU, Banner.TKMAXX);
        if (!isBarcode(value)) {
            return null;
        }
        int counter = 0;
        int length = 0;
        Barcode barcode = new Barcode();
        length = spec.getDivision();
        if (length > 0) {
            barcode.setDivision(Integer.parseInt(value.substring(counter, counter + length)));
            counter += length;
        }
        length = spec.getDepartment();
        if (length > 0) {
            barcode.setDepartment(Integer.parseInt(value.substring(counter, counter + length)));
            counter += length;
        }
        length = spec.getCategory();
        if (length > 0) {
            barcode.setCategory(Integer.parseInt(value.substring(counter, counter + length)));
            counter += length;
        }
        length = spec.getStyle();
        if (length > 0) {
            barcode.setStyle(Integer.parseInt(value.substring(counter, counter + length)));
            counter += length;
            BigDecimal price = new BigDecimal(value.substring(counter, counter + length));
            price = price.movePointLeft(2);
            barcode.setPrice(price);
        }
        length = spec.getWeek();
        if (length > 0) {
            counter += length;
            barcode.setWeek(Integer.parseInt(value.substring(counter, counter + length)));
        }
        return barcode;
    }
    
    public Barcode getBarcodeByForm(
        Form requestForm
    ) {
        return getBarcode(requestForm.getValueByKey("search"));
    }
    
    private BarcodeSpecification getSpecifiction(
        Region region,
        Banner banner
    ) {
        for (BarcodeSpecification spec : barcodeConfig.getBarcodeSpecifications()) {
            if (spec.getRegion().equals(region.name()) &&
                spec.getBanner().equals(banner.name())) {
                return spec;
            }
        }
        return null;
    }
    
    // "9206603710000999"
    private boolean isBarcode(
        String value
    ) {
        if (value == null) {
            return false;
        }
        BarcodeSpecification spec = getSpecifiction(Region.EU, Banner.TKMAXX);
        String regExpn = "^[0-9]{" + spec.getLength() + "}$";
        Pattern pattern = Pattern.compile(regExpn);
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    
}
