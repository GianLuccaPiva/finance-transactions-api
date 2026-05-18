package com.financetransactions.service;

import com.financetransactions.dto.BalanceResponse;
import com.financetransactions.dto.TransactionRequest;
import com.financetransactions.dto.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.financetransactions.exception.AccountNotFoundException;
import com.financetransactions.exception.InvalidTransactionException;
import com.financetransactions.model.ClientAccountModel;
import com.financetransactions.model.TransactionModel;
import java.util.Optional;
import com.financetransactions.repository.ClientAccountRepo;
import com.financetransactions.repository.OperationTypeRepo;
import com.financetransactions.repository.TransactionRepo;

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

    ClientAccountModel account = new ClientAccountModel();
    account.setAccountState(true);
    when(clientAccountRepo.findById(1L)).thenReturn(Optional.of(account));
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

    RuntimeException exception = assertThrows(AccountNotFoundException.class, () -> {
        transactionService.createTransaction(request);
    });

    assertEquals("Account not found", exception.getMessage());
    System.out.println("Exceção lançada: " + exception.getMessage());
}

    @Test
    void shouldThrowExceptionWhenOperationTypeNotFound() {

        TransactionRequest request = new TransactionRequest(1L, 99L, new BigDecimal("-50.00"));

        ClientAccountModel account = new ClientAccountModel();
        account.setAccountState(true);
        when(clientAccountRepo.findById(1L)).thenReturn(Optional.of(account));
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

        ClientAccountModel account4 = new ClientAccountModel();
        account4.setAccountState(true);
        when(clientAccountRepo.findById(1L)).thenReturn(Optional.of(account4));
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

        ClientAccountModel account2 = new ClientAccountModel();
        account2.setAccountState(true);
        when(clientAccountRepo.findById(1L)).thenReturn(Optional.of(account2));
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

        ClientAccountModel account1 = new ClientAccountModel();
        account1.setAccountState(true);
        when(clientAccountRepo.findById(1L)).thenReturn(Optional.of(account1));
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

        ClientAccountModel account4b = new ClientAccountModel();
        account4b.setAccountState(true);
        when(clientAccountRepo.findById(1L)).thenReturn(Optional.of(account4b));
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

    @Test
    void shouldReturnEmptyPageWhenNoTransactionsExist() {

        Pageable pageable = PageRequest.of(0, 10);

        when(transactionRepo.findAll(pageable)).thenReturn(Page.empty());

        Page<TransactionResponse> response = transactionService.getTransactions(pageable);

        assertTrue(response.isEmpty());
        System.out.println("Página retornada vazia: " + response.isEmpty());

    }

    @Test
    void shouldReturnPageWithTransactionsWhenTransactionsExist() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<TransactionModel> page = new PageImpl<>(List.of(
            new TransactionModel(1L, 1L, new BigDecimal("-50.00")),
            new TransactionModel(1L, 1L, new BigDecimal("-100.00")),
            new TransactionModel(1L, 1L, new BigDecimal("-50.00"))
        ));
        
        when(transactionRepo.findAll(pageable)).thenReturn(page);

        Page<TransactionResponse> response = transactionService.getTransactions(pageable);

        assertEquals(3, response.getTotalElements());
        System.out.println("Página retornada: " + response.getTotalElements() + " transações");

    }

}
