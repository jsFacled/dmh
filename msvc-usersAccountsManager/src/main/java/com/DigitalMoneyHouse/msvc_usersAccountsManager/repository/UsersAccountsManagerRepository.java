package com.DigitalMoneyHouse.msvc_usersAccountsManager.repository;

import com.DigitalMoneyHouse.msvc_usersAccountsManager.entity.UsersAccountsManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersAccountsManagerRepository extends JpaRepository<UsersAccountsManager, Long> {

    //@Query(value = "SELECT EXISTS (SELECT 1 FROM users_accounts_manager WHERE account_id = :accountId AND user_id = :userId)", nativeQuery = true)
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UsersAccountsManager u WHERE u.accountId = :accountId AND u.userId = :userId")
    boolean isAuthorizedForAccount(@Param("accountId") Long accountId, @Param("userId") Long userId);

}
