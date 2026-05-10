package com.pismo.service;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pismo.exception.AccountNotFoundException;
import com.pismo.exception.InvalidTransactionException;

import com.pismo.dto.BalanceResponse;
import com.pismo.dto.TransactionRequest;
import com.pismo.dto.TransactionResponse;
import com.pismo.model.ClientAccountModel;
import com.pismo.model.TransactionModel;
import com.pismo.repository.ClientAccountRepo;
import com.pismo.repository.OperationTypeRepo;
import com.pismo.repository.TransactionRepo;



@Service  
public class TransactionService {

    private final ClientAccountRepo clientAccountRepo;
    private final OperationTypeRepo operationTypeRepo;
    private final TransactionRepo transactionRepo;

    public TransactionService(
        ClientAccountRepo clientAccountRepo,
        OperationTypeRepo operationTypeRepo,
        TransactionRepo transactionRepo) {
            this.clientAccountRepo = clientAccountRepo;
            this.operationTypeRepo = operationTypeRepo;
            this.transactionRepo = transactionRepo;
    }

    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {

        if (!clientAccountRepo.existsById(accountId)) {
        throw new AccountNotFoundException();
        }

        List<TransactionModel> transactions = transactionRepo.findByAccountId(accountId);

        return transactions.stream()
        .map(t -> new TransactionResponse(t.getTransactionId(), t.getAccountId(), t.getOperationTypeId(), t.getAmount()))
        .collect(Collectors.toList());

    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {

        if (!clientAccountRepo.existsById(request.getAccountId())) {
            throw new AccountNotFoundException();
        }

        ClientAccountModel account = clientAccountRepo.findById(request.getAccountId())
        .orElseThrow(() -> new AccountNotFoundException());

        if (!account.isAccountState()) {
        throw new InvalidTransactionException("Invalid Transaction");
}

        if (!operationTypeRepo.existsById(request.getOperationTypeId())) {
            throw new InvalidTransactionException("Invalid Transaction");
        }

        switch (request.getOperationTypeId().intValue()) {
            case 4 -> {
                if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new InvalidTransactionException("Amount must be a positive value");
                }
            }
            
            case 1, 2, 3 -> {
                if (request.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
                    throw new InvalidTransactionException("Amount must be a negative value");
                    
                }
            }
            default -> throw new InvalidTransactionException("Invalid Operation Type");
          
        }

        TransactionModel transaction = new TransactionModel( 
            request.getAccountId(),
            request.getOperationTypeId(),
            request.getAmount()  
        );
        
        TransactionModel saved = transactionRepo.save(transaction);

        return new TransactionResponse(
            saved.getTransactionId(),
            saved.getAccountId(),
            saved.getOperationTypeId(),
            saved.getAmount()    
        );

    }

    public BalanceResponse getBalanceByAccountId(Long accountId) {

            if (!clientAccountRepo.existsById(accountId)) {
            throw new AccountNotFoundException();
            }

            BigDecimal balance = transactionRepo.sumAmountByAccountId(accountId);
            balance = balance != null ? balance : BigDecimal.ZERO;

            return new BalanceResponse(accountId, balance);

        }

    public Page<TransactionResponse> getTransactions(Pageable pageable) {

        Page<TransactionModel> transactionPage = transactionRepo.findAll(pageable);
        
        return transactionPage.map(t -> new TransactionResponse(
        t.getTransactionId(), t.getAccountId(), t.getOperationTypeId(), t.getAmount()
        ));

    }
}




 

