package com.brainridge.bankingapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransferFundsRequestDTO {

    @NotNull
    private Long issuerAccountId;

    @NotNull
    private Long recipientAccountId;

    @NotNull
    @DecimalMin(value = "1")
    private BigDecimal amount;
}
