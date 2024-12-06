package com.tjx.lew00305.slimstore.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjx.lew00305.slimstore.model.entity.Transaction;
import com.tjx.lew00305.slimstore.model.entity.User;
import com.tjx.lew00305.slimstore.service.TransactionReportService;
import com.tjx.lew00305.slimstore.service.TranslationService;
import com.tjx.lew00305.slimstore.service.UserService;

@RestController
public class ApiController {

    private UserService userService;
    private TransactionReportService transactionReportService;
    private TranslationService translationService;

    public ApiController(
        UserService userService,
        TransactionReportService transactionReportService,
        TranslationService translationService
    ) {
        this.userService = userService;
        this.transactionReportService = transactionReportService;
        this.translationService = translationService;
    }
    
    @GetMapping(path = "/api/translations/generate")
    public String generateTranslations() {
        if (!userService.isUserAdmin()) {
            return null;
        }
        List<String> translations = translationService.generateTranslations();
        translations.add(0, "<pre>");
        return translations.stream().collect(Collectors.joining("\n"));
    }

    @GetMapping(path = "/api/transactions/all")
    public Iterable<Transaction> getAllTransactions() {
        if (!userService.isUserAdmin()) {
            return null;
        }
        return transactionReportService.getTransactionReport();
    }

    @GetMapping(path = "/api/users/all")
    public Iterable<User> getAllUsers() {
        if (!userService.isUserAdmin()) {
            return null;
        }
        return userService.getAllUsers();
    }

}
