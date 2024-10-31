package com.brainridge.bankingapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brainridge.bankingapi.dto.RegisterAccountRequestDTO;
import com.brainridge.bankingapi.dto.RegisterAccountResponseDTO;
import com.brainridge.bankingapi.exception.RegisterAccountException;
import com.brainridge.bankingapi.model.Account;
import com.brainridge.bankingapi.repository.AccountRepository;
import com.brainridge.bankingapi.repository.CurrencyRepository;

import java.math.BigDecimal;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    AccountService(AccountRepository accountRepository,
                   CurrencyRepository currencyRepository) {
        this.accountRepository = accountRepository;
        this.currencyRepository = currencyRepository;
    }

    @Transactional
    public RegisterAccountResponseDTO registerAccount(RegisterAccountRequestDTO request) {

        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RegisterAccountException("Account with that username already exists");
        }
        if (!currencyRepository.findByCode(request.getCurrency()).isPresent()){
            throw new RegisterAccountException("Currency code not supported");
        }

        BigDecimal balance = request.getBalance() == null ? BigDecimal.ZERO : request.getBalance();
        String currency = request.getCurrency();
        Account account = new Account(request.getUsername(), currency, balance);
        accountRepository.save(account);
        return RegisterAccountResponseDTO.builder()
                .id(account.getId())
                .username(account.getUsername())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .build();
    }

    public Account fundsReceived(Long id, BigDecimal amount) {
        Account account = accountRepository.findById(id).get();
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        return account;
    }

    public Account fundsSent(Long id, BigDecimal amount) {
        Account account = accountRepository.findById(id).get();
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        return account;
    }

}
