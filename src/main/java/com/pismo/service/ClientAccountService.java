package com.pismo.service;

import org.springframework.stereotype.Service;

import com.pismo.exception.AccountNotFoundException;
import com.pismo.exception.DuplicateDocumentException;
import com.pismo.dto.ClientAccountRequest;
import com.pismo.dto.ClientAccountResponse;
import com.pismo.model.ClientAccountModel;
import com.pismo.repository.ClientAccountRepo;

@Service
public class ClientAccountService {

    private ClientAccountRepo clientAccountRepo;

    public ClientAccountService(ClientAccountRepo clientAccountRepo) {
        this.clientAccountRepo = clientAccountRepo;
    }

    public ClientAccountResponse createAccount(ClientAccountRequest request) {

        if (clientAccountRepo.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new DuplicateDocumentException();
        }

        ClientAccountModel account = new ClientAccountModel();
        account.setDocumentNumber(request.getDocumentNumber());

        ClientAccountModel saved = clientAccountRepo.save(account);

        return new ClientAccountResponse(saved.getAccountId(), saved.getDocumentNumber());

    }

    public ClientAccountResponse getAccount(Long accountId) {

        ClientAccountModel account = clientAccountRepo.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException());

        return new ClientAccountResponse(account.getAccountId(), account.getDocumentNumber());

    }
    
}
