package com.DigitalMoneyHouse.msvc_transactions.repository;


import com.DigitalMoneyHouse.msvc_transactions.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTop5ByAccountIdOrderByDateDesc(Long accountId);
}
