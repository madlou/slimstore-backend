package com.tjx.lew00305.slimstore.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.model.entity.Transaction;
import com.tjx.lew00305.slimstore.model.entity.TransactionLine;
import com.tjx.lew00305.slimstore.model.entity.TransactionTender;
import com.tjx.lew00305.slimstore.model.session.BasketLine;
import com.tjx.lew00305.slimstore.model.session.TenderLine;
import com.tjx.lew00305.slimstore.repository.TransactionLineRepository;
import com.tjx.lew00305.slimstore.repository.TransactionRepository;
import com.tjx.lew00305.slimstore.repository.TransactionTenderRepository;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository txnRepo;

    @Autowired
    private TransactionLineRepository lineRepo;
    
    @Autowired
    private TransactionTenderRepository tenderRepo;
    
    @Autowired
    private LocationService locationService;

    @Autowired
    private BasketService basketService;
    
    @Autowired
    private TenderService tenderService;

    @Autowired
    private UserService userService;

    public void addTransaction() {
        Integer txnNumber = locationService.getStoreRegister().getLastTxnNumber() + 1;
        locationService.setTransactionNumber(locationService.getStoreRegister().getId(), txnNumber);
        Transaction transaction = new Transaction();
        transaction.setStore(locationService.getStore());
        transaction.setRegister(locationService.getStoreRegister());
        transaction.setUser(userService.getUser());
        transaction.setNumber(txnNumber);
        transaction.setDate(new Timestamp(System.currentTimeMillis()));
        transaction.setTotal(basketService.getTotal());
        transaction = txnRepo.save(transaction);
        Integer counter = 1;
        for(BasketLine basketLine : basketService.getBasketArray()) {
            TransactionLine txnLine = new TransactionLine();
            txnLine.setTransaction(transaction);
            txnLine.setNumber(counter++);
            txnLine.setProductCode(basketLine.getCode());
            txnLine.setType(basketLine.getType());
            txnLine.setQuantity(basketLine.getQuantity());
            txnLine.setUnitValue(basketLine.getUnitValue());
            txnLine.setLineValue(basketLine.getQuantity() * basketLine.getUnitValue());
            lineRepo.save(txnLine);            
        }
        counter = 1;
        for(TenderLine tenderLine : tenderService.getTenderArray()) {
            TransactionTender txnTender = new TransactionTender();
            txnTender.setTransaction(transaction);
            txnTender.setNumber(counter++);
            txnTender.setType(tenderLine.getType());
            txnTender.setValue(tenderLine.getValue());
            txnTender.setReference(tenderLine.getReference());
            tenderRepo.save(txnTender);            
        }
    }
    
}
