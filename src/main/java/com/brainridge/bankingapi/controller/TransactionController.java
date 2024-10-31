package com.brainridge.bankingapi.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.brainridge.bankingapi.dto.TransactionHistoryRequestDTO;
import com.brainridge.bankingapi.dto.TransactionHistoryResponseDTO;
import com.brainridge.bankingapi.dto.TransferFundsRequestDTO;
import com.brainridge.bankingapi.dto.TransferFundsResponseDTO;
import com.brainridge.bankingapi.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/transactions/")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("transfer/funds")
    public ResponseEntity<TransferFundsResponseDTO> transferFunds(@Valid @RequestBody TransferFundsRequestDTO request) {
        TransferFundsResponseDTO response = transactionService.transferFunds(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("transfer/history")
    public ResponseEntity<List<TransactionHistoryResponseDTO>> getTransactionHistory(@Valid @RequestBody TransactionHistoryRequestDTO request) {
        List<TransactionHistoryResponseDTO> response = transactionService.getTransactionHistory(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
