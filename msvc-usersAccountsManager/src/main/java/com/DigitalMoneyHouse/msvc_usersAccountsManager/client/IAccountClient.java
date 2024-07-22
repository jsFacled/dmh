package com.DigitalMoneyHouse.msvc_usersAccountsManager.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-accounts", url = "http://localhost:8082/account")
public interface IAccountClient {

    @PostMapping("/create/{userId}")
    String createAccount(@PathVariable("userId") Long userId);
}
