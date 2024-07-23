package com.DigitalMoneyHouse.msvc_usersAccountsManager.dto;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsersAccountsManagerDTO {
    Long id;
    Long userId;
    Long accountId;
}
