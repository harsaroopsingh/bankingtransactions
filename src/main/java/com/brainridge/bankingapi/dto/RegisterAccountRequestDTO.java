package com.brainridge.bankingapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RegisterAccountRequestDTO {
    @NotBlank
    @Email
    private String username;

    @NotBlank
    private String currency;

    @DecimalMin(value = "0")
    private BigDecimal balance;
}
