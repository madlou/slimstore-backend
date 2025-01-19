package com.tjx.lew00305.slimstore.store;

import org.springframework.data.repository.CrudRepository;

public interface StoreRepository extends CrudRepository<Store, Integer> {
    
    Store findByNumber(
        Integer number
    );

    Store getReferenceById(
        Integer id
    );
    
}
