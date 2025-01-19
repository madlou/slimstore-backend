package com.tjx.lew00305.slimstore.user;

import org.springframework.data.repository.CrudRepository;

import com.tjx.lew00305.slimstore.store.Store;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByCode(
        String code
    );
    
    Iterable<User> findByStore(
        Store store
    );

    User getReferenceById(
        Integer id
    );

}
