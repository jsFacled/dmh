package com.DigitalMoneyHouse.msvc_transactions.repository;


import com.DigitalMoneyHouse.msvc_transactions.models.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT DISTINCT t.destinationAccountId " +
            "FROM Transaction t " +
            "WHERE t.originAccountId = :accountId " +
            "AND t.destinationAccountId != t.originAccountId " +
            "ORDER BY t.date DESC")
    List<Long> findDistinctDestinationAccountIdsByAccountId(@Param("accountId") Long accountId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.originAccountId = :accountId OR t.destinationAccountId = :accountId ORDER BY t.date DESC")
    List<Transaction> findTop5ByAccountIdOrderByDateDesc(Long accountId);


    @Query("SELECT t FROM Transaction t WHERE t.originAccountId = :accountId OR t.destinationAccountId = :accountId")
    List<Transaction> findAllByAccountId(@Param("accountId") Long accountId);

}