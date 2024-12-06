package com.tjx.lew00305.slimstore.service;

import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.dto.UserInterfaceTranslationDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserInterfaceService {

    private HttpServletRequest request;
    private TranslationService translationService;

    public UserInterfaceService(
        HttpServletRequest request,
        TranslationService translationService
    ) {
        this.request = request;
        this.translationService = translationService;
    }
    
    public UserInterfaceTranslationDTO getUserInterfaceTranslations() {
        return translationService.getUserInterfaceTranslations(request.getLocale());
    }

}
