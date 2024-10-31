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
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long vendorAccountId;

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private boolean incoming; 

    @Column(nullable = false)
    private boolean outgoing;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String vendorUsername;

    @Column(nullable = false, updatable = false)
    private LocalDateTime transactionCreatedAt;

    @PrePersist
    public void prePersist() {
        transactionCreatedAt = LocalDateTime.now();
    }

    public Transaction(Long vendorAccountId,
                       Long accountId, 
                       boolean incoming, 
                       boolean outgoing, 
                       String currency, 
                       BigDecimal amount, 
                       BigDecimal balance, 
                       String vendorUsername) {
        this.vendorAccountId = vendorAccountId;
        this.accountId = accountId;
        this.currency = currency;
        this.amount = amount;
        this.incoming = incoming;
        this.outgoing = outgoing;
        this.balance = balance;
        this.vendorUsername = vendorUsername;
    }

}