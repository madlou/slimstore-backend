package com.tjx.lew00305.slimstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.context.annotation.RequestScope;

import com.tjx.lew00305.slimstore.model.View;

@Configuration
@ImportResource({
    "classpath:view/404.xml",
    "classpath:view/giftcard.xml",
    "classpath:view/home.xml",
    "classpath:view/login.xml",
    "classpath:view/reports.xml",
    "classpath:view/return.xml",
    "classpath:view/return-manual.xml",
    "classpath:view/sale.xml",
    "classpath:view/search.xml",
    "classpath:view/system.xml",
    "classpath:view/tender.xml",
    "classpath:view/tender/card.xml",
    "classpath:view/tender/cash.xml",
    "classpath:view/tender/discount.xml",
    "classpath:view/tender/giftcard.xml",
    "classpath:view/tender/voucher.xml",
    "classpath:view/user-new.xml",
    "classpath:view/void.xml",
    "classpath:view/void-post.xml",
})
public class ViewConfig {
        
    @Autowired    
    private View[] views;
        
    public View[] getViews() {
        return views;
    }
}
