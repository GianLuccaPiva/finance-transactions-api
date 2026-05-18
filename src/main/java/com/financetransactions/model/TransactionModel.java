package com.financetransactions.model;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class TransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(name = "Account_ID")
    private Long accountId;

    @Column(name = "OperationType_ID")
    private Long operationTypeId;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Column(name = "EventDate")
    private LocalDateTime eventDate;
    
   @PrePersist
   protected void onCreate() {
    this.eventDate = LocalDateTime.now();
   }

   public TransactionModel() {}

   public TransactionModel(Long accountId, Long operationTypeId, BigDecimal amount) {
       this.accountId = accountId;
       this.operationTypeId = operationTypeId;
       this.amount = amount;
   }

   public Long getTransactionId() { return this.transactionId; }
   public Long getAccountId() { return this.accountId; }
   public Long getOperationTypeId() { return this.operationTypeId; }
   public BigDecimal getAmount() { return this.amount; }
   public LocalDateTime getEventDate() { return this.eventDate; }


}
