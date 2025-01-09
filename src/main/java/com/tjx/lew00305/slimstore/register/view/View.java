package com.tjx.lew00305.slimstore.register.view;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tjx.lew00305.slimstore.register.form.Form;

import lombok.Data;

@Data
public class View {
    
    public enum ViewName {
        ABOUT,
        COMPLETE,
        GIFTCARD,
        HOME,
        LOGIN,
        PAGE_NOT_FOUND,
        REGISTER_CHANGE,
        REPORTS,
        RETURN,
        RETURN_MANUAL,
        RETURN_VIEW,
        SALE,
        SEARCH,
        STORE_SETUP,
        SYSTEM,
        TENDER,
        TENDER_CARD,
        TENDER_CASH,
        TENDER_DISCOUNT,
        TENDER_GIFTCARD,
        TENDER_VOUCHER,
        USER_EDIT,
        USER_LIST,
        USER_NEW,
        VOID,
        VOID_POST,
    }
    
    private ViewName name;
    private String title = "";
    private String message = "";
    private Form form = new Form();
    private ViewFunctionButton[] functionButtons = new ViewFunctionButton[0];
    @JsonIgnore
    private Locale locale;
    @JsonIgnore
    private String cacheKey;

}
