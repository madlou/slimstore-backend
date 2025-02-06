package cloud.matthews.slimstore.store;

import cloud.matthews.slimstore.store.Store.Country;
import cloud.matthews.slimstore.store.Store.Currency;
import cloud.matthews.slimstore.translation.Language;

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
