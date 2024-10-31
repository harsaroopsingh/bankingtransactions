package com.brainridge.bankingapi.exception;

public class InsufficentBalanceException extends RuntimeException {
    public InsufficentBalanceException(String errors) {
        super(errors);
    }
}

