package com.pismo.repository;

import com.pismo.model.TransactionModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<TransactionModel, Long> {

    List<TransactionModel> findByAccountId(Long accountId);
    
}
