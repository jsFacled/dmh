package com.DigitalMoneyHouse.msvc_accounts.repository;

import com.DigitalMoneyHouse.msvc_accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a.balance FROM Account a WHERE a.id = :accountId")
    BigDecimal findBalanceByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT a.userId FROM Account a WHERE a.id = :accountId")
    Long findUserIdByAccountId(@Param("accountId") Long accountId);

    boolean existsById(Long accountId);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.id = :accountId AND a.balance >= :amount")
    boolean decreaseBalanceIfSufficient(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.id = :accountId")
    boolean increaseBalance(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);

}
