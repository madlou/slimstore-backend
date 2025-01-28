package com.tjx.lew00305.slimstore.transaction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionTenderRepository extends CrudRepository<TransactionTender, Integer> {
    
}
