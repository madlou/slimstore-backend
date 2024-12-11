package com.tjx.lew00305.slimstore.model.common;

import lombok.Data;

@Data
public class ErrorTranslation {

    String error;
    String locationInvalid;
    String locationInvalidStore;
    String locationInvalidRegister;
    String locationEnterRegister;
    String locationSetupRequired;
    String tenderValueNotAllowed;
    String tenderRefundTooHigh;
    String userCreationError;
    String userDemoEditError;
    String userDuplicateEntry;
    String userUnableToSave;
    String securityInvalidLogin;
    String securityUserNotFound;
    String securityUserNoStore;
    String securityUserWrongStore;

}
