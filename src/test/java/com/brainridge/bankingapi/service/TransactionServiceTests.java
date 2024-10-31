package com.brainridge.bankingapi.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private TransactionService transactionService;

    private Account issuerAccount;
    private Account recipientAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        issuerAccount = new Account();
        issuerAccount.setId(1L);
        issuerAccount.setUsername("issuer@example.com");
        issuerAccount.setBalance(BigDecimal.valueOf(1000));
        issuerAccount.setCurrency("CAD");

        recipientAccount = new Account();
        recipientAccount.setId(2L);
        recipientAccount.setUsername("recipient@example.com");
        recipientAccount.setBalance(BigDecimal.valueOf(500));
        recipientAccount.setCurrency("CAD");
    }

    @Test
    void transferFunds_Success() {
        TransferFundsRequestDTO request = new TransferFundsRequestDTO();
        request.setIssuerAccountId(issuerAccount.getId());
        request.setRecipientAccountId(recipientAccount.getId());
        request.setAmount(BigDecimal.valueOf(100));

        when(accountRepository.findById(issuerAccount.getId())).thenReturn(Optional.of(issuerAccount));
        when(accountRepository.findById(recipientAccount.getId())).thenReturn(Optional.of(recipientAccount));
        when(accountService.fundsSent(issuerAccount.getId(), request.getAmount())).thenReturn(issuerAccount);
        when(accountService.fundsReceived(recipientAccount.getId(), request.getAmount())).thenReturn(recipientAccount);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransferFundsResponseDTO response = transactionService.transferFunds(request);

        assertNotNull(response);
        assertEquals(recipientAccount.getUsername(), response.getRecipientUsername());
        assertEquals(request.getAmount(), response.getAmount());
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    void transferFunds_InsufficientBalance() {
        TransferFundsRequestDTO request = new TransferFundsRequestDTO();
        request.setIssuerAccountId(issuerAccount.getId());
        request.setRecipientAccountId(recipientAccount.getId());
        request.setAmount(BigDecimal.valueOf(2000)); // Amount exceeds balance

        when(accountRepository.findById(issuerAccount.getId())).thenReturn(Optional.of(issuerAccount));

        assertThrows(InsufficentBalanceException.class, () -> transactionService.transferFunds(request));
    }

    @Test
    void transferFunds_IssuerAccountNotFound() {
        TransferFundsRequestDTO request = new TransferFundsRequestDTO();
        request.setIssuerAccountId(1L);
        request.setRecipientAccountId(2L);
        request.setAmount(BigDecimal.valueOf(100));

        when(accountRepository.findById(request.getIssuerAccountId())).thenReturn(Optional.empty());

        assertThrows(RegisterAccountException.class, () -> transactionService.transferFunds(request));
    }

    @Test
    void transferFunds_RecipientAccountNotFound() {
        TransferFundsRequestDTO request = new TransferFundsRequestDTO();
        request.setIssuerAccountId(issuerAccount.getId());
        request.setRecipientAccountId(3L); // Invalid recipient ID
        request.setAmount(BigDecimal.valueOf(100));

        when(accountRepository.findById(issuerAccount.getId())).thenReturn(Optional.of(issuerAccount));
        when(accountRepository.findById(request.getRecipientAccountId())).thenReturn(Optional.empty());

        assertThrows(RegisterAccountException.class, () -> transactionService.transferFunds(request));
    }

    @Test
    void transferFunds_SameAccount() {
        TransferFundsRequestDTO request = new TransferFundsRequestDTO();
        request.setIssuerAccountId(issuerAccount.getId());
        request.setRecipientAccountId(issuerAccount.getId()); // Same account ID
        request.setAmount(BigDecimal.valueOf(100));

        when(accountRepository.findById(issuerAccount.getId())).thenReturn(Optional.of(issuerAccount));

        assertThrows(SameAccountException.class, () -> transactionService.transferFunds(request));
    }

    @Test
    void getTransactionHistory_Success() {
        TransactionHistoryRequestDTO request = new TransactionHistoryRequestDTO();
        request.setAccountId(issuerAccount.getId());

        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.of(issuerAccount));
        when(transactionRepository.findByAccountId(request.getAccountId())).thenReturn(Collections.emptyList());

        List<TransactionHistoryResponseDTO> response = transactionService.getTransactionHistory(request);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(transactionRepository, times(1)).findByAccountId(request.getAccountId());
    }

    @Test
    void getTransactionHistory_AccountNotFound() {
        TransactionHistoryRequestDTO request = new TransactionHistoryRequestDTO();
        request.setAccountId(3L); // Invalid account ID

        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.empty());

        assertThrows(RegisterAccountException.class, () -> transactionService.getTransactionHistory(request));
    }
}
