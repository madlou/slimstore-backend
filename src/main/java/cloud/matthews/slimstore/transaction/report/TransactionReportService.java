package cloud.matthews.slimstore.transaction.report;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cloud.matthews.slimstore.register.Register;
import cloud.matthews.slimstore.register.RegisterService;
import cloud.matthews.slimstore.register.form.Form;
import cloud.matthews.slimstore.store.Store;
import cloud.matthews.slimstore.store.StoreService;
import cloud.matthews.slimstore.transaction.Transaction;
import cloud.matthews.slimstore.transaction.TransactionLine;
import cloud.matthews.slimstore.transaction.TransactionRepository;
import cloud.matthews.slimstore.transaction.TransactionTender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionReportService {

    private final TransactionRepository txnRepo;
    private final RegisterService registerService;
    private final StoreService storeService;
    private final ObjectMapper mapper;

    private ArrayList<TransactionAudit> getTransactionAudit(
        List<Transaction> data
    ) throws JsonProcessingException {
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

    private ArrayList<TransactionFlat> getTransactionFlat(
        List<Transaction> data
    ) throws JsonProcessingException {
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
            line.setReview(txnRow.getReviewScore());
            report.add(line);
        }
        return report;
    }

    private ArrayList<TransactionLineFlat> getTransactionLineFlat(
        List<Transaction> data
    ) throws JsonProcessingException {
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

    private ArrayList<TransactionTenderAggregation> getTransactionTenderAggregation(
        List<TransactionTenderAggregationInterface> data
    ) throws JsonProcessingException {
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

    private ArrayList<TransactionTenderFlat> getTransactionTenderFlat(
        List<Transaction> data
    ) throws JsonProcessingException {
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

    public String runReport(
        String scope,
        String name,
        Integer days
    ) throws JsonProcessingException {
        String reportName = scope + " " + name;
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).minusDays(days - 1);
        LocalDateTime stop = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        Store store = storeService.getStoreReference();
        String storeNumber = "" + store.getNumber();
        Register register = registerService.getRegisterReference();
        String regNumber = "" + register.getNumber();
        ArrayList<TransactionAudit> transactionAudits;
        ArrayList<TransactionFlat> transactionFlats;
        ArrayList<TransactionLineFlat> transactionLineFlats;
        ArrayList<TransactionTenderAggregation> transactionTenderAggregations;
        ArrayList<TransactionTenderFlat> transactionTenderFlats;
        switch (reportName) {
            case "Register Transactions":
                transactionFlats = getTransactionFlat(txnRepo.findByRegisterAndDateBetweenOrderByDateAsc(register, start, stop));
                return mapper.writeValueAsString(transactionFlats);
            case "Store Transactions":
                transactionFlats = getTransactionFlat(txnRepo.findByStoreAndDateBetweenOrderByDateAsc(store, start, stop));
                return mapper.writeValueAsString(transactionFlats);
            case "Company Transactions":
                transactionFlats = getTransactionFlat(txnRepo.findByDateBetweenOrderByDateAsc(start, stop));
                return mapper.writeValueAsString(transactionFlats);
            case "Register Transaction Lines":
                transactionLineFlats = getTransactionLineFlat(txnRepo.findByRegisterAndDateBetweenOrderByDateAsc(register, start, stop));
                return mapper.writeValueAsString(transactionLineFlats);
            case "Store Transaction Lines":
                transactionLineFlats = getTransactionLineFlat(txnRepo.findByStoreAndDateBetweenOrderByDateAsc(store, start, stop));
                return mapper.writeValueAsString(transactionLineFlats);
            case "Company Transaction Lines":
                transactionLineFlats = getTransactionLineFlat(txnRepo.findByDateBetweenOrderByDateAsc(start, stop));
                return mapper.writeValueAsString(transactionLineFlats);
            case "Register Transaction Tenders":
                transactionTenderFlats = getTransactionTenderFlat(txnRepo.findByRegisterAndDateBetweenOrderByDateAsc(register, start, stop));
                return mapper.writeValueAsString(transactionTenderFlats);
            case "Store Transaction Tenders":
                transactionTenderFlats = getTransactionTenderFlat(txnRepo.findByStoreAndDateBetweenOrderByDateAsc(store, start, stop));
                return mapper.writeValueAsString(transactionTenderFlats);
            case "Company Transaction Tenders":
                transactionTenderFlats = getTransactionTenderFlat(txnRepo.findByDateBetweenOrderByDateAsc(start, stop));
                return mapper.writeValueAsString(transactionTenderFlats);
            case "Register Aggregate Tenders":
                transactionTenderAggregations = getTransactionTenderAggregation(txnRepo.aggregateTenders(regNumber, storeNumber, start, stop));
                return mapper.writeValueAsString(transactionTenderAggregations);
            case "Store Aggregate Tenders":
                transactionTenderAggregations = getTransactionTenderAggregation(txnRepo.aggregateTenders("%", storeNumber, start, stop));
                return mapper.writeValueAsString(transactionTenderAggregations);
            case "Company Aggregate Tenders":
                transactionTenderAggregations = getTransactionTenderAggregation(txnRepo.aggregateTenders("%", "%", start, stop));
                return mapper.writeValueAsString(transactionTenderAggregations);
            case "Register Audit Transactions":
                transactionAudits = getTransactionAudit(txnRepo.findByRegisterAndDateBetweenOrderByDateAsc(register, start, stop));
                return mapper.writeValueAsString(transactionAudits);
            case "Store Audit Transactions":
                transactionAudits = getTransactionAudit(txnRepo.findByStoreAndDateBetweenOrderByDateAsc(store, start, stop));
                return mapper.writeValueAsString(transactionAudits);
            case "Company Audit Transactions":
                transactionAudits = getTransactionAudit(txnRepo.findByDateBetweenOrderByDateAsc(start, stop));
                return mapper.writeValueAsString(transactionAudits);
            default:
                return "[]";
        }
    }

    public String runReportByForm(
        Form requestForm
    ) throws JsonProcessingException {
        return runReport(requestForm.getValueByKey("scope"), requestForm.getValueByKey("report"), requestForm.getIntegerValueByKey("days"));
    }

}
