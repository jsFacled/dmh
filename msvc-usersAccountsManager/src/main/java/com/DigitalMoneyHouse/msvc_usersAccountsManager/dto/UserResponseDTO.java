package com.DigitalMoneyHouse.msvc_usersAccountsManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private String phone;
}