package cloud.matthews.slimstore.translation;

import java.util.Locale;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cloud.matthews.slimstore.transaction.Transaction;
import cloud.matthews.slimstore.transaction.report.TransactionReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TranslationController {

    private final TransactionReportService transactionReportService;
    private final TranslationService translationService;
    private final UserInterfaceService userInterfaceService;
    
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

    @GetMapping(path = "/api/public/languages")
    public Language[] getLanguages() {
        return Language.values();
    }

    @GetMapping(path = "/api/public/translations/{languageCode}")
    public UserInterfaceTranslationDTO getUiTranslations(
        @PathVariable("languageCode")
        String languageCode
    ) {
        return userInterfaceService.getUserInterfaceTranslations(Locale.of(languageCode));
    }

    @GetMapping(path = "/api/transactions/all")
    public Iterable<Transaction> getAllTransactions() {
        return transactionReportService.getTransactionReport();
    }

}
