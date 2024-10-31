package com.brainridge.bankingapi.exception;


public class TransactionHistoryException extends RuntimeException {
    public TransactionHistoryException(String errors) {
        super(errors);
    }
}
