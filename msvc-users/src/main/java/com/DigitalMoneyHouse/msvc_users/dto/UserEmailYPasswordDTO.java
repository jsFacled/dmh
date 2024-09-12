package com.DigitalMoneyHouse.msvc_users.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEmailYPasswordDTO {
    private String email;
    private String password;

    public UserEmailYPasswordDTO() {

    }
}
