package com.brainridge.bankingapi.exception;

public class SameAccountException extends RuntimeException {
    public SameAccountException(String errors) {
        super(errors);
    }
}

