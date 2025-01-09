package com.tjx.lew00305.slimstore.location;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Component
@SessionScope
public class LocationSession implements Serializable {

    private Store store;
    private StoreRegister storeRegister;

}
