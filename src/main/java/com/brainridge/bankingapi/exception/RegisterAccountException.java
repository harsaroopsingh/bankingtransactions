package com.brainridge.bankingapi.exception;

public class RegisterAccountException extends RuntimeException{
    public RegisterAccountException(String errors) {
        super(errors);
    }
}