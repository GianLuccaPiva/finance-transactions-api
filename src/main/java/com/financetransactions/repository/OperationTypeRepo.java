package com.financetransactions.repository;

import com.financetransactions.model.OperationTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationTypeRepo extends JpaRepository<OperationTypeModel, Long>{
    
}
