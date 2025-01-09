package com.tjx.lew00305.slimstore.translation;

import lombok.Data;

@Data
public class ErrorTranslationDTO {
    
    String error;
    String locationInvalid;
    String locationInvalidStore;
    String locationInvalidRegister;
    String locationEnterRegister;
    String locationSetupRequired;
    String tenderValueNotAllowed;
    String tenderRefundTooHigh;
    String transactionNotFound;
    String userCreationError;
    String userDemoEditError;
    String userDuplicateEntry;
    String userUnableToSave;
    String securityInvalidLogin;
    String securityUserNotFound;
    String securityUserNoStore;
    String securityUserWrongStore;
    
}
