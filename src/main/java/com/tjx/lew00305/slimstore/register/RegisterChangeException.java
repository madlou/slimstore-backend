package com.tjx.lew00305.slimstore.register;

@SuppressWarnings("serial")
public class RegisterChangeException extends Exception {
    public RegisterChangeException(
        String errorMessage
    ) {
        super(errorMessage);
    }
}
