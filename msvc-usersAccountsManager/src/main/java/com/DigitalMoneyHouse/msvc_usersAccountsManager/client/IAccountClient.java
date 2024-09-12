package com.DigitalMoneyHouse.msvc_usersAccountsManager.client;


import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.AccountRegisteredResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-accounts", url = "http://localhost:8083/accounts")
public interface IAccountClient {

    @PostMapping("/create/{userId}")
    ResponseEntity<AccountRegisteredResponseDTO> createAccount(@PathVariable("userId") Long userId);


}
