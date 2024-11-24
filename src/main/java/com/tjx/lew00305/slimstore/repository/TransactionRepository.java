package com.tjx.lew00305.slimstore.repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.entity.Transaction;
import com.tjx.lew00305.slimstore.model.entity.TransactionLine;
import com.tjx.lew00305.slimstore.model.entity.TransactionTender;
import com.tjx.lew00305.slimstore.model.entity.User;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    List<Transaction> findByRegister(StoreRegister register);
    List<Transaction> findByDate(Timestamp date);
    List<Transaction> findByRegisterAndDate(StoreRegister register, Timestamp date);
    List<Transaction> findByUser(User user);
    List<Transaction> findByRegisterAndUser(StoreRegister register, User user);
    List<Transaction> findByDateBetweenOrderByDateDesc(Date start, Date stop);
    List<Transaction> findByRegisterAndDateBetweenOrderByDateAsc(StoreRegister register, Date start, Date stop);
    List<Transaction> findByRegisterAndDateBetweenOrderByDateDesc(StoreRegister register, Date start, Date stop);
    List<Transaction> findByStore(Store store);
    List<Transaction> findByStoreAndDateBetweenOrderByDateAsc(Store store, Date start, Date stop);
    List<Transaction> findByStoreAndDateBetweenOrderByDateDesc(Store store, Date start, Date stop);
    
}

record TransactionsOnly(
    Store store,
    Integer number,
    Timestamp date,
    Float total
) {}

record TransactionsAndTenders(
    Store store, 
    Integer number, 
    Timestamp date, 
    Float total, 
    List<TransactionTender> tenders
) {}

record TransactionsAndLines(
    Store store, 
    Integer number, 
    Timestamp date, 
    Float total, 
    List<TransactionLine> lines
) {}
