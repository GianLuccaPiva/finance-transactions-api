package com.financetransactions.dto;


import jakarta.validation.constraints.Pattern;

public class ClientAccountRequest {

    @Pattern(regexp = "\\d{11}", message = "Document must contain only 11 digits")
    private String documentNumber;

    public ClientAccountRequest() {}
    
    public String getDocumentNumber() { return this.documentNumber; }
    public void setDocumentNumber(String documentNumber) {this.documentNumber = documentNumber;}


}
