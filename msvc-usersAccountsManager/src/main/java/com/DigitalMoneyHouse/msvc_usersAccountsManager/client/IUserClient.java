package com.DigitalMoneyHouse.msvc_usersAccountsManager.client;


import com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.autModels.LoginRequestDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(name = "msvc-users", url = "http://localhost:8082/users")
public interface IUserClient {

    @PostMapping("/create")
    UserRegisteredResponseDTO createUser(@RequestBody UserDTO userDTO);

   // @GetMapping("/{id}")
    //UserResponseDTO getUserById(@PathVariable("id") Long id);

//Creados al agregar security
    @PostMapping("/validate")
    ResponseEntity<String> validateUser(@RequestBody LoginRequestDTO loginDTO);

    @GetMapping("/get/{id}")
    ResponseEntity<?> getUserById(@PathVariable Long id);


    @GetMapping("/email/{email}")
    ResponseEntity<UserEmailYPasswordDTO> getUserByEmail(@PathVariable("email") String email);

    /*
    @GetMapping("/email/{email}")
    ResponseEntity<UserRequestDTO> findByEmail(@PathVariable("email") String email);
     */

}
