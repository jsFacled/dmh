package com.DigitalMoneyHouse.msvc_usersAccountsManager.auth.userDetails;

import com.DigitalMoneyHouse.msvc_usersAccountsManager.client.IUserClient;
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
        UserRequestDTO user = userClient.findByEmail(username).getBody(); // Busca por email, que usas como username
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>()); // Pasa un conjunto vacío de authorities al no usar roles
    }
}
