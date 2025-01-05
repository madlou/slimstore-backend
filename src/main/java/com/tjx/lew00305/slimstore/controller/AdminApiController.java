package com.tjx.lew00305.slimstore.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjx.lew00305.slimstore.dto.LanguageTranslationDTO;
import com.tjx.lew00305.slimstore.model.entity.Transaction;
import com.tjx.lew00305.slimstore.model.entity.User;
import com.tjx.lew00305.slimstore.service.TransactionReportService;
import com.tjx.lew00305.slimstore.service.TranslationService;
import com.tjx.lew00305.slimstore.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminApiController {
    
    private final UserService userService;
    private final TransactionReportService transactionReportService;
    private final TranslationService translationService;

    @GetMapping(path = "/api/translations/generate")
    public List<LanguageTranslationDTO> generateTranslations() {
        List<LanguageTranslationDTO> translations = translationService.getTranslations();
        return translations;
    }
    
    @GetMapping(path = "/api/translations/missing")
    public String generateTranslationsMissing() {
        List<String> translations = translationService.getMissingTranslations();
        translations.add(0, "<pre>");
        return String.join("\n", translations);
    }
    
    @GetMapping(path = "/api/transactions/all")
    public Iterable<Transaction> getAllTransactions() {
        return transactionReportService.getTransactionReport();
    }
    
    @GetMapping(path = "/api/users/all")
    public Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
}
