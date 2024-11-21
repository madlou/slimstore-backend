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
    
    public void addTransaction() throws Exception {
        Integer txnNumber = locationService.getStoreRegister().getLastTxnNumber() + 1;
        locationService.setTransactionNumber(locationService.getStoreRegister().getId(),txnNumber);
        txnRepo.save(new Transaction(
            null,
            locationService.getStoreRegister().getId(),
            txnNumber,
            new Timestamp(System.currentTimeMillis()),
            basketService.getTotal()
        ));
        Integer counter = 1;
        for(BasketLine line : basketService.getBasketArray()) {
            lineRepo.save(new TransactionLine(
                null,
                txnNumber,
                counter++,
                null,
                line.getCode(),
                line.getType(),
                line.getQuantity(),
                line.getUnitValue(),
                line.getQuantity() * line.getUnitValue()
            ));            
        }
        counter = 1;
        for(TenderLine line : tenderService.getTenderArray()) {
            tenderRepo.save(new TransactionTender(
                null,
                txnNumber,
                counter++,
                line.getType(),
                line.getValue(),
                line.getReference()
            ));            
        }

    }
    
}
