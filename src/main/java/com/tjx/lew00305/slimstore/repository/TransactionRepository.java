package com.tjx.lew00305.slimstore.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.entity.Transaction;
import com.tjx.lew00305.slimstore.model.report.TransactionTenderAggregationInterface;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    List<Transaction> findByRegisterAndDateBetweenOrderByDateAsc(StoreRegister register, Date start, Date stop);
    List<Transaction> findByStoreAndDateBetweenOrderByDateAsc(Store store, Date start, Date stop);
    List<Transaction> findByDateBetweenOrderByDateAsc(Date start, Date stop);    
    @Query(value = 
        "SELECT " + 
        "s.number AS 'store', " +
        "s.name AS 'storeName', " +
        "DATE(x.date) AS 'date', " +
        "r.number AS 'reg', " +
        "t.type AS 'type', " +
        "ROUND(SUM(t.value), 2) AS 'value' " +
        "FROM store s " +
        "LEFT JOIN store_register r ON (s.number = r.store_id) " +
        "LEFT JOIN transaction x ON (r.id = x.register_id) " +
        "LEFT JOIN transaction_tender t ON (x.id = t.transaction_id) " +
        "WHERE " +
        "r.number LIKE ?1 AND " +
        "s.number LIKE ?2 AND " +
        "DATE(x.date) >= ?3 AND " +
        "DATE(x.date) <= ?4 " +
        "GROUP BY " +
        "s.number, " +
        "s.name, " +
        "DATE(x.date), " +
        "r.number, " +
        "t.type " +
        "HAVING value > 0 " +
        "ORDER BY " +
        "s.number ASC, " +
        "r.number ASC, " +
        "DATE(x.date) ASC, " +
        "t.type ASC "
        , nativeQuery = true)
    List<TransactionTenderAggregationInterface> aggregateTenders(String reg, String store, String start, String stop);
    
}

//record TransactionsOnly(
//    Store store,
//    Integer number,
//    Timestamp date,
//    Float total
//) {}
//
//record TransactionsAndTenders(
//    Store store, 
//    Integer number, 
//    Timestamp date, 
//    Float total, 
//    List<TransactionTender> tenders
//) {}
//
//record TransactionsAndLines(
//    Store store, 
//    Integer number, 
//    Timestamp date, 
//    Float total, 
//    List<TransactionLine> lines
//) {}
