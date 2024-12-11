package com.tjx.lew00305.slimstore.service;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.enums.Country;
import com.tjx.lew00305.slimstore.enums.Currency;
import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.session.LocationSession;
import com.tjx.lew00305.slimstore.repository.StoreRegisterRepository;
import com.tjx.lew00305.slimstore.repository.StoreRepository;

@Service
public class LocationService {

    private LocationSession locationSession;
    private StoreRepository storeRepository;
    private StoreRegisterRepository storeRegisterRepository;
    private TranslationService translationService;

    public LocationService(
        LocationSession locationSession,
        StoreRepository storeRepository,
        StoreRegisterRepository storeRegisterRepository,
        TranslationService translationService
    ) {
        this.locationSession = locationSession;
        this.storeRepository = storeRepository;
        this.storeRegisterRepository = storeRegisterRepository;
        this.translationService = translationService;
    }
    
    private StoreRegister createRegister(
        Store store,
        Integer registerNumber
    ) {
        StoreRegister storeRegister = new StoreRegister();
        storeRegister.setStore(store);
        storeRegister.setNumber(registerNumber);
        storeRegister.setStatus("CLOSED");
        storeRegister.setLastTxnNumber(0);
        storeRegister = storeRegisterRepository.save(storeRegister);
        return storeRegister;
    }

    private Store createStore(
        Integer storeNumber,
        Integer registerNumber
    ) {
        Store store = new Store();
        store.setNumber(storeNumber);
        store.setName("Unset");
        store.setCountryCode(Country.UK);
        store.setCurrencyCode(Currency.GBP);
        store = storeRepository.save(store);
        store.getRegisters().add(createRegister(store, registerNumber));
        return store;
    }

    public Store getStore() {
        return locationSession.getStore();
    }

    public Store getStore(
        Integer number
    ) {
        return storeRepository.findByNumber(number);
    }

    public StoreRegister getStoreRegister() {
        return locationSession.getStoreRegister();
    }

    public Iterable<Store> getStores() {
        return storeRepository.findAll();
    }
    
    public void setLocation(
        Integer storeNumber,
        Integer registerNumber
    ) {
        Store store = storeRepository.findByNumber(storeNumber);
        StoreRegister storeRegister = store.getRegisterByNumber(registerNumber);
        setLocation(store, storeRegister);
    }

    public void setLocation(
        Store store,
        StoreRegister register
    ) {
        if ((store != null) &&
            (register != null)) {
            locationSession.setStore(store);
            locationSession.setStoreRegister(register);
        }
    }

    public void setLocation(
        String storeNumber,
        String registerNumber
    ) {
        setLocation(Integer.parseInt(storeNumber), Integer.parseInt(registerNumber));
    }

    public void setTransactionNumber(
        Integer id,
        Integer txnNumber
    ) {
        StoreRegister reg = storeRegisterRepository.findById(id).orElse(null);
        reg.setLastTxnNumber(txnNumber);
        storeRegisterRepository.save(reg);
        locationSession.getStoreRegister().setLastTxnNumber(txnNumber);;
    }

    public void updateStoreByForm(
        Form requestForm
    ) {
        Store store = getStore();
        store.setName(requestForm.getValueByKey("name"));
        store.setCountryCode(Country.valueOf(requestForm.getValueByKey("countryCode")));
        store.setCurrencyCode(Currency.valueOf(requestForm.getValueByKey("currencyCode")));
        store.setAddress1(requestForm.getValueByKey("address1"));
        store.setAddress2(requestForm.getValueByKey("address2"));
        store.setCity(requestForm.getValueByKey("city"));
        store.setPostCode(requestForm.getValueByKey("postCode"));
        store.setPhoneNumber(requestForm.getValueByKey("phoneNumber"));
        storeRepository.save(store);
    }
    
    public String validateLocationByForm(
        Form requestForm,
        Boolean isAdmin
    ) {
        Integer storeNumber = requestForm.getIntegerValueByKey("storeNumber");
        Integer registerNumber = requestForm.getIntegerValueByKey("registerNumber");
        Store store = storeRepository.findByNumber(storeNumber);
        if (store == null) {
            if (isAdmin) {
                store = createStore(storeNumber, registerNumber);
            } else {
                return translationService.translate("location_invalid_store");
            }
        }
        StoreRegister storeRegister = store.getRegisterByNumber(registerNumber);
        if (storeRegister == null) {
            if (isAdmin) {
                storeRegister = createRegister(store, registerNumber);
            } else {
                return translationService.translate("location_invalid_register");
            }
        }
        setLocation(store, storeRegister);
        return null;
    }
    
}
