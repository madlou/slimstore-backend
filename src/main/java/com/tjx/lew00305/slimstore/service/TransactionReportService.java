package com.tjx.lew00305.slimstore.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.entity.Transaction;
import com.tjx.lew00305.slimstore.model.report.TransactionFlat;
import com.tjx.lew00305.slimstore.repository.TransactionRepository;

@Service
public class TransactionReportService {
    
    
    @Autowired
    private TransactionRepository txnRepo;
   
    @Autowired
    private LocationService locationService;
    
    @SuppressWarnings("rawtypes")
    public List runReportByRequest(RegisterRequestDTO request) {
        FormElement[] formElements = request.getFormElements();
        if(formElements == null || formElements.length < 2) {
            return null;
        }
        return runReport(formElements[0].getValue(), Integer.parseInt(formElements[1].getValue()));
    }
    
    @SuppressWarnings("rawtypes")
    public List runReport(String name, Integer days) {
        List<Transaction> data;
        java.util.Date day = new Date(System.currentTimeMillis());
        Date start = new Date(day.getTime() - (86400000L * (days)));
        Date stop = new Date(day.getTime());
        Store store = locationService.getStore();
        StoreRegister register = locationService.getStoreRegister();
        switch(name) {
            case "Store Transactions":
                data = txnRepo.findByStoreAndDateBetweenOrderByDateAsc(store , start, stop);
                return getTransactionFlat(data);
            case "Register Transactions":
                data = txnRepo.findByRegisterAndDateBetweenOrderByDateAsc(register , start, stop);
                return getTransactionFlat(data);
        }
        return null;
    }
    
    private List<TransactionFlat> getTransactionFlat(List<Transaction> data) {
        ArrayList<TransactionFlat> report = new ArrayList<TransactionFlat>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        for(Transaction dataLine : data) {
            TransactionFlat line = new TransactionFlat();
            line.setStore(dataLine.getStore().getNumber());
            line.setStoreName(dataLine.getStore().getName());
            line.setDate(dateFormatter.format(dataLine.getDate()));
            line.setTime(timeFormatter.format(dataLine.getDate()));            
            line.setReg(dataLine.getRegister().getNumber());
            line.setTxn(dataLine.getNumber());
            line.setUser(dataLine.getUser().getName());
            line.setTotal(dataLine.getTotal());
            report.add(line);
        }
        return (List<TransactionFlat>) report;
    }

}
