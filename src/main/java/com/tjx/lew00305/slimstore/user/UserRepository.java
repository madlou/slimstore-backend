package com.tjx.lew00305.slimstore.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tjx.lew00305.slimstore.store.Store;

@Repository
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
