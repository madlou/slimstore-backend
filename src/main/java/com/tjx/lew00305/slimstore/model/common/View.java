package com.tjx.lew00305.slimstore.model.common;


import lombok.Data;

@Data
public class View {

    public enum ViewName {
        COMPLETE,
        GIFTCARD,
        HOME,
        LOGIN,
        PAGE_NOT_FOUND,
        REGISTER_CHANGE,
        REPORTS,
        RETURN,
        RETURN_MANUAL,
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
    private String formProcess = "";
    private ViewName formSuccess;
    private FormElement[] formElements = new FormElement[0];
    private FunctionButton[] functionButtons = new FunctionButton[0];
    
}
