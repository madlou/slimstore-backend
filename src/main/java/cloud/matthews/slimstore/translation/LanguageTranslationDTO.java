package cloud.matthews.slimstore.translation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.Data;

@Data
public class LanguageTranslationDTO {
    
    private Boolean isBase = false;
    private String language;
    private Locale locale;
    private List<String> translations = new ArrayList<String>();
    
}
