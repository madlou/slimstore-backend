package com.tjx.lew00305.slimstore.service;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.config.ViewConfig;
import com.tjx.lew00305.slimstore.dto.UserInterfaceTranslationDTO;
import com.tjx.lew00305.slimstore.model.common.FormElement;
import com.tjx.lew00305.slimstore.model.common.FunctionButton;
import com.tjx.lew00305.slimstore.model.common.View;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TranslationService {
    
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ViewConfig viewConfig;
    @Autowired
    private HttpServletRequest request;
    
    private List<String> uiTranslationList = new ArrayList<String>();
    private String translationFile = "messages.properties";
    
    public TranslationService() {
        Method[] methods = new UserInterfaceTranslationDTO().getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().substring(0, 3).equals("get") &&
                !method.getName().equals("getClass")) {
                uiTranslationList.add("ui." + camelToSnake(method.getName().substring(3)));
            }
        }
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
    
    public List<String> generateTranslations() {
        List<String> output = new ArrayList<String>();
        for (String line : uiTranslationList) {
            output.add(line + "=" + messageSource.getMessage(line, null, null, Locale.ENGLISH));
        }
        for (View view : viewConfig.getAll()) {
            String crumb = "view." + view.getName().toString().toLowerCase() + ".";
            output.add(crumb + "title=" + view.getTitle());
            output.add(crumb + "message=" + view.getMessage());
            for (FormElement element : view.getForm().getElements()) {
                output.add(crumb + "form." + element.getKey() + "=" + element.getLabel());
                if (element.getOptions() != null) {
                    for (String option : element.getOptions()) {
                        String optionName = option.toLowerCase().replace(" ", "_");
                        output.add(crumb + "form." + element.getKey() + "." + optionName + "=" + option);
                    }
                }
            }
            for (FunctionButton button : view.getFunctionButtons()) {
                output.add(crumb + "button." + button.getPosition() + "=" + button.getLabel());
            }
        }
        Collections.sort(output);
        saveToFile(output);
        return output;
    }
    
    public String translate(
        String code
    ) {
        return messageSource.getMessage(code, null, null, request.getLocale());
    }
    
    private void saveToFile(
        List<String> lines
    ) {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(translationFile), StandardCharsets.UTF_8)) {
            for (String line : lines) {
                writer.write((line +
                    System.getProperty("line.separator")));
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
            text = text.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(text.charAt(text.indexOf("_") +
                1))));
        }
        return text;
    }
    
    private String camelToSnake(
        String text
    ) {
        return text.replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase();
    }
}
