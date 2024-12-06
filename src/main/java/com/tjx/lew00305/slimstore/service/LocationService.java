package com.tjx.lew00305.slimstore.service;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.session.LocationSession;
import com.tjx.lew00305.slimstore.repository.StoreRegisterRepository;
import com.tjx.lew00305.slimstore.repository.StoreRepository;

@Service
public class LocationService {

    private StoreRepository storeRepository;
    private StoreRegisterRepository storeRegisterRepository;
    private LocationSession locationSession;
    private UserService userService;

    public LocationService(
        StoreRepository storeRepository,
        StoreRegisterRepository storeRegisterRepository,
        LocationSession locationSession,
        UserService userService
    ) {
        this.storeRepository = storeRepository;
        this.storeRegisterRepository = storeRegisterRepository;
        this.locationSession = locationSession;
        this.userService = userService;
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
        store.setCountryCode("Unset");
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
        String storeName = requestForm.getValueByKey("name");
        Store store = getStore();
        store.setName(storeName);
        storeRepository.save(store);
    }

    public String validateLocationByForm(
        Form requestForm
    ) {
        Integer storeNumber = requestForm.getIntegerValueByKey("storeNumber");
        Integer registerNumber = requestForm.getIntegerValueByKey("registerNumber");
        Store store = storeRepository.findByNumber(storeNumber);
        if (store == null) {
            if (userService.getUser().getCode().equals("admin")) {
                store = createStore(storeNumber, registerNumber);
            } else {
                return "Invalid store number.";
            }
        }
        StoreRegister storeRegister = store.getRegisterByNumber(registerNumber);
        if (storeRegister == null) {
            if (userService.getUser().getCode().equals("admin")) {
                storeRegister = createRegister(store, registerNumber);
            } else {
                return "Invalid register number.";
            }
        }
        setLocation(store, storeRegister);
        return null;
    }

}
