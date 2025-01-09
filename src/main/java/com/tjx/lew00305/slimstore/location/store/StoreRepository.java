package com.tjx.lew00305.slimstore.location.store;

import org.springframework.data.repository.CrudRepository;

public interface StoreRepository extends CrudRepository<Store, Integer> {

    Store findByNumber(
        Integer number
    );

}
