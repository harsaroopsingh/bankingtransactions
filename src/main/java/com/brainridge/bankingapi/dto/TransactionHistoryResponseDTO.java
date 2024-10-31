package com.brainridge.bankingapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionHistoryResponseDTO {
    Long vendorAccountId;

    String currency;
    
    BigDecimal recentTransaction;

    BigDecimal balance;

    boolean incoming;

    boolean outgoing;

    String vendorUsername;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timeStamp;
}
