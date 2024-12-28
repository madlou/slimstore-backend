package com.tjx.lew00305.slimstore.service;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.config.ViewConfig;
import com.tjx.lew00305.slimstore.dto.ErrorTranslationDTO;
import com.tjx.lew00305.slimstore.dto.LanguageTranslationDTO;
import com.tjx.lew00305.slimstore.dto.UserInterfaceTranslationDTO;
import com.tjx.lew00305.slimstore.enums.Language;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.common.FunctionButton;
import com.tjx.lew00305.slimstore.model.common.View;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TranslationService {

    private MessageSource messageSource;
    private ViewConfig viewConfig;

    private HttpServletRequest request;
    
    private List<String> errorTranslationList = new ArrayList<String>();
    private List<String> uiTranslationList = new ArrayList<String>();

    private String translationFile = "messages_%s.properties";

    public TranslationService(
        MessageSource messageSource,
        ViewConfig viewConfig,
        HttpServletRequest request
    ) {
        this.messageSource = messageSource;
        this.viewConfig = viewConfig;
        this.request = request;
        Method[] errorMethods = new ErrorTranslationDTO().getClass().getMethods();
        for (Method method : errorMethods) {
            if (method.getName().substring(0, 3).equals("get") &&
                !method.getName().equals("getClass")) {
                errorTranslationList.add("error." + camelToSnake(method.getName().substring(3)));
            }
        }
        Method[] uiMethods = new UserInterfaceTranslationDTO().getClass().getMethods();
        for (Method method : uiMethods) {
            if (method.getName().substring(0, 3).equals("get") &&
                !method.getName().equals("getClass")) {
                uiTranslationList.add("ui." + camelToSnake(method.getName().substring(3)));
            }
        }
    }

    private String camelToSnake(
        String text
    ) {
        return text.replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase();
    }

    public List<String> getMissingTranslations() {
        List<String> output = new ArrayList<String>();
        List<LanguageTranslationDTO> languages = getTranslations();
        HashMap<String, String> baseLookup = new HashMap<String, String>();
        for (LanguageTranslationDTO language : languages) {
            if (language.getIsBase()) {
                for (String line : language.getTranslations()) {
                    if (line.length() > 0) {
                        String[] split = line.split("=");
                        baseLookup.put(split[0], split.length > 1 ? split[1] : "null");
                    }
                }
            } else {
                output.add(language.getLanguage() + ":");
                for (String line : language.getTranslations()) {
                    String[] split = line.split("=");
                    if (split[1].equals("null") ||
                        (!language.getLanguage().equals("en") &&
                            split[1].equals(baseLookup.get(split[0])))) {
                        output.add(split[0] + "=" + baseLookup.get(split[0]));
                    }
                }
            }
        }
        return output;
    }
    
    public List<LanguageTranslationDTO> getTranslations() {
        List<LanguageTranslationDTO> languages = new ArrayList<LanguageTranslationDTO>();
        LanguageTranslationDTO base = new LanguageTranslationDTO();
        base.setLanguage("en");
        base.setLocale(Locale.of("en"));
        base.setIsBase(true);
        languages.add(base);
        for (Language language : Language.values()) {
            LanguageTranslationDTO languageTranslation = new LanguageTranslationDTO();
            languageTranslation.setLanguage(language.toString().toLowerCase());
            languageTranslation.setLocale(Locale.of(language.toString()));
            languages.add(languageTranslation);
        }
        String key;
        String value;
        for (String line : errorTranslationList) {
            key = line;
            for (LanguageTranslationDTO language : languages) {
                value = messageSource.getMessage(key, null, null, language.getLocale());
                language.getTranslations().add(key + "=" + value);
                
            }
        }
        for (String line : uiTranslationList) {
            key = line;
            for (LanguageTranslationDTO language : languages) {
                value = messageSource.getMessage(key, null, null, language.getLocale());
                language.getTranslations().add(key + "=" + value);
            }
        }
        for (View view : viewConfig.getAll()) {
            String crumb = "view." + view.getName().toString().toLowerCase() + ".";
            key = crumb + "title";
            for (LanguageTranslationDTO language : languages) {
                if (language.getIsBase() == true) {
                    value = view.getTitle();
                } else {
                    value = messageSource.getMessage(key, null, null, language.getLocale());
                }
                language.getTranslations().add(key + "=" + value);
            }
            key = crumb + "message";
            for (LanguageTranslationDTO language : languages) {
                if (language.getIsBase() == true) {
                    value = view.getMessage();
                } else {
                    value = messageSource.getMessage(key, null, null, language.getLocale());
                }
                language.getTranslations().add(key + "=" + value);
            }
            for (FormElement element : view.getForm().getElements()) {
                key = crumb + "form." + element.getKey();
                for (LanguageTranslationDTO language : languages) {
                    if (language.getIsBase() == true) {
                        value = element.getLabel();
                    } else {
                        value = messageSource.getMessage(key, null, null, language.getLocale());
                    }
                    language.getTranslations().add(key + "=" + value);
                }
                if (element.getOptions() != null) {
                    for (String option : element.getOptions()) {
                        String optionName = option.toLowerCase().replace(" ", "_");
                        key = crumb + "form." + element.getKey() + "." + optionName;
                        for (LanguageTranslationDTO language : languages) {
                            if (language.getIsBase() == true) {
                                value = option;
                            } else {
                                value = messageSource.getMessage(key, null, null, language.getLocale());
                            }
                            language.getTranslations().add(key + "=" + value);
                        }
                    }
                }
            }
            for (FunctionButton button : view.getFunctionButtons()) {
                key = crumb + "button." + button.getPosition();
                for (LanguageTranslationDTO language : languages) {
                    if (language.getIsBase() == true) {
                        value = button.getLabel();
                    } else {
                        value = messageSource.getMessage(key, null, null, language.getLocale());
                    }
                    language.getTranslations().add(key + "=" + value);
                }
            }
        }
        for (LanguageTranslationDTO language : languages) {
            Collections.sort(language.getTranslations());
            saveToFile(language.getLanguage(), language.getTranslations());
        }
        return languages;
    }

    @Cacheable("uiTranslations")
    public UserInterfaceTranslationDTO getUserInterfaceTranslations(
        Locale locale
    ) {
        UserInterfaceTranslationDTO uiTranslation = new UserInterfaceTranslationDTO();
        try {
            for (String item : uiTranslationList) {
                String methodName = snakeToCamel("set_" + item.substring(3));
                Method method = uiTranslation.getClass().getMethod(methodName, String.class);
                method.invoke(uiTranslation, messageSource.getMessage(item, null, null, locale));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return uiTranslation;
    }

    private void saveToFile(
        String language,
        List<String> lines
    ) {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(String.format(translationFile, language)), StandardCharsets.UTF_8)) {
            for (String line : lines) {
                writer.write((line + System.getProperty("line.separator")));
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("An error occurred saving file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String snakeToCamel(
        String text
    ) {
        while (text.contains("_")) {
            text = text.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(text.charAt(text.indexOf("_") + 1))));
        }
        return text;
    }

    public String translate(
        String code,
        Object... args
    ) {
        return messageSource.getMessage(code, null, code, request.getLocale()).formatted(args);
    }
    
    @Cacheable(value = "viewTranslations", key = "#view.cacheKey")
    public View translateView(
        View view
    ) {
        Locale locale = view.getLocale();
        String crumb = "view." + view.getName().toString().toLowerCase() + ".";
        view.setTitle(messageSource.getMessage(crumb + "title", null, view.getTitle(), locale));
        view.setMessage(messageSource.getMessage(crumb + "message", null, view.getMessage(), locale));
        for (FormElement element : view.getForm().getElements()) {
            element.setLabel(messageSource.getMessage(crumb + "form." + element.getKey(), null, element.getLabel(), locale));
            if (element.getOptions() != null) {
                Integer optionIndex = 0;
                String[] options = element.getOptions();
                String[] translatedOptions = new String[options.length];
                for (String option : options) {
                    String optionName = option.toLowerCase().replace(" ", "_");
                    translatedOptions[optionIndex] = option +
                        "|" +
                        messageSource.getMessage(crumb + "form." + element.getKey() + "." + optionName, null, option, locale);
                    optionIndex++;
                }
                element.setOptions(translatedOptions);
            }
        }
        for (FunctionButton button : view.getFunctionButtons()) {
            button.setLabel(messageSource.getMessage(crumb + "button." + button.getPosition(), null, button.getLabel(), locale));
        }
        return view;
    }
    
}
