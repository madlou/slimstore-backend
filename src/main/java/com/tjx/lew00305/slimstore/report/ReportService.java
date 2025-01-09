package com.tjx.lew00305.slimstore.report;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.location.LocationService;
import com.tjx.lew00305.slimstore.location.Store;
import com.tjx.lew00305.slimstore.location.StoreRegister;
import com.tjx.lew00305.slimstore.register.form.Form;
import com.tjx.lew00305.slimstore.report.transaction.TransactionAudit;
import com.tjx.lew00305.slimstore.report.transaction.TransactionFlat;
import com.tjx.lew00305.slimstore.report.transaction.TransactionLineFlat;
import com.tjx.lew00305.slimstore.report.transaction.TransactionTenderAggregation;
import com.tjx.lew00305.slimstore.report.transaction.TransactionTenderAggregationInterface;
import com.tjx.lew00305.slimstore.report.transaction.TransactionTenderFlat;
import com.tjx.lew00305.slimstore.transaction.Transaction;
import com.tjx.lew00305.slimstore.transaction.TransactionLine;
import com.tjx.lew00305.slimstore.transaction.TransactionRepository;
import com.tjx.lew00305.slimstore.transaction.TransactionTender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    
    private final TransactionRepository txnRepo;
    private final LocationService locationService;

    private List<TransactionAudit> getTransactionAudit(
        List<Transaction> data
    ) {
        DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormater = DateTimeFormatter.ofPattern("HH:mm:ss");
        ArrayList<TransactionAudit> report = new ArrayList<TransactionAudit>();
        for (Transaction row : data) {
            TransactionAudit line = new TransactionAudit();
            line.setStore(row.getStore().getNumber());
            line.setStoreName(row.getStore().getName());
            line.setRegister(row.getRegister().getNumber());
            line.setDate(row.getDate().toLocalDateTime().format(dateFormater));
            line.setTime(row.getDate().toLocalDateTime().format(timeFormater));
            line.setTransaction(row.getNumber());
            line.setTransactionTotal(row.getTotal());
            line.setLineTotal(row.getLineTotal());
            line.setTenderTotal(row.getTenderTotal());
            if ((line.getTransactionTotal().compareTo(line.getLineTotal()) == 0) &&
                (line.getTransactionTotal().compareTo(line.getTenderTotal()) == 0)) {
                line.setCheck("");
            } else {
                line.setCheck("X");
            }
            report.add(line);
        }
        return report;
    }
    
    private List<TransactionFlat> getTransactionFlat(
        List<Transaction> data
    ) {
        ArrayList<TransactionFlat> report = new ArrayList<TransactionFlat>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        for (Transaction txnRow : data) {
            TransactionFlat line = new TransactionFlat();
            line.setStore(txnRow.getStore().getNumber());
            line.setStoreName(txnRow.getStore().getName());
            line.setDate(dateFormatter.format(txnRow.getDate()));
            line.setTime(timeFormatter.format(txnRow.getDate()));
            line.setRegister(txnRow.getRegister().getNumber());
            line.setTransaction(txnRow.getNumber());
            line.setUser(txnRow.getUser().getName());
            line.setCurrency(txnRow.getCurrencyCode());
            line.setTotal(txnRow.getTotal());
            report.add(line);
        }
        return report;
    }
    
    private List<TransactionLineFlat> getTransactionLineFlat(
        List<Transaction> data
    ) {
        ArrayList<TransactionLineFlat> report = new ArrayList<TransactionLineFlat>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        for (Transaction txnRow : data) {
            for (TransactionLine lineRow : txnRow.getLines()) {
                TransactionLineFlat line = new TransactionLineFlat();
                line.setStore(txnRow.getStore().getNumber());
                line.setStoreName(txnRow.getStore().getName());
                line.setDate(dateFormatter.format(txnRow.getDate()));
                line.setTime(timeFormatter.format(txnRow.getDate()));
                line.setRegister(txnRow.getRegister().getNumber());
                line.setTransaction(txnRow.getNumber());
                line.setNumber(lineRow.getNumber());
                line.setProduct(lineRow.getProductCode());
                line.setType(lineRow.getType());
                line.setQuantity(lineRow.getQuantity());
                line.setLineValue(lineRow.getLineValue());
                line.setReturnedQuantity(lineRow.getReturnedQuantity());
                report.add(line);
            }
        }
        return report;
    }
    
    public Iterable<Transaction> getTransactionReport() {
        return txnRepo.findAll();
    }
    
    private List<TransactionTenderAggregation> getTransactionTenderAggregation(
        List<TransactionTenderAggregationInterface> data
    ) {
        ArrayList<TransactionTenderAggregation> report = new ArrayList<TransactionTenderAggregation>();
        for (TransactionTenderAggregationInterface row : data) {
            TransactionTenderAggregation line = new TransactionTenderAggregation();
            line.setStore(row.getStore());
            line.setStoreName(row.getStoreName());
            line.setRegister(row.getRegister());
            line.setDate(row.getDate());
            line.setType(row.getType());
            line.setValue(row.getValue());
            report.add(line);
        }
        return report;
    }
    
    private List<TransactionTenderFlat> getTransactionTenderFlat(
        List<Transaction> data
    ) {
        ArrayList<TransactionTenderFlat> report = new ArrayList<TransactionTenderFlat>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        for (Transaction txnRow : data) {
            for (TransactionTender tenderRow : txnRow.getTenders()) {
                TransactionTenderFlat line = new TransactionTenderFlat();
                line.setStore(txnRow.getStore().getNumber());
                line.setStoreName(txnRow.getStore().getName());
                line.setDate(dateFormatter.format(txnRow.getDate()));
                line.setTime(timeFormatter.format(txnRow.getDate()));
                line.setRegister(txnRow.getRegister().getNumber());
                line.setTransaction(txnRow.getNumber());
                line.setNumber(tenderRow.getNumber());
                line.setType(tenderRow.getType().toString());
                line.setValue(tenderRow.getValue());
                line.setReference(tenderRow.getReference());
                report.add(line);
            }
        }
        return report;
    }
    
    @SuppressWarnings("rawtypes")
    public List runReport(
        String scope,
        String name,
        Integer days
    ) {
        String reportName = scope + " " + name;
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).minusDays(days - 1);
        LocalDateTime stop = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        Store store = locationService.getStore();
        String storeNumber = "" + store.getNumber();
        StoreRegister register = locationService.getStoreRegister();
        String regNumber = "" + register.getNumber();
        switch (reportName) {
            case "Register Transactions":
                return getTransactionFlat(txnRepo.findByRegisterAndDateBetweenOrderByDateAsc(register, start, stop));
            case "Store Transactions":
                return getTransactionFlat(txnRepo.findByStoreAndDateBetweenOrderByDateAsc(store, start, stop));
            case "Company Transactions":
                return getTransactionFlat(txnRepo.findByDateBetweenOrderByDateAsc(start, stop));
            case "Register Transaction Lines":
                return getTransactionLineFlat(txnRepo.findByRegisterAndDateBetweenOrderByDateAsc(register, start, stop));
            case "Store Transaction Lines":
                return getTransactionLineFlat(txnRepo.findByStoreAndDateBetweenOrderByDateAsc(store, start, stop));
            case "Company Transaction Lines":
                return getTransactionLineFlat(txnRepo.findByDateBetweenOrderByDateAsc(start, stop));
            case "Register Transaction Tenders":
                return getTransactionTenderFlat(txnRepo.findByRegisterAndDateBetweenOrderByDateAsc(register, start, stop));
            case "Store Transaction Tenders":
                return getTransactionTenderFlat(txnRepo.findByStoreAndDateBetweenOrderByDateAsc(store, start, stop));
            case "Company Transaction Tenders":
                return getTransactionTenderFlat(txnRepo.findByDateBetweenOrderByDateAsc(start, stop));
            case "Register Aggregate Tenders":
                return getTransactionTenderAggregation(txnRepo.aggregateTenders(regNumber, storeNumber, start, stop));
            case "Store Aggregate Tenders":
                return getTransactionTenderAggregation(txnRepo.aggregateTenders("%", storeNumber, start, stop));
            case "Company Aggregate Tenders":
                return getTransactionTenderAggregation(txnRepo.aggregateTenders("%", "%", start, stop));
            case "Register Audit Transactions":
                return getTransactionAudit(txnRepo.findByRegisterAndDateBetweenOrderByDateAsc(register, start, stop));
            case "Store Audit Transactions":
                return getTransactionAudit(txnRepo.findByStoreAndDateBetweenOrderByDateAsc(store, start, stop));
            case "Company Audit Transactions":
                return getTransactionAudit(txnRepo.findByDateBetweenOrderByDateAsc(start, stop));
        }
        return null;
    }
    
    @SuppressWarnings("rawtypes")
    public List runReportByForm(
        Form requestForm
    ) {
        return runReport(requestForm.getValueByKey("scope"), requestForm.getValueByKey("report"), requestForm.getIntegerValueByKey("days"));
    }
    
}
