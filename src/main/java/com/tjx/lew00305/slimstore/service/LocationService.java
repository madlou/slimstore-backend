package com.tjx.lew00305.slimstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.session.LocationSession;
import com.tjx.lew00305.slimstore.model.session.UserSession;
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
    private UserSession userSession;

    public Store getStore() {
        return locationSession.getStore();
    }

    public StoreRegister getStoreRegister() {
        return locationSession.getStoreRegister();
    }
    
    public LocationSession validateLocation(String storeNumberString, String registerNumberString) {
        Integer storeNumber = Integer.parseInt(storeNumberString);
        Integer registerNumber = Integer.parseInt(registerNumberString);
        Store store = storeRepository.findByNumber(storeNumber);
        StoreRegister storeRegister = store.getRegisterByNumber(registerNumber);
//        StoreRegister storeRegister = storeRegisterRepository.findByNumber(registerNumber);
        if(store == null || storeRegister == null) {
            return null;            
        }
        locationSession.setStore(store);
        locationSession.setStoreRegister(storeRegister);
        return locationSession;
    }

    public LocationSession validateLocationByRequest(RegisterRequestDTO request) {
        Integer storeNumber = Integer.parseInt(request.getFormElements()[0].getValue());
        Integer registerNumber = Integer.parseInt(request.getFormElements()[1].getValue());
        Store store = storeRepository.findByNumber(storeNumber);
        if(store == null) {
            if(userSession.getUser().getCode().equals("admin")) {
                createStore(storeNumber, registerNumber);
                return locationSession;
            } else {
                return null;
            }
        }
        StoreRegister storeRegister = store.getRegisterByNumber(registerNumber);
        if(storeRegister == null) {
            if(userSession.getUser().getCode().equals("admin")) {
                createRegister(store, registerNumber);
                return locationSession;
            } else {
                return null;
            }
        }
        locationSession.setStore(store);
        locationSession.setStoreRegister(storeRegister);
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
    
    public void createStore(Integer storeNumber, Integer registerNumber) {
        Store store = storeRepository.save(new Store(storeNumber, "Unset", "Unset", null));
        StoreRegister storeRegister = storeRegisterRepository.save(new StoreRegister(null, store, registerNumber, "CLOSED", 1, null));
        locationSession.setStore(store);
        locationSession.setStoreRegister(storeRegister);
    }

    public void createRegister(Store store, Integer registerNumber) {
        StoreRegister storeRegister = storeRegisterRepository.save(new StoreRegister(null, store, registerNumber, "CLOSED", 1, null));
        locationSession.setStore(store);
        locationSession.setStoreRegister(storeRegister);
    }
}
