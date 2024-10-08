package com.DigitalMoneyHouse.msvc_users.repository;

import com.DigitalMoneyHouse.msvc_users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {


    @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "SELECT id FROM user WHERE email = :email", nativeQuery = true)
    Optional<Long> getUserIdByUserEmail(@Param("email") String email);


}

