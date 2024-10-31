package com.brainridge.bankingapi.service;

import com.brainridge.bankingapi.dto.RegisterAccountRequestDTO;
import com.brainridge.bankingapi.exception.RegisterAccountException;
import com.brainridge.bankingapi.model.Account;
import com.brainridge.bankingapi.model.Currency;
import com.brainridge.bankingapi.repository.AccountRepository;
import com.brainridge.bankingapi.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CurrencyRepository currencyRepository; // Add @Mock annotation here

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerAccount_Success() {
        RegisterAccountRequestDTO request = new RegisterAccountRequestDTO();
        request.setUsername("test@example.com");
        request.setCurrency("CAD"); // Ensure this matches the new field
        request.setBalance(BigDecimal.ZERO);

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Currency currency = new Currency(); // Create a new Currency object or use a mock
        currency.setCode("CAD");
        when(currencyRepository.findByCode("CAD")).thenReturn(Optional.of(currency));

        var response = accountService.registerAccount(request);

        assertNotNull(response);
        assertEquals("test@example.com", response.getUsername());
        assertEquals("CAD", response.getCurrency()); // Ensure this matches the new field
        assertEquals(BigDecimal.ZERO, response.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    
    @Test
    void registerAccount_InvalidCurrency() {
        RegisterAccountRequestDTO request = new RegisterAccountRequestDTO();
        request.setUsername("test@example.com");
        request.setCurrency("INVALID_CURRENCY"); // Invalid currency code
        request.setBalance(BigDecimal.ZERO);

        Currency currency = new Currency(); // Create a new Currency object or use a mock
        currency.setCode("zzz");
        when(currencyRepository.findByCode("zzz")).thenReturn(Optional.empty());

        RegisterAccountException thrown = assertThrows(RegisterAccountException.class, () -> {
            accountService.registerAccount(request);
        });

        assertEquals("Currency code not supported", thrown.getMessage());
    }

    @Test
    void registerAccount_AccountAlreadyExists() {
        RegisterAccountRequestDTO request = new RegisterAccountRequestDTO();
        request.setUsername("test@example.com");
        request.setCurrency("CAD");
        request.setBalance(BigDecimal.ZERO);

        Currency currency = new Currency();
        currency.setCode("CAD");
        when(currencyRepository.findByCode("CAD")).thenReturn(Optional.of(currency));

        // Mock the account repository to return an existing account
        when(accountRepository.findByUsername("test@example.com")).thenReturn(Optional.of(new Account()));

        // Check if RegisterAccountException is thrown
        RegisterAccountException thrown = assertThrows(RegisterAccountException.class, () -> {
            accountService.registerAccount(request);
        });

        assertEquals("Account with that username already exists", thrown.getMessage()); // Adjust the message as per your exception implementation
    }
}
