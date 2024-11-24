package com.tjx.lew00305.slimstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.session.LocationSession;
import com.tjx.lew00305.slimstore.repository.StoreRegisterRepository;
import com.tjx.lew00305.slimstore.repository.StoreRepository;

@Service
public class LocationService {
    
    @Autowired
    private StoreRepository storeRepository;
    
    @Autowired
    private StoreRegisterRepository storeRegisterRepository;
    
    @Autowired
    private LocationSession locationSession;

    @Autowired
    private UserService userService;

    public Store getStore() {
        return locationSession.getStore();
    }

    public StoreRegister getStoreRegister() {
        return locationSession.getStoreRegister();
    }
    
    public void setLocation(Integer storeNumber, Integer registerNumber) {
        Store store = storeRepository.findByNumber(storeNumber);
        StoreRegister storeRegister = store.getRegisterByNumber(registerNumber);
        setLocation(store, storeRegister);
    }

    public void setLocation(Store store, StoreRegister register) {
        if(store != null && register != null) {
            locationSession.setStore(store);
            locationSession.setStoreRegister(register);
        }
    }
    
    public LocationSession validateLocationByRequest(RegisterRequestDTO request) {
        Integer storeNumber = Integer.parseInt(request.getFormElements()[0].getValue());
        Integer registerNumber = Integer.parseInt(request.getFormElements()[1].getValue());
        Store store = storeRepository.findByNumber(storeNumber);
        if(store == null) {
            if(userService.getUser().getCode().equals("admin")) {
                createStore(storeNumber, registerNumber);
                return locationSession;
            } else {
                return null;
            }
        }
        StoreRegister storeRegister = store.getRegisterByNumber(registerNumber);
        if(storeRegister == null) {
            if(userService.getUser().getCode().equals("admin")) {
                createRegister(store, registerNumber);
                return locationSession;
            } else {
                return null;
            }
        }
        setLocation(store, storeRegister);
        return locationSession;
    }

    public void setTransactionNumber(Integer id, Integer txnNumber) throws Exception {
        StoreRegister reg = storeRegisterRepository.findById(id).orElse(null);
        if(reg == null) {
            throw new Exception("Register not found on transaction number update!");
        }
        reg.setLastTxnNumber(txnNumber);
        storeRegisterRepository.save(reg);
        locationSession.getStoreRegister().setLastTxnNumber(txnNumber);;
    }
    
    private void createStore(Integer storeNumber, Integer registerNumber) {
        Store store = new Store();
        store.setNumber(storeNumber);
        store.setName("Unset");
        store.setCountryCode("Unset");
        store = storeRepository.save(store);
        store.getRegisters().add(createRegister(store, registerNumber));
        locationSession.setStore(store);
    }

    private StoreRegister createRegister(Store store, Integer registerNumber) {
        StoreRegister storeRegister = new StoreRegister();
        storeRegister.setStore(store);
        storeRegister.setNumber(registerNumber);
        storeRegister.setStatus("CLOSED");
        storeRegister.setLastTxnNumber(0);
        storeRegister = storeRegisterRepository.save(storeRegister);
        locationSession.setStoreRegister(storeRegister);
        return storeRegister;
    }
    
}
