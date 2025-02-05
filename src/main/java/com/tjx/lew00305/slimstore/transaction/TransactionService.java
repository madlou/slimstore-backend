package com.tjx.lew00305.slimstore.transaction;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.basket.BasketLine;
import com.tjx.lew00305.slimstore.basket.BasketService;
import com.tjx.lew00305.slimstore.register.Register;
import com.tjx.lew00305.slimstore.register.RegisterService;
import com.tjx.lew00305.slimstore.register.form.FormElement.FormElementType;
import com.tjx.lew00305.slimstore.store.Store;
import com.tjx.lew00305.slimstore.store.StoreService;
import com.tjx.lew00305.slimstore.tender.TenderLine;
import com.tjx.lew00305.slimstore.tender.TenderService;
import com.tjx.lew00305.slimstore.transaction.TransactionLine.TransactionLineType;
import com.tjx.lew00305.slimstore.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository txnRepo;
    private final TransactionLineRepository lineRepo;
    private final TransactionTenderRepository tenderRepo;
    private final RegisterService registerService;
    private final StoreService storeService;
    private final BasketService basketService;
    private final TenderService tenderService;
    private final UserService userService;
    
    public void addReview(
        Integer storeNumber, 
        Integer registerNumber, 
        Integer transactionNumber,
        Integer score
    ) {
        txnRepo.setReviewScore(storeNumber, registerNumber, transactionNumber, score);
    }

    public void addTransaction() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Integer txnNumber = registerService.updateRegisterWithTransaction(time);
        Transaction transaction = new Transaction();
        transaction.setStore(storeService.getStoreReference());
        transaction.setRegister(registerService.getRegisterReference());
        transaction.setUser(userService.getUserReference());
        transaction.setNumber(txnNumber);
        transaction.setDate(time);
        transaction.setCurrencyCode(storeService.getStore().getCurrencyCode());
        transaction.setTotal(basketService.getTotal());
        transaction = txnRepo.save(transaction);
        Integer counter = 1;
        for (BasketLine basketLine : basketService.getBasketArray()) {
            TransactionLine txnLine = new TransactionLine();
            String code = basketLine.getCode();
            if (basketLine.getType().equals(FormElementType.RETURN)) {
                String[] split = basketLine.getCode().split(":");
                Integer lineId = Integer.parseInt(split[4]);
                TransactionLine originalLine = lineRepo.findById(lineId).orElse(null);
                originalLine.setReturnedQuantity(originalLine.getReturnedQuantity() + basketLine.getQuantity());
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
        Store store = storeService.getStore(storeNumber);
        Register register = registerService.getRegister(store, regNumber);
        LocalDateTime start = LocalDateTime.parse(date + "T00:00:00");
        LocalDateTime stop = LocalDateTime.parse(date + "T23:59:59");
        return txnRepo.findByStoreAndRegisterAndNumberAndDateBetween(store, register, txnNumber, start, stop);
    }


}
