package com.brainridge.bankingapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionHistoryRequestDTO {
    @NotNull
    private Long accountId;
}
