package com.DigitalMoneyHouse.msvc_accounts.repository;

import com.DigitalMoneyHouse.msvc_accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
}
