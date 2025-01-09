package com.tjx.lew00305.slimstore.location.register;

import org.springframework.data.repository.CrudRepository;

public interface RegisterRepository extends CrudRepository<Register, Integer> {
    
    Register findByNumber(
        Integer number
    );
    
}
