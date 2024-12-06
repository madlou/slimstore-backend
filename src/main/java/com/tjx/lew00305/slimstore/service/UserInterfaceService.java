package com.tjx.lew00305.slimstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjx.lew00305.slimstore.dto.UserInterfaceTranslationDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserInterfaceService {
    
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TranslationService translationService;
    
    public UserInterfaceTranslationDTO getUserInterfaceTranslations() {
        return translationService.getUserInterfaceTranslations(request.getLocale());
    }
    
}
