package com.pismo.service;

import com.pismo.dto.BalanceResponse;
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
import java.util.List;

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

    @Test
    void shouldThrowExceptionWhenGetTransactionsDontFindAccount() {

        when(clientAccountRepo.existsById(99L)).thenReturn(false);

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
         transactionService.getTransactionsByAccountId(99L);
        });
        
        System.out.println("Exceção lançada: " + exception.getMessage());
    }

    @Test
    void shouldReturnEmptyListWhenAccountHasNoTransactions() {

        when(clientAccountRepo.existsById(1L)).thenReturn(true);
        when(transactionRepo.findByAccountId(1L)).thenReturn(List.of());

        List<TransactionResponse> response = transactionService.getTransactionsByAccountId(1L);

        assertTrue(response.isEmpty());
        System.out.println("Lista retornada: " + response.size() + " transações");

    }

    @Test
    void shouldReturnTransactionsWhenAccountExists() {

        when(clientAccountRepo.existsById(1L)).thenReturn(true);
        when(transactionRepo.findByAccountId(1L)).thenReturn(List.of(
            new TransactionModel(1L, 1L, new BigDecimal("-50.00")),
            new TransactionModel(1L, 2L, new BigDecimal("-150.00"))
        ));
    
        List<TransactionResponse> response = transactionService.getTransactionsByAccountId(1L);

        assertEquals(2, response.size());
        System.out.println("Lista retornada " + response.size() + "transações");
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundForBalance() {

        when(clientAccountRepo.existsById(99L)).thenReturn(false);

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
        transactionService.getBalanceByAccountId(99L);
        });

        System.out.println("Exceção lançada: " + exception.getMessage());
        }
    
    @Test
    void shouldReturnZeroBalanceWhenAccountHasNoTransactions() {

        when(clientAccountRepo.existsById(1L)).thenReturn(true);
        when(transactionRepo.sumAmountByAccountId(1L)).thenReturn(null);

        BalanceResponse response = transactionService.getBalanceByAccountId(1L);

        assertEquals(BigDecimal.ZERO, response.getBalance());
        System.out.println("Balance retornado: " + response.getBalance());
    }

    @Test
    void shouldReturnBalanceWhenAccountHasTransactions() {

        when(clientAccountRepo.existsById(1L)).thenReturn(true);
        when(transactionRepo.sumAmountByAccountId(1L)).thenReturn(new BigDecimal("50.00"));

        BalanceResponse response = transactionService.getBalanceByAccountId(1L);

        assertEquals(new BigDecimal("50.00"), response.getBalance());
        System.out.println("Balance retornado: " + response.getBalance());
    }

}
