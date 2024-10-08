package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.userDetails;

import com.DigitalMoneyHouse.msvc_usersAccountsManager.client.IUserClient;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserEmailYPasswordDTO;
import com.DigitalMoneyHouse.msvc_usersAccountsManager.dto.UserRequestDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    // Repositorio del microservicio Users para acceder a UserEntity
    private final IUserClient userClient;

    public CustomUserDetailsService(IUserClient userClient) {
        this.userClient = userClient;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEmailYPasswordDTO uEyPdto = userClient.getUserByEmail(username).getBody(); // Busca por email, que se usa como username
        if (uEyPdto == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(uEyPdto.getEmail(), uEyPdto.getPassword(), new ArrayList<>()); // Pasa un conjunto vacío de authorities al no usar roles
    }
}
