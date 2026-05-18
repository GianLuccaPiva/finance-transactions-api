package com.financetransactions.dto;

import java.math.BigDecimal;

public class TransactionResponse {

    private Long transactionId;   
    private Long accountId;
    private Long operationTypeId;
    private BigDecimal amount;

    public TransactionResponse(Long transactionId, Long accountId, Long operationTypeId, BigDecimal amount) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.operationTypeId = operationTypeId;
        this.amount = amount;
    }

    public Long getTransactionId() { return this.transactionId; }
    public Long getAccountId() { return this.accountId; }
    public Long getOperationTypeId() { return this.operationTypeId; }
    public BigDecimal getAmount() { return this.amount; }

}
