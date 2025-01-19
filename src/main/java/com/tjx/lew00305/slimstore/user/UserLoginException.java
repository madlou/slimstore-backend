package com.tjx.lew00305.slimstore.user;

@SuppressWarnings("serial")
public class UserLoginException extends Exception {
    public UserLoginException(
        String errorMessage
    ) {
        super(errorMessage);
    }
}
