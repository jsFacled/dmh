package com.DigitalMoneyHouse.msvc_usersAccountsManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegisteredResponseDTO {
    private Long userId;
    private String message;

}