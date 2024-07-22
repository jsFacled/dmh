package com.DigitalMoneyHouse.msvc_usersAccountsManager.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private String phone;
    private String password;


}
