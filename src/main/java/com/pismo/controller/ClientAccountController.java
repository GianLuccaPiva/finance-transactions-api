package com.pismo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.pismo.dto.BalanceResponse;
import com.pismo.dto.ClientAccountRequest;
import com.pismo.dto.ClientAccountResponse;
import com.pismo.dto.TransactionResponse;
import com.pismo.service.ClientAccountService;
import com.pismo.service.TransactionService;

import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class ClientAccountController {

    private ClientAccountService clientAccountService;
    private TransactionService transactionService;
    
    public ClientAccountController(ClientAccountService clientAccountService, TransactionService transactionService) {
        this.clientAccountService = clientAccountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<ClientAccountResponse> createAccount(
        @RequestBody
        @Valid
        ClientAccountRequest request) {

            ClientAccountResponse response = clientAccountService.createAccount(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClientAccountResponse> getAccount(
        @PathVariable 
        Long id) {

        ClientAccountResponse response = clientAccountService.getAccount(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccountId(@PathVariable Long id) {
        List<TransactionResponse> response = transactionService.getTransactionsByAccountId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceResponse> getBalanceByAccountId(@PathVariable Long id) {
        BalanceResponse response = transactionService.getBalanceByAccountId(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable Long id) {
        
        clientAccountService.deleteAccountById(id);
        return ResponseEntity.noContent().build();
    }
        
}
