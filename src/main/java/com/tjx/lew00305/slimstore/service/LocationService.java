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
        StoreRegister storeRegister = storeRegisterRepository.findByNumber(registerNumber);
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
        StoreRegister storeRegister = storeRegisterRepository.findByNumber(registerNumber);
        if(store == null || storeRegister == null) {
            return null;            
        }
        locationSession.setStore(store);
        locationSession.setStoreRegister(storeRegister);
        return locationSession;
    }

}
