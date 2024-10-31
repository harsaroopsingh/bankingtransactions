package com.brainridge.bankingapi.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brainridge.bankingapi.dto.RegisterAccountRequestDTO;
import com.brainridge.bankingapi.dto.RegisterAccountResponseDTO;
import com.brainridge.bankingapi.service.AccountService;


@RestController
@RequestMapping("/accounts/")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    AccountController(AccountService accountService){
        this.accountService = accountService;
    }
    @PostMapping("register")
    public ResponseEntity<RegisterAccountResponseDTO> registerAccount(@Valid @RequestBody RegisterAccountRequestDTO request) {
        RegisterAccountResponseDTO response =  accountService.registerAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
