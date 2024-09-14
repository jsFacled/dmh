package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.autModels;

import lombok.Data;

@Data

public class LoginRequestDTO {
    private String email;
    private String password;

    public LoginRequestDTO(String email, String password) {

    }

    // Getters and setters
}
