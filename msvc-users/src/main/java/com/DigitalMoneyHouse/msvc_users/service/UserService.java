package com.DigitalMoneyHouse.msvc_users.service;

import com.DigitalMoneyHouse.msvc_users.controller.UserController;
import com.DigitalMoneyHouse.msvc_users.dto.*;
import com.DigitalMoneyHouse.msvc_users.entity.User;
import com.DigitalMoneyHouse.msvc_users.exceptions.UserAlreadyExistsException;
import com.DigitalMoneyHouse.msvc_users.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserRegisteredResponseDTO createUser(UserDTO userDTO) {
        // Verificar si el usuario ya existe
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            logger.error("Error al crear el usuario con el email " + userDTO.getEmail() + ": ya está registrado.");
            throw new UserAlreadyExistsException("El usuario con el email " + userDTO.getEmail() + " ya está registrado.");
        }

        // Crear y guardar el nuevo usuario
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDni(userDTO.getDni());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setPassword(encryptPassword(userDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return new UserRegisteredResponseDTO(savedUser.getId(), "ok");
    }


    public UserResponseDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toUserResponseDTO).orElse(null);
    }

    public UserResponseDTO getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            System.out.println(" * * * * Usuario encontrado: " + user.get());
        } else {
            System.out.println(" / * * * * * Usuario no encontrado.");
        }
        return user.map(userMapper::toUserResponseDTO).orElse(null);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setDni(userDTO.getDni());
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setUpdatedAt(LocalDateTime.now());

            User updatedUser = userRepository.save(user);
            return userMapper.toUserDTO(updatedUser);
        }
        return null;
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private String encryptPassword(String password) {
        passwordEncoder.encode(password);
        return password;
    }


    //En vez de devolver un ResponseEntity directamente se responde con boolean.
    public boolean validarSinEncriptar(LoginRequestDTO loginRequestDTO) {
        System.out.println("* * Desde ms-users * * en UserService INICIO en validarSinEncriptar()");

        // Trae el usuario de la base de datos (id, email, contraseña)
        User userEntity = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("* * UserService * *  validarSinEncriptar el userprueba que devuelve base de datos es : " + userEntity);

        // Matchea la contraseña del DTO con la de la base de datos (inseguro)
        return loginRequestDTO.getPassword().equals(userEntity.getPassword());
    }

}
