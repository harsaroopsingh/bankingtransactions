package com.brainridge.bankingapi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false, updatable = false)
    private LocalDateTime accountCreatedAt;

    @PrePersist
    public void prePersist() {
        accountCreatedAt = LocalDateTime.now();
    }

    public Account(String username, String currency, BigDecimal initialBalance) {
        this.username = username;
        this.currency = currency;
        this.balance = initialBalance;
        
    }
}
