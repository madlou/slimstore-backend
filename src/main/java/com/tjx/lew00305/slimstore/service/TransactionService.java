package com.tjx.lew00305.slimstore.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.entity.Transaction;
import com.tjx.lew00305.slimstore.model.entity.TransactionLine;
import com.tjx.lew00305.slimstore.model.entity.TransactionLine.TransactionLineType;
import com.tjx.lew00305.slimstore.model.entity.TransactionTender;
import com.tjx.lew00305.slimstore.model.session.BasketLine;
import com.tjx.lew00305.slimstore.model.session.TenderLine;
import com.tjx.lew00305.slimstore.repository.TransactionLineRepository;
import com.tjx.lew00305.slimstore.repository.TransactionRepository;
import com.tjx.lew00305.slimstore.repository.TransactionTenderRepository;

@Service
public class TransactionService {
    
    private TransactionRepository txnRepo;
    private TransactionLineRepository lineRepo;
    private TransactionTenderRepository tenderRepo;
    private LocationService locationService;
    private BasketService basketService;
    private TenderService tenderService;
    private UserService userService;
    
    public TransactionService(
        TransactionRepository txnRepo,
        TransactionLineRepository lineRepo,
        TransactionTenderRepository tenderRepo,
        LocationService locationService,
        BasketService basketService,
        TenderService tenderService,
        UserService userService
    ) {
        this.txnRepo = txnRepo;
        this.lineRepo = lineRepo;
        this.tenderRepo = tenderRepo;
        this.locationService = locationService;
        this.basketService = basketService;
        this.tenderService = tenderService;
        this.userService = userService;
    }

    public void addTransaction() {
        Integer txnNumber = 1 +
            locationService.getStoreRegister().getLastTxnNumber();
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
        for (BasketLine basketLine : basketService.getBasketArray()) {
            TransactionLine txnLine = new TransactionLine();
            String code = basketLine.getCode();
            if (basketLine.getSignedQuantity() < 0) {
                String[] split = basketLine.getCode().split(":");
                Integer lineId = Integer.parseInt(split[4]);
                TransactionLine originalLine = lineRepo.findById(lineId).orElse(null);
                originalLine.setReturnedQuantity(originalLine.getReturnedQuantity() +
                    basketLine.getQuantity());
                lineRepo.save(originalLine);
                txnLine.setOriginalTransactionLineId(lineId);
                code = originalLine.getProductCode();
            }
            txnLine.setTransaction(transaction);
            txnLine.setNumber(counter++);
            txnLine.setProductCode(code);
            TransactionLineType type = basketLine.getSignedQuantity() < 0 ? TransactionLineType.RETURN : TransactionLineType.SALE;
            txnLine.setType(type);
            txnLine.setQuantity(basketLine.getSignedQuantity());
            txnLine.setUnitValue(basketLine.getUnitValue());
            txnLine.setLineValue(basketLine.getLineValue());
            txnLine.setReturnedQuantity(0);
            lineRepo.save(txnLine);
        }
        counter = 1;
        for (TenderLine tenderLine : tenderService.getTenderArray()) {
            TransactionTender txnTender = new TransactionTender();
            txnTender.setTransaction(transaction);
            txnTender.setNumber(counter++);
            txnTender.setType(tenderLine.getType());
            txnTender.setValue(tenderLine.getValue());
            txnTender.setReference(tenderLine.getReference());
            tenderRepo.save(txnTender);
        }
    }
    
    public Transaction getTransaction(
        Integer storeNumber,
        Integer regNumber,
        Integer txnNumber,
        String date
    ) {
        Store store = locationService.getStore(storeNumber);
        StoreRegister register = store.getRegisterByNumber(regNumber);
        LocalDateTime start = LocalDateTime.parse(date + "T00:00:00");
        LocalDateTime stop = LocalDateTime.parse(date + "T23:59:59");
        return txnRepo.findByStoreAndRegisterAndNumberAndDateBetween(store, register, txnNumber, start, stop);
    }
    
}
