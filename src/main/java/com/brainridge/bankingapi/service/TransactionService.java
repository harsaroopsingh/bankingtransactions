package com.brainridge.bankingapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.brainridge.bankingapi.dto.TransactionHistoryRequestDTO;
import com.brainridge.bankingapi.dto.TransactionHistoryResponseDTO;
import com.brainridge.bankingapi.dto.TransferFundsRequestDTO;
import com.brainridge.bankingapi.dto.TransferFundsResponseDTO;
import com.brainridge.bankingapi.exception.InsufficentBalanceException;
import com.brainridge.bankingapi.exception.RegisterAccountException;
import com.brainridge.bankingapi.exception.SameAccountException;
import com.brainridge.bankingapi.model.Account;
import com.brainridge.bankingapi.model.Transaction;
import com.brainridge.bankingapi.repository.AccountRepository;
import com.brainridge.bankingapi.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final CurrencyService currencyService;

    @Autowired
    TransactionService(TransactionRepository transactionRepository,
                       AccountRepository accountRepository,
                       AccountService accountService,
                       CurrencyService currencyService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.currencyService = currencyService;
    }

    @Transactional
    public TransferFundsResponseDTO transferFunds(@RequestBody TransferFundsRequestDTO request) {
        Optional<Account> optionalIssuerAccount = accountRepository.findById(request.getIssuerAccountId());
        if (optionalIssuerAccount.isEmpty()) {
            throw new RegisterAccountException("Issuer does not have a registered account");
        } else if (optionalIssuerAccount.get().getBalance().subtract(request.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficentBalanceException("Insufficient balance in the account");
        }
        if(request.getIssuerAccountId().equals(request.getRecipientAccountId())){
            throw new SameAccountException("Issuer and Recipient must be different");
        }
        if (accountRepository.findById(request.getRecipientAccountId()).isEmpty()) {
           throw new RegisterAccountException("Recipient does not have a registered account");
        }
        
        BigDecimal amount = request.getAmount();
        Account issuerAccount = accountService.fundsSent(request.getIssuerAccountId(), amount);
        Account recipientAccount = accountService.fundsReceived(request.getRecipientAccountId(), amount);
        BigDecimal convertedAmount;
        
        if (!issuerAccount.getCurrency().equals(recipientAccount.getCurrency())) {
            convertedAmount = currencyService.convertCurrency(amount, issuerAccount.getCurrency(), recipientAccount.getCurrency());
        } else {
            convertedAmount = amount;
        }

        Transaction issuerTransaction = new Transaction(
                issuerAccount.getId(), 
                false, 
                true,
                issuerAccount.getCurrency(),
                amount,
                issuerAccount.getBalance(), 
                recipientAccount.getUsername());

        transactionRepository.save(issuerTransaction);

        Transaction recipientTransaction = new Transaction(
                recipientAccount.getId(), 
                true, 
                false,
                recipientAccount.getCurrency(),
                convertedAmount, 
                recipientAccount.getBalance(), 
                issuerAccount.getUsername());

        transactionRepository.save(recipientTransaction);

        return TransferFundsResponseDTO.builder()
                .transferCreatedAt(issuerTransaction.getTransactionCreatedAt())
                .recipientUsername(recipientAccount.getUsername())
                .currency(recipientAccount.getCurrency())
                .amount(convertedAmount)
                .build();
    }

    @Transactional(readOnly = true)
    public List<TransactionHistoryResponseDTO> getTransactionHistory(@RequestBody TransactionHistoryRequestDTO request) {
        List<Transaction> transactions = transactionRepository.findByAccountId(request.getAccountId());
        Long accountId = request.getAccountId();
        if (accountRepository.findById(accountId).isEmpty()) {
            throw new RegisterAccountException("Account not found");
        }
        return transactions.stream()
                .map(transaction -> TransactionHistoryResponseDTO.builder()
                        .recentTransaction(transaction.getAmount())
                        .currency(transaction.getCurrency())
                        .accountBalanceAfterTransaction(transaction.getAccountBalanceAfterTransaction())
                        .incoming(transaction.isIncoming())
                        .outgoing(transaction.isOutgoing())
                        .vendorUsername(transaction.getVendorUsername())
                        .timeStamp(transaction.getTransactionCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
