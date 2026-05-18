package com.financetransactions.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.financetransactions.model.TransactionModel;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<TransactionModel, Long> {

    @Query("SELECT SUM(t.amount) FROM TransactionModel t WHERE t.accountId = :accountId")
    BigDecimal sumAmountByAccountId(@Param("accountId") Long accountId);

    List<TransactionModel> findByAccountId(Long accountId);

    Page<TransactionModel> findAll(Pageable pageable);
    
}
