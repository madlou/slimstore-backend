package com.tjx.lew00305.slimstore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
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
    private HttpServletRequest request;
    @Autowired
    private ViewConfig viewConfig;
    
    List<String> fixedLines = new ArrayList<String>();;
    
    public TranslationService() {
        fixedLines.add("ui.logo");
        fixedLines.add("ui.header");
        fixedLines.add("ui.keyboard");
        fixedLines.add("ui.store");
        fixedLines.add("ui.register");
        fixedLines.add("ui.user");
        fixedLines.add("ui.administrator");
        fixedLines.add("ui.transaction");
        fixedLines.add("ui.status");
        fixedLines.add("ui.password");
        fixedLines.add("ui.role");
        fixedLines.add("ui.store_name");
        fixedLines.add("ui.date");
        fixedLines.add("ui.time");
        fixedLines.add("ui.number");
        fixedLines.add("ui.product");
        fixedLines.add("ui.type");
        fixedLines.add("ui.quantity");
        fixedLines.add("ui.line_value");
        fixedLines.add("ui.returned_quantity");
        fixedLines.add("ui.total");
        fixedLines.add("ui.reference");
        fixedLines.add("ui.value");
        fixedLines.add("ui.transaction_total");
        fixedLines.add("ui.line_total");
        fixedLines.add("ui.tender_total");
        fixedLines.add("ui.check");
        fixedLines.add("ui.devmessage1");
        fixedLines.add("ui.devmessage2");
        fixedLines.add("ui.devmessage3");
    }
    
    public UserInterfaceTranslationDTO getUserInterfaceTranslations() {
        Locale locale = request.getLocale();
        UserInterfaceTranslationDTO uiTranslations = new UserInterfaceTranslationDTO();
        uiTranslations.setLogo(messageSource.getMessage("ui.logo", null, null, locale));
        uiTranslations.setHeader(messageSource.getMessage("ui.header", null, null, locale));
        uiTranslations.setKeyboard(messageSource.getMessage("ui.keyboard", null, null, locale));
        uiTranslations.setStore(messageSource.getMessage("ui.store", null, null, locale));
        uiTranslations.setRegister(messageSource.getMessage("ui.register", null, null, locale));
        uiTranslations.setUser(messageSource.getMessage("ui.user", null, null, locale));
        uiTranslations.setAdministrator(messageSource.getMessage("ui.administrator", null, null, locale));
        uiTranslations.setTransaction(messageSource.getMessage("ui.transaction", null, null, locale));
        uiTranslations.setStatus(messageSource.getMessage("ui.status", null, null, locale));
        uiTranslations.setPassword(messageSource.getMessage("ui.password", null, null, locale));
        uiTranslations.setRole(messageSource.getMessage("ui.role", null, null, locale));
        uiTranslations.setStoreName(messageSource.getMessage("ui.store_name", null, null, locale));
        uiTranslations.setDate(messageSource.getMessage("ui.date", null, null, locale));
        uiTranslations.setTime(messageSource.getMessage("ui.time", null, null, locale));
        uiTranslations.setNumber(messageSource.getMessage("ui.number", null, null, locale));
        uiTranslations.setProduct(messageSource.getMessage("ui.product", null, null, locale));
        uiTranslations.setType(messageSource.getMessage("ui.type", null, null, locale));
        uiTranslations.setQuantity(messageSource.getMessage("ui.quantity", null, null, locale));
        uiTranslations.setLineValue(messageSource.getMessage("ui.line_value", null, null, locale));
        uiTranslations.setReturnedQuantity(messageSource.getMessage("ui.returned_quantity", null, null, locale));
        uiTranslations.setTotal(messageSource.getMessage("ui.total", null, null, locale));
        uiTranslations.setReference(messageSource.getMessage("ui.reference", null, null, locale));
        uiTranslations.setValue(messageSource.getMessage("ui.value", null, null, locale));
        uiTranslations.setTransactionTotal(messageSource.getMessage("ui.transaction_total", null, null, locale));
        uiTranslations.setLineTotal(messageSource.getMessage("ui.line_total", null, null, locale));
        uiTranslations.setTenderTotal(messageSource.getMessage("ui.tender_total", null, null, locale));
        uiTranslations.setCheck(messageSource.getMessage("ui.check", null, null, locale));
        uiTranslations.setDevmessage1(messageSource.getMessage("ui.devmessage1", null, null, locale));
        uiTranslations.setDevmessage2(messageSource.getMessage("ui.devmessage2", null, null, locale));
        uiTranslations.setDevmessage3(messageSource.getMessage("ui.devmessage3", null, null, locale));
        return uiTranslations;
    }

    public View translateView(View view) {
        Locale locale = request.getLocale();
        String crumb = "view." + view.getName().toString().toLowerCase() + ".";
        view.setLocale(locale);
        view.setTitle(messageSource.getMessage(
            crumb + "title", 
            null, 
            view.getTitle(), 
            locale
        ));
        view.setMessage(messageSource.getMessage(
            crumb + "message", 
            null, 
            view.getMessage(), 
            locale
        ));
        for(FormElement element : view.getForm().getElements()) {
            element.setLabel(messageSource.getMessage(
                crumb + "form." + element.getKey(),
                null, 
                element.getLabel(), 
                locale
            ));
            if(element.getOptions() != null) {
                Integer optionIndex = 0;
                String[] options = element.getOptions();
                String[] translatedOptions = new String[options.length];
                for(String option : options) {
                    String optionName = option.toLowerCase().replace(" ", "_");
                    translatedOptions[optionIndex] = option + "|" + messageSource.getMessage(
                        crumb + "form." + element.getKey() + "." + optionName,
                        null,
                        option, 
                        locale
                    );
                    optionIndex++;
                }
                element.setOptions(translatedOptions);
            }
        }
        for(FunctionButton button : view.getFunctionButtons()) {
            button.setLabel(messageSource.getMessage(
                crumb + "button." + button.getPosition(),
                null, 
                button.getLabel(), 
                locale
            ));
        }
        return view;
    }
    
    public List<String> generateTranslations() {
        List<String> output = new ArrayList<String>();
        output.add("<pre>");
        for(String line : fixedLines) {
            output.add(
                line + "=" + messageSource.getMessage(line, null, null, Locale.ENGLISH)
            );
        }
        for(View view : viewConfig.getAll()) {
            String crumb = "view." + view.getName().toString().toLowerCase() + ".";
            output.add(crumb + "title=" + view.getTitle());
            output.add(crumb + "message=" + view.getMessage());
            for(FormElement element : view.getForm().getElements()) {
                output.add(crumb + "form." + element.getKey() + "=" + element.getLabel());
                if(element.getOptions() != null) {
                    for(String option : element.getOptions()) {
                        String optionName = option.toLowerCase().replace(" ", "_");
                        output.add(crumb + "form." + element.getKey() + "." + optionName+ "=" + option);
                    }
                }
            }
            for(FunctionButton button : view.getFunctionButtons()) {
                output.add(crumb + "button." + button.getPosition() + "=" + button.getLabel());
            }
        }
        return output;
    }
    
}
