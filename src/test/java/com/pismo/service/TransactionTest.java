package com.pismo.service;

import com.pismo.dto.TransactionRequest;
import com.pismo.dto.TransactionResponse;
import com.pismo.exception.AccountNotFoundException;
import com.pismo.exception.InvalidTransactionException;
import com.pismo.model.TransactionModel;
import com.pismo.repository.ClientAccountRepo;
import com.pismo.repository.OperationTypeRepo;
import com.pismo.repository.TransactionRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionTest {

    @Mock
    private ClientAccountRepo clientAccountRepo;

    @Mock
    private OperationTypeRepo operationTypeRepo;

    @Mock
    private TransactionRepo transactionRepo;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldCreateTransactionSuccessfully() {

    TransactionRequest request = new TransactionRequest(1L, 1L, new BigDecimal("-50.00"));

    TransactionModel saved = new TransactionModel(1L, 1L, new BigDecimal("-50.00"));

    when(clientAccountRepo.existsById(1L)).thenReturn(true);
    when(operationTypeRepo.existsById(1L)).thenReturn(true);
    when(transactionRepo.save(any(TransactionModel.class))).thenReturn(saved);

    TransactionResponse response = transactionService.createTransaction(request);

    assertNotNull(response);
    System.out.println("Transaction criada com accountId: " + response.getAccountId());
    System.out.println("Amount: " + response.getAmount());
}
    @Test
    void shouldThrowExceptionWhenAccountNotFound() {

    TransactionRequest request = new TransactionRequest(99L, 1L, new BigDecimal("-50.00"));

    when(clientAccountRepo.existsById(99L)).thenReturn(false);

    RuntimeException exception = assertThrows(AccountNotFoundException.class, () -> {
        transactionService.createTransaction(request);
    });

    assertEquals("Account not found", exception.getMessage());
    System.out.println("Exceção lançada: " + exception.getMessage());
}

    @Test
    void shouldThrowExceptionWhenOperationTypeNotFound() {

        TransactionRequest request = new TransactionRequest(1L, 99L, new BigDecimal("-50.00"));

        when(clientAccountRepo.existsById(1L)).thenReturn(true);
        when(operationTypeRepo.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(InvalidTransactionException.class, () -> {
            transactionService.createTransaction(request);
        });

        assertEquals("Invalid Transaction", exception.getMessage());
        System.out.println("Exceção lançada: " + exception.getMessage());


    }

    @Test
    void shouldThrowExceptionWhenPaymentHasNegativeAmount() {
        TransactionRequest request = new TransactionRequest(1L, 4L, new BigDecimal("-50.00"));

        when(clientAccountRepo.existsById(1L)).thenReturn(true);
        when(operationTypeRepo.existsById(4L)).thenReturn(true);

        RuntimeException exception = assertThrows(InvalidTransactionException.class, () -> {
            transactionService.createTransaction(request);
        });

        assertEquals("Amount must be a positive value", exception.getMessage());
        System.out.println("Exceção lançada: " + exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenDebitOperationHasPositiveAmount() {
        TransactionRequest request = new TransactionRequest(1L, 2L, new BigDecimal("50.00"));

        when(clientAccountRepo.existsById(1L)).thenReturn(true);
        when(operationTypeRepo.existsById(2L)).thenReturn(true);

        RuntimeException exception = assertThrows(InvalidTransactionException.class, () -> {
            transactionService.createTransaction(request);
        });

        assertEquals("Amount must be a negative value", exception.getMessage());
        System.out.println("Exceção lançada: " + exception.getMessage());
        
    }

    @Test
    void shouldThrowExceptionWhenDebitAmountIsZero() {
        TransactionRequest request = new TransactionRequest(1L, 1L, new BigDecimal("0"));

        when(clientAccountRepo.existsById(1L)).thenReturn(true);
        when(operationTypeRepo.existsById(1L)).thenReturn(true);

        RuntimeException exception = assertThrows(InvalidTransactionException.class, () -> {
            transactionService.createTransaction(request);
        });
        
        assertEquals("Amount must be a negative value", exception.getMessage());
        System.out.println("Exceção lançada: " + exception.getMessage());

    }
    @Test
    void shouldThrowExceptionWhenPaymentAmountIsZero() {
        TransactionRequest request = new TransactionRequest(1L, 4L, new BigDecimal("0"));

        when(clientAccountRepo.existsById(1L)).thenReturn(true);
        when(operationTypeRepo.existsById(4L)).thenReturn(true);

        RuntimeException exception = assertThrows(InvalidTransactionException.class, () -> {
            transactionService.createTransaction(request);
        });
        
        assertEquals("Amount must be a positive value", exception.getMessage());
        System.out.println("Exceção lançada: " + exception.getMessage());

    }
}
