package com.financetransactions.dto;

public class ClientAccountResponse {

    private Long accountId;
    private String documentNumber;

    public ClientAccountResponse(Long accountId, String documentNumber) {
        this.accountId = accountId;
        this.documentNumber = documentNumber;

    }

    public Long getAccountId() { return this.accountId; }
    public String getDocumentNumber() { return this.documentNumber; }
}