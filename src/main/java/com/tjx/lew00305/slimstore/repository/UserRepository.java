package com.tjx.lew00305.slimstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    
    User findByCode(
        String code
    );

    Iterable<User> findByStore(
        Store store
    );
    
}
