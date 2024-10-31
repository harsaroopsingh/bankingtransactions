package com.brainridge.bankingapi.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private BigDecimal exchangeRate; // Rate to a base currency (CAD)

    public Currency(String code, BigDecimal exchangeRate) {
        this.code = code;
        this.exchangeRate = exchangeRate;
    }

}