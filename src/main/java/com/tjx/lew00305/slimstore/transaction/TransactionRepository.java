package com.tjx.lew00305.slimstore.transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tjx.lew00305.slimstore.register.Register;
import com.tjx.lew00305.slimstore.store.Store;
import com.tjx.lew00305.slimstore.transaction.report.TransactionTenderAggregationInterface;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Query(value = """
            SELECT
            s.number AS 'store',
            s.name AS 'storeName',
            DATE(x.date) AS 'date',
            r.number AS 'register',
            t.type AS 'type',
            ROUND(SUM(t.value), 2) AS 'value'
            FROM store s
            LEFT JOIN store_register r ON (s.number = r.store_id)
            LEFT JOIN transaction x ON (r.id = x.register_id)
            LEFT JOIN transaction_tender t ON (x.id = t.transaction_id)
            WHERE
            r.number LIKE ?1 AND
            s.number LIKE ?2 AND
            DATE(x.date) >= ?3 AND
            DATE(x.date) <= ?4
            GROUP BY
            s.number,
            s.name,
            DATE(x.date),
            r.number,
            t.type
            HAVING t.type != ""
            ORDER BY
            s.number ASC,
            r.number ASC,
            DATE(x.date) ASC,
            t.type ASC
        """, nativeQuery = true)
    List<TransactionTenderAggregationInterface> aggregateTenders(
        String reg,
        String store,
        LocalDateTime start,
        LocalDateTime stop
    );

    List<Transaction> findByDateBetweenOrderByDateAsc(
        LocalDateTime start,
        LocalDateTime stop
    );

    List<Transaction> findByRegisterAndDateBetweenOrderByDateAsc(
        Register register,
        LocalDateTime start,
        LocalDateTime stop
    );
    
    List<Transaction> findByStoreAndDateBetweenOrderByDateAsc(
        Store store,
        LocalDateTime start,
        LocalDateTime stop
    );

    Transaction findByStoreAndRegisterAndNumberAndDateBetween(
        Store store,
        Register regNumber,
        Integer txnNumber,
        LocalDateTime start,
        LocalDateTime stop
    );
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
