package com.tjx.lew00305.slimstore.service;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.entity.Store;
import com.tjx.lew00305.slimstore.entity.StoreRegister;

@Service
public class LocationService {

    public Store getStore(int i) {
        // TODO Auto-generated method stub
        Store dummy = new Store();
        dummy.setId(1);
        dummy.setNumber(423);
        dummy.setName("Watford");
        dummy.setCountryCode("UK");
        return dummy;
    }

    public StoreRegister getRegister(int i, int j) {
        StoreRegister dummy = new StoreRegister();
        dummy.setId(1);
        dummy.setNumber(2);
        dummy.setStoreId(1);
        dummy.setStatus("Open");
        dummy.setLastTxnNumber(1234);
        return dummy;
    }

}
