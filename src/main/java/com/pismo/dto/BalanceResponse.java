package com.pismo.dto;

import java.math.BigDecimal;

public class BalanceResponse {

    Long accountId;

    BigDecimal balance;
    
    public BalanceResponse(Long accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    };

    public Long getAccountId() { return this.accountId; }
    public BigDecimal getBalance() { return this.balance; }
    
}
