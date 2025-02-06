package cloud.matthews.slimstore.admin;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.matthews.slimstore.transaction.Transaction;
import cloud.matthews.slimstore.transaction.report.TransactionReportService;
import cloud.matthews.slimstore.translation.LanguageTranslationDTO;
import cloud.matthews.slimstore.translation.TranslationService;
import cloud.matthews.slimstore.user.User;
import cloud.matthews.slimstore.user.UserService;

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
