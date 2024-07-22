package com.DigitalMoneyHouse.msvc_usersAccountsManager.repository;

import com.DigitalMoneyHouse.msvc_usersAccountsManager.entity.UsersAccountsManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersAccountsManagerRepository extends JpaRepository<UsersAccountsManager, Long> {
}
