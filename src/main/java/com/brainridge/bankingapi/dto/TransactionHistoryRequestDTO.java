package com.brainridge.bankingapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TransactionHistoryRequestDTO {
    @NotNull
    private Long accountId;
}
