package com.tjx.lew00305.slimstore.location;

import org.springframework.data.repository.CrudRepository;

public interface StoreRegisterRepository extends CrudRepository<StoreRegister, Integer> {
    
    StoreRegister findByNumber(
        Integer number
    );
    
}
