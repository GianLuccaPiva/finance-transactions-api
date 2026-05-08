package com.pismo.service;

import java.math.BigDecimal;


import org.springframework.stereotype.Service;

import com.pismo.exception.AccountNotFoundException;
import com.pismo.exception.InvalidTransactionException;
import com.pismo.dto.TransactionRequest;
import com.pismo.dto.TransactionResponse;
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

    public TransactionResponse createTransaction(TransactionRequest request) {
        if (!clientAccountRepo.existsById(request.getAccountId())) {
            throw new AccountNotFoundException();
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
}




 

