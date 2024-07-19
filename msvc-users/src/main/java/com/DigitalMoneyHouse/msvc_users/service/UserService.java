package com.DigitalMoneyHouse.msvc_users.service;

import com.DigitalMoneyHouse.msvc_users.dto.UserDTO;
import com.DigitalMoneyHouse.msvc_users.entity.User;
import com.DigitalMoneyHouse.msvc_users.repository.IUserRepository;

import java.time.LocalDateTime;

public class UserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();

        user.setName(userDTO.getName());
        user.setDni(userDTO.getDni());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setPassword(encryptPassword(userDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(this::convertToDTO).orElse(null);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return convertToDTO(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(userDTO.getName());
            user.setDni(userDTO.getDni());
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setUpdatedAt(LocalDateTime.now());

            User updatedUser = userRepository.save(user);
            return convertToDTO(updatedUser);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private String encryptPassword(String password) {
        // Implementar lógica de encriptación de contraseña
        return password;
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setDni(user.getDni());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        return userDTO;
    }
}
