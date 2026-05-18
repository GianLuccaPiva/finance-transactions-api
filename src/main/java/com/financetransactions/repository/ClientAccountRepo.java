package com.financetransactions.repository;

import com.financetransactions.model.ClientAccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientAccountRepo extends JpaRepository<ClientAccountModel, Long>{

    boolean existsByDocumentNumber(String documentNumber);
    
    
}
