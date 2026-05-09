package com.pismo.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.pismo.dto.TransactionRequest;
import com.pismo.dto.TransactionResponse;
import com.pismo.service.TransactionService;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> postTransaction(
        @RequestBody
        @Valid
        TransactionRequest request) {
            TransactionResponse response = transactionService.createTransaction(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }    

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getTransactions(Pageable pageable) {
        Page<TransactionResponse> response = transactionService.getTransactions(pageable);
        return ResponseEntity.ok(response);
    }

}