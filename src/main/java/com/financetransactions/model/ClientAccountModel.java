package com.financetransactions.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Table(name = "accounts")
public class ClientAccountModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(name = "document_number", nullable = false, unique = true)
    private String documentNumber;

    @Column(name = "account_state", nullable =false)
    private boolean accountState = true;

    public ClientAccountModel() {}

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

    public boolean isAccountState() { return accountState; }
    public void setAccountState(boolean accountState) { this.accountState = accountState; }

}
