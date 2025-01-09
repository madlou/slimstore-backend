package com.tjx.lew00305.slimstore.location;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.tjx.lew00305.slimstore.location.register.Register;
import com.tjx.lew00305.slimstore.location.store.Store;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Component
@SessionScope
public class LocationSession implements Serializable {

    private Store store;
    private Register storeRegister;

}
