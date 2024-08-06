package com.DigitalMoneyHouse.msvc_usersAccountsManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestDTO {
    private String email;
    private String password;

    // Getters and setters
}
