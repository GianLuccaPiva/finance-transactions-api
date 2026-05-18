package com.financetransactions.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public class TransactionRequest {

    @NotNull
    Long accountId;

    @NotNull
    Long operationTypeId;

    BigDecimal amount;
    
    public TransactionRequest() {}

    public TransactionRequest(Long accountId, Long operationTypeId, BigDecimal amount) {
    this.accountId = accountId;
    this.operationTypeId = operationTypeId;
    this.amount = amount;
}

    public Long getAccountId() { return this.accountId; }
    public Long getOperationTypeId() { return this.operationTypeId; }
    public BigDecimal getAmount() { return this.amount; }



}
