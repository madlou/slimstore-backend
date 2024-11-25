package com.tjx.lew00305.slimstore.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.dto.RegisterRequestDTO;
import com.tjx.lew00305.slimstore.model.common.Form;
import com.tjx.lew00305.slimstore.model.entity.Store;
import com.tjx.lew00305.slimstore.model.entity.StoreRegister;
import com.tjx.lew00305.slimstore.model.entity.Transaction;
import com.tjx.lew00305.slimstore.model.entity.TransactionLine;
import com.tjx.lew00305.slimstore.model.entity.TransactionTender;
import com.tjx.lew00305.slimstore.model.report.TransactionFlat;
import com.tjx.lew00305.slimstore.model.report.TransactionLineFlat;
import com.tjx.lew00305.slimstore.model.report.TransactionTenderAggregation;
import com.tjx.lew00305.slimstore.model.report.TransactionTenderAggregationInterface;
import com.tjx.lew00305.slimstore.model.report.TransactionTenderFlat;
import com.tjx.lew00305.slimstore.repository.TransactionRepository;

@Service
public class TransactionReportService {
    
    
    @Autowired
    private TransactionRepository txnRepo;
   
    @Autowired
    private LocationService locationService;
    
    public Iterable<Transaction> getTransactionReport(){
        return txnRepo.findAll();
    }
    
    @SuppressWarnings("rawtypes")
    public List runReportByRequest(RegisterRequestDTO request) {
        Form form = request.getForm();
        return runReport(
            form.getValueByKey("scope"),
            form.getValueByKey("report"),
            form.getIntegerValueByKey("days")
        );
    }
    
    @SuppressWarnings("rawtypes")
    public List runReport(String scope, String name, Integer days) {
        String reportName = scope + " " + name;
        java.util.Date day = new Date(System.currentTimeMillis());
        Date start = new Date(day.getTime() - (86400000L * (days)));
        String startString = start.toString();
        Date stop = new Date(day.getTime());
        String stopString = stop.toString();
        Store store = locationService.getStore();
        String storeNumber = "" + store.getNumber();
        StoreRegister register = locationService.getStoreRegister();
        String regNumber = "" + register.getNumber();
        switch(reportName) {
            case "Register Transactions":
                return getTransactionFlat(
                    txnRepo.findByRegisterAndDateBetweenOrderByDateAsc(register , start, stop)
                );
            case "Store Transactions":
                return getTransactionFlat(
                    txnRepo.findByStoreAndDateBetweenOrderByDateAsc(store , start, stop)
                );
            case "Company Transactions":
                return getTransactionFlat(
                    txnRepo.findByDateBetweenOrderByDateAsc(start, stop)
                );
            case "Register Transaction Lines":
                return getTransactionLineFlat(
                    txnRepo.findByRegisterAndDateBetweenOrderByDateAsc(register , start, stop)
                );
            case "Store Transaction Lines":
                return getTransactionLineFlat(
                    txnRepo.findByStoreAndDateBetweenOrderByDateAsc(store , start, stop)
                );
            case "Company Transaction Lines":
                return getTransactionLineFlat(
                    txnRepo.findByDateBetweenOrderByDateAsc(start, stop)
                );
            case "Register Transaction Tenders":
                return getTransactionTenderFlat(
                    txnRepo.findByRegisterAndDateBetweenOrderByDateAsc(register , start, stop)
                );
            case "Store Transaction Tenders":
                return getTransactionTenderFlat(
                    txnRepo.findByStoreAndDateBetweenOrderByDateAsc(store , start, stop)
                );
            case "Company Transaction Tenders":
                return getTransactionTenderFlat(
                    txnRepo.findByDateBetweenOrderByDateAsc(start, stop)
                );
            case "Register Aggregate Tenders":
                return getTransactionTenderAggregation(
                    txnRepo.aggregateTenders(regNumber, storeNumber, startString, stopString)
                );
            case "Store Aggregate Tenders":
                return getTransactionTenderAggregation(
                    txnRepo.aggregateTenders("%", storeNumber, startString, stopString)
                );
            case "Company Aggregate Tenders":
                return getTransactionTenderAggregation(
                    txnRepo.aggregateTenders("%", "%", startString, stopString)
                );
        }
        return null;
    }
    
    private List<TransactionFlat> getTransactionFlat(List<Transaction> data) {
        ArrayList<TransactionFlat> report = new ArrayList<TransactionFlat>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        for(Transaction txnRow : data) {
            TransactionFlat line = new TransactionFlat();
            line.setStore(txnRow.getStore().getNumber());
            line.setStoreName(txnRow.getStore().getName());
            line.setDate(dateFormatter.format(txnRow.getDate()));
            line.setTime(timeFormatter.format(txnRow.getDate()));            
            line.setReg(txnRow.getRegister().getNumber());
            line.setTxn(txnRow.getNumber());
            line.setUser(txnRow.getUser().getName());
            line.setTotal(txnRow.getTotal());
            report.add(line);
        }
        return (List<TransactionFlat>) report;
    }

    private List<TransactionLineFlat> getTransactionLineFlat(List<Transaction> data) {
        ArrayList<TransactionLineFlat> report = new ArrayList<TransactionLineFlat>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        for(Transaction txnRow : data) {
            for(TransactionLine lineRow : txnRow.getLines()) {
                TransactionLineFlat line = new TransactionLineFlat();
                line.setStore(txnRow.getStore().getNumber());
                line.setStoreName(txnRow.getStore().getName());
                line.setDate(dateFormatter.format(txnRow.getDate()));
                line.setTime(timeFormatter.format(txnRow.getDate()));            
                line.setReg(txnRow.getRegister().getNumber());
                line.setTxn(txnRow.getNumber());
                line.setNumber(lineRow.getNumber());
                line.setProduct(lineRow.getProductCode());
                line.setType(lineRow.getType());
                line.setQuantity(lineRow.getQuantity());
                line.setLineValue(lineRow.getLineValue());
                report.add(line);                
            }
        }
        return (List<TransactionLineFlat>) report;
    }
    
    private List<TransactionTenderFlat> getTransactionTenderFlat(List<Transaction> data) {
        ArrayList<TransactionTenderFlat> report = new ArrayList<TransactionTenderFlat>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        for(Transaction txnRow : data) {
            for(TransactionTender tenderRow : txnRow.getTenders()) {
                TransactionTenderFlat line = new TransactionTenderFlat();
                line.setStore(txnRow.getStore().getNumber());
                line.setStoreName(txnRow.getStore().getName());
                line.setDate(dateFormatter.format(txnRow.getDate()));
                line.setTime(timeFormatter.format(txnRow.getDate()));            
                line.setReg(txnRow.getRegister().getNumber());
                line.setTxn(txnRow.getNumber());
                line.setNumber(tenderRow.getNumber());
                line.setType(tenderRow.getType());
                line.setValue(tenderRow.getValue());
                line.setReference(tenderRow.getReference());
                report.add(line);                
            }
        }
        return (List<TransactionTenderFlat>) report;
    }
    
    private List<TransactionTenderAggregation> getTransactionTenderAggregation(List<TransactionTenderAggregationInterface> data){
        ArrayList<TransactionTenderAggregation> report = new ArrayList<TransactionTenderAggregation>();
        for(TransactionTenderAggregationInterface row : data) {
            TransactionTenderAggregation line = new TransactionTenderAggregation();
            line.setStore(row.getStore());
            line.setStoreName(row.getStoreName());
            line.setReg(row.getReg());
            line.setDate(row.getDate());
            line.setType(row.getType());
            line.setValue(row.getValue());
            report.add(line);                
        }
        return (List<TransactionTenderAggregation>) report;
    }
    
}
