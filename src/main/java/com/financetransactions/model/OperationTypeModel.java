package com.financetransactions.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "operation_types")
public class OperationTypeModel {

    @Id
    private Long operationTypeId;

    @Column(nullable = false, unique = true)
    private String description;
    
    public OperationTypeModel() {}

    public Long getOperationTypeId() { return this.operationTypeId; }

    public String getOperationTypeDescription() { return this.description; }

    // Devido aos parâmetros serem gerados na criação do banco acredito 
    // que não faça sentido existir os setters para esses parâmetros
}
