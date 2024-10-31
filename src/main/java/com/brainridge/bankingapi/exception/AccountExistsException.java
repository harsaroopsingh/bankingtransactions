package com.brainridge.bankingapi.exception;

public class AccountExistsException extends RuntimeException {
    public AccountExistsException(String errors) {
        super(errors);
    }
}