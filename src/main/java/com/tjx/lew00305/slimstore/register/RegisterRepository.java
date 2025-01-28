package com.tjx.lew00305.slimstore.register;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tjx.lew00305.slimstore.store.Store;

@Repository
public interface RegisterRepository extends CrudRepository<Register, Integer> {

    Register findByStoreAndNumber(
        Store store,
        Integer number
    );
    
    Register getReferenceById(
        Integer id
    );
    
}
