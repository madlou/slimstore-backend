package com.tjx.lew00305.slimstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.tjx.lew00305.slimstore.model.entity.Store;

public interface StoreRepository extends CrudRepository<Store, Integer> {

    Store findByNumber(Integer number);
    
}
