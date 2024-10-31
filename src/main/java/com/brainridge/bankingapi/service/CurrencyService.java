package com.brainridge.bankingapi.service;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brainridge.bankingapi.model.Currency;
import com.brainridge.bankingapi.repository.CurrencyRepository;

import java.math.BigDecimal;

import java.util.Optional;

@Service
public class CurrencyService {
    private CurrencyRepository currencyRepository;
    

    @Autowired
    CurrencyService(CurrencyRepository currencyRepository){
        this.currencyRepository= currencyRepository;
    }

    @PostConstruct
    public void init() {
        if (currencyRepository.count() == 0) {
            currencyRepository.save(new Currency("USD", BigDecimal.valueOf(1.0)));
            currencyRepository.save(new Currency("EUR", BigDecimal.valueOf(0.85)));
            currencyRepository.save(new Currency("GBP", BigDecimal.valueOf(0.75)));
            currencyRepository.save(new Currency("CAD", BigDecimal.valueOf(1.3)));
        }
    }

    public BigDecimal convertCurrency(BigDecimal amount, String fromCurrencyCode, String toCurrencyCode) {
        if (fromCurrencyCode.equals(toCurrencyCode)) {
            return amount;
        }

        Optional<Currency> OptionalFromCurrency = currencyRepository.findByCode(fromCurrencyCode);
        Optional<Currency> OptionalToCurrency = currencyRepository.findByCode(toCurrencyCode);
        Currency fromCurrency = OptionalFromCurrency.get();
        Currency toCurrency = OptionalToCurrency.get();
        
        BigDecimal amountInBaseCurrency = amount.divide(fromCurrency.getExchangeRate());
        return amountInBaseCurrency.multiply(toCurrency.getExchangeRate());
    }
}
