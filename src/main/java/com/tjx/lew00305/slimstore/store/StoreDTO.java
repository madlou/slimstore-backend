package com.tjx.lew00305.slimstore.store;

import com.tjx.lew00305.slimstore.store.Store.Country;
import com.tjx.lew00305.slimstore.store.Store.Currency;
import com.tjx.lew00305.slimstore.translation.Language;

import lombok.Data;

@Data
public class StoreDTO {
    
    private Integer number;
    private String name;
    private Country countryCode;
    private Currency currencyCode;
    private String address1;
    private String address2;
    private String city;
    private String postCode;
    private String phoneNumber;
    private Language languageCode;

}
