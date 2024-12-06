package com.tjx.lew00305.slimstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.tjx.lew00305.slimstore.model.entity.StoreRegister;

public interface StoreRegisterRepository extends CrudRepository<StoreRegister, Integer> {

    StoreRegister findByNumber(
        Integer number
    );

}
