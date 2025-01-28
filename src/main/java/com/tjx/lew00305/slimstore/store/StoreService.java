package com.tjx.lew00305.slimstore.store;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.register.RegisterChangeException;
import com.tjx.lew00305.slimstore.register.form.Form;
import com.tjx.lew00305.slimstore.store.Store.Country;
import com.tjx.lew00305.slimstore.store.Store.Currency;
import com.tjx.lew00305.slimstore.translation.Language;
import com.tjx.lew00305.slimstore.translation.TranslationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {
    
    private final StoreRepository storeRepository;
    private final TranslationService translationService;

    private final Store store;

    private Store addStore(
        Integer storeNumber
    ) {
        Store store = new Store();
        store.setNumber(storeNumber);
        store.setName("Unset");
        store.setCountryCode(Country.UK);
        store.setCurrencyCode(Currency.GBP);
        store = storeRepository.save(store);
        return store;
    }
    
    public Store getStore() {
        return store;
    }
    
    public Store getStore(
        Integer number
    ) {
        return storeRepository.findByNumber(number);
    }
    
    public Store getStoreFromDb() {
        return storeRepository.findById(store.getNumber()).orElse(null);
    }
    
    public Store getStoreReference() {
        return storeRepository.getReferenceById(store.getNumber());
    }
    
    public Iterable<Store> getStores() {
        return storeRepository.findAll();
    }
    
    public void saveStoreByForm(
        Form requestForm
    ) {
        Store dbStore = getStore(store.getNumber());
        dbStore.setName(requestForm.getValueByKey("name"));
        dbStore.setCountryCode(Country.valueOf(requestForm.getValueByKey("countryCode")));
        dbStore.setCurrencyCode(Currency.valueOf(requestForm.getValueByKey("currencyCode")));
        dbStore.setLanguageCode(Language.valueOf(requestForm.getValueByKey("languageCode")));
        dbStore.setAddress1(requestForm.getValueByKey("address1"));
        dbStore.setAddress2(requestForm.getValueByKey("address2"));
        dbStore.setCity(requestForm.getValueByKey("city"));
        dbStore.setPostCode(requestForm.getValueByKey("postCode"));
        dbStore.setPhoneNumber(requestForm.getValueByKey("phoneNumber"));
        dbStore = storeRepository.save(dbStore);
        updateStore(dbStore);
    }

    public Store setStore(
        Integer storeNumber
    ) throws Exception {
        Store dbStore = storeRepository.findByNumber(storeNumber);
        if (dbStore == null) {
            return null;
        }
        updateStore(dbStore);
        return store;
    }
    
    public void setStoreByForm(
        Form form,
        Boolean isUserAdmin
    ) throws Exception {
        Integer storeNumber = form.getIntegerValueByKey("storeNumber");
        Store dbStore = setStore(storeNumber);
        if (dbStore == null) {
            System.out.println("admin?: " + isUserAdmin);
            if (isUserAdmin) {
                updateStore(addStore(storeNumber));
            } else {
                throw new RegisterChangeException(translationService.translate("error.location_invalid_store"));
            }
        }
    }

    public void updateStore(
        Store store
    ) {
        this.store.setAddress1(store.getAddress1());
        this.store.setAddress2(store.getAddress2());
        this.store.setCity(store.getCity());
        this.store.setCountryCode(store.getCountryCode());
        this.store.setCurrencyCode(store.getCurrencyCode());
        this.store.setLanguageCode(store.getLanguageCode());
        this.store.setName(store.getName());
        this.store.setNumber(store.getNumber());
        this.store.setPhoneNumber(store.getPhoneNumber());
        this.store.setPostCode(store.getPostCode());
    }

}
