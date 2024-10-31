package com.brainridge.bankingapi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {

    private String message;
    private int status;
    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}