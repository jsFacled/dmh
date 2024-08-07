package com.DigitalMoneyHouse.msvc_usersAccountsManager.service;


import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface IUsersAccountsManagerService {


    ResponseEntity<?> registrarUserAccount(UserDTO userDTO);


}
