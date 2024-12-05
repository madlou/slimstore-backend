package com.tjx.lew00305.slimstore.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjx.lew00305.slimstore.model.entity.Transaction;
import com.tjx.lew00305.slimstore.model.entity.User;
import com.tjx.lew00305.slimstore.service.UserService;
import com.tjx.lew00305.slimstore.service.TransactionReportService;
import com.tjx.lew00305.slimstore.service.TranslationService;

@RestController
public class ApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private TransactionReportService transactionReportService;
    @Autowired
    private TranslationService translationService;

    @GetMapping(path = "/api/transactions/all")
    public Iterable<Transaction> getAllTransactions(){
        if(!userService.isUserAdmin()) {
            return null;
        }
        return transactionReportService.getTransactionReport();
    }

    @GetMapping(path = "/api/users/all")
    public Iterable<User> getAllUsers(){
        if(!userService.isUserAdmin()) {
            return null;
        }
        return userService.getAllUsers();
    }

    @GetMapping(path = "/api/translations/generate")
    public String generateTranslations(){
        if(!userService.isUserAdmin()) {
            return null;
        }
        List<String> translations = translationService.generateTranslations();
        translations.add(0, "<pre>");
        return translations.stream().collect(Collectors.joining("\n"));
    }

}
