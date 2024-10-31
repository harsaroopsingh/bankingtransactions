package com.brainridge.bankingapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class TransactionHistoryResponseDTO {

    String currency;
    
    BigDecimal recentTransaction;

    BigDecimal accountBalanceAfterTransaction;

    boolean incoming;

    boolean outgoing;

    String vendorUsername;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timeStamp;
}
