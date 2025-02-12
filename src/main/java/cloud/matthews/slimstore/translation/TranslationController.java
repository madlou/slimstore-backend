package cloud.matthews.slimstore.translation;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.matthews.slimstore.transaction.Transaction;
import cloud.matthews.slimstore.transaction.report.TransactionReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TranslationController {

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

}
