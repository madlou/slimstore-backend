package cloud.matthews.slimstore.translation;

import java.util.Locale;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInterfaceService {

    private final HttpServletRequest request;
    private final TranslationService translationService;
    
    public UserInterfaceTranslationDTO getUserInterfaceTranslations() {
        return translationService.getUserInterfaceTranslations(request.getLocale());
    }

    public UserInterfaceTranslationDTO getUserInterfaceTranslations(
        Locale locale
    ) {
        return translationService.getUserInterfaceTranslations(locale);
    }
}
