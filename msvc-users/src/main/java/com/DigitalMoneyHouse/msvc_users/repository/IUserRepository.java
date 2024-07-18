package com.DigitalMoneyHouse.msvc_users.repository;

import com.DigitalMoneyHouse.msvc_users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

}

