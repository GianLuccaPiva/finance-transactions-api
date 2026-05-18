package com.financetransactions.service;

import com.financetransactions.dto.ClientAccountRequest;
import com.financetransactions.dto.ClientAccountResponse;
import com.financetransactions.exception.DuplicateDocumentException;
import com.financetransactions.exception.InvalidTransactionException;
import com.financetransactions.model.ClientAccountModel;
import com.financetransactions.repository.ClientAccountRepo;
import com.financetransactions.exception.AccountNotFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class ClientAccountServiceTest {

    @Mock
    private ClientAccountRepo clientAccountRepo;

    @InjectMocks
    private ClientAccountService clientAccountService;

    @Test
    void shouldCreateAccountSuccessfully() {
        ClientAccountRequest request = new ClientAccountRequest();
        request.setDocumentNumber("12345678901");

        ClientAccountModel saved = new ClientAccountModel();
        saved.setAccountId(1L);
        saved.setDocumentNumber("12345678901");

        when(clientAccountRepo.existsByDocumentNumber("12345678901")).thenReturn(false);
        when(clientAccountRepo.save(any(ClientAccountModel.class))).thenReturn(saved);

        ClientAccountResponse response = clientAccountService.createAccount(request);
        
        System.out.println("Account ID retornado: " + response.getAccountId());
        System.out.println("Document retornado: " + response.getDocumentNumber());

        assertNotNull(response);
        assertEquals(1L, response.getAccountId());
        assertEquals("12345678901", response.getDocumentNumber());

    }

    @Test
    void shouldRejectDocumentWithMoreOrLessThan11Digits() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    ClientAccountRequest request = new ClientAccountRequest();
    request.setDocumentNumber("123456789012"); // 12 dígitos

    Set<ConstraintViolation<ClientAccountRequest>> violations = validator.validate(request);

    assertFalse(violations.isEmpty());

    System.out.println("Violations encontradas: " + violations.size());
    violations.forEach(v -> System.out.println("Mensagem: " + v.getMessage()));
}

    @Test
    void shouldThrowExceptionWhenDocumentAlreadyExists() {
     
        ClientAccountRequest request = new ClientAccountRequest();
        request.setDocumentNumber("12345678901");

        when(clientAccountRepo.existsByDocumentNumber("12345678901")).thenReturn(true);

        assertThrows(DuplicateDocumentException.class, () -> {
            clientAccountService.createAccount(request);
        });
    }

    @Test
    void shouldGetAccountSuccessfully() {
    
        ClientAccountModel account = new ClientAccountModel();
        account.setAccountId(1L);
        account.setDocumentNumber("12345678901");

        when(clientAccountRepo.findById(1L)).thenReturn(Optional.of(account));

        ClientAccountResponse response = clientAccountService.getAccount(1L);

        assertNotNull(response);
        assertEquals(1L, response.getAccountId());
        assertEquals("12345678901", response.getDocumentNumber());
        System.out.println("Account ID retornado: " + response.getAccountId());
        System.out.println("Document retornado: " + response.getDocumentNumber());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {

        when(clientAccountRepo.findById(99L)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            clientAccountService.getAccount(99L);
        });
        System.out.println("Exceção lançada: " + exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundForDelete() {

        when(clientAccountRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            clientAccountService.deleteAccountById(99L);
        });
    }

    @Test
    void shouldThrowExceptionWhenAccountIsAlreadyInactive() {

        ClientAccountModel account = new ClientAccountModel();
        account.setAccountState(false);

        when(clientAccountRepo.findById(1L)).thenReturn(Optional.of(account));

        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class, () -> {
            clientAccountService.deleteAccountById(1L);
        });

        assertEquals("Account is already inactive", exception.getMessage());
        System.out.println("Exceção lançada: " + exception.getMessage());
    }

    @Test
    void shouldDeactivateAccountSuccessfully() {

        ClientAccountModel account = new ClientAccountModel();
        account.setAccountState(true);

        when(clientAccountRepo.findById(1L)).thenReturn(Optional.of(account));
        when(clientAccountRepo.save(any(ClientAccountModel.class))).thenReturn(account);

        clientAccountService.deleteAccountById(1L);

        assertFalse(account.isAccountState());
        System.out.println("Account state após desativação: " + account.isAccountState());
    }
}
