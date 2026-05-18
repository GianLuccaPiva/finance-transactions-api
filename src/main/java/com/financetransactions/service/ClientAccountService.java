package com.financetransactions.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financetransactions.exception.InvalidTransactionException;
import com.financetransactions.exception.AccountNotFoundException;
import com.financetransactions.exception.DuplicateDocumentException;
import com.financetransactions.dto.ClientAccountRequest;
import com.financetransactions.dto.ClientAccountResponse;
import com.financetransactions.model.ClientAccountModel;
import com.financetransactions.repository.ClientAccountRepo;

@Service
public class ClientAccountService {


    private ClientAccountRepo clientAccountRepo;

    public ClientAccountService(ClientAccountRepo clientAccountRepo) {
        this.clientAccountRepo = clientAccountRepo;
    }

    @Transactional
    public ClientAccountResponse createAccount(ClientAccountRequest request) {

        if (clientAccountRepo.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new DuplicateDocumentException();
        }

        ClientAccountModel account = new ClientAccountModel();
        account.setDocumentNumber(request.getDocumentNumber());

        ClientAccountModel saved = clientAccountRepo.save(account);

        return new ClientAccountResponse(saved.getAccountId(), saved.getDocumentNumber());

    }

    @Transactional
    public void deleteAccountById(Long accountId) {

       ClientAccountModel account = clientAccountRepo.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException());

        if (!account.isAccountState()) {
        throw new InvalidTransactionException("Account is already inactive");
        }

        account.setAccountState(false);

        clientAccountRepo.save(account);

    }

    public ClientAccountResponse getAccount(Long accountId) {

        ClientAccountModel account = clientAccountRepo.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException());

        return new ClientAccountResponse(account.getAccountId(), account.getDocumentNumber());

    }
    
}
