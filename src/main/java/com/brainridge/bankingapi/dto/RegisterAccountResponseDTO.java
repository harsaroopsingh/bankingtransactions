package com.brainridge.bankingapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class RegisterAccountResponseDTO {

    Long id;

    String username;

    String currency;

    BigDecimal balance;

}
