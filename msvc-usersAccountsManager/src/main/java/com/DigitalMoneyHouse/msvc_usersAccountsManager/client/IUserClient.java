package com.DigitalMoneyHouse.msvc_usersAccountsManager.client;


import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserRegisteredResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(name = "msvc-users", url = "http://localhost:8082/users")
public interface IUserClient {

    @PostMapping("/create")
    UserRegisteredResponseDTO createUser(@RequestBody UserDTO userDTO);

}
