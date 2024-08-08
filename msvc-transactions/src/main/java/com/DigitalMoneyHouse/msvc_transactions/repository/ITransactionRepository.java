package com.DigitalMoneyHouse.msvc_transactions.repository;


import com.DigitalMoneyHouse.msvc_transactions.models.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
   // @Query("SELECT t FROM transaction t WHERE t.destinationAccountId = :accountId OR t.originTypeId = :accountId ORDER BY t.date DESC")
  //  List<Transaction> findTop5ByAccountIdOrderByDateDesc(Long accountId);


    @Query("SELECT t FROM Transaction t WHERE t.originAccountId = :accountId OR t.destinationAccountId = :accountId")
    List<Transaction> findAllByAccountId(@Param("accountId") Long accountId);

}