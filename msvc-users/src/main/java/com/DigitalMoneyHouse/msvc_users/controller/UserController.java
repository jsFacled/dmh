package com.DigitalMoneyHouse.msvc_users.controller;


import com.DigitalMoneyHouse.msvc_users.dto.*;
import com.DigitalMoneyHouse.msvc_users.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/hello")
    public ResponseEntity<?> hello()    {
        System.out.println(" -  -  -  ** -  -  - // -  - Desde ms-users/hello: dice Hola mundo!! /*  - - - - // ** // **.");
        return ResponseEntity.ok(" - - - ¡¡Hola Mundo!! Soy ms-users!!!  -  -  - ");
    }


    @PostMapping("/create")
    public ResponseEntity<UserRegisteredResponseDTO> createUser(@Validated @RequestBody UserDTO userDTO) {
        UserRegisteredResponseDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO userDTO = userService.getUserById(id);
        if (userDTO != null) {
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserEmailYPasswordDTO> getUserByEmail(@PathVariable String email) {

        UserEmailYPasswordDTO uEyPdto = userService.getUserByEmail(email);
        if (uEyPdto != null) {
            return ResponseEntity.ok(uEyPdto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/userIdFromEmail")
    public ResponseEntity<?> getUserIdByEmail(@RequestParam String email) {
        Optional<Long> userId = userService.getUserIdByEmail(email).describeConstable();

        if (userId.isPresent()) {
            return ResponseEntity.ok(userId.get());
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
/*
   @GetMapping("/email")
   //Se solicita con Params --> http://localhost:8080/users/email?email=maub@gmail.com
   public ResponseEntity<UserResponseDTO> getUserByEmail(@RequestParam String email) {

       UserResponseDTO urDTO = userService.getUserByEmail(email);
        if (urDTO != null) {
            return ResponseEntity.ok(urDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
*/

    @GetMapping("all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> patchUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        UserDTO updatedUser = userService.patchUser(id, updates);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PostMapping("/validate")
    public ResponseEntity<String> validateUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            boolean isAuthenticated = userService.validarSinEncriptar(loginRequestDTO);
            if (isAuthenticated) {
                return ResponseEntity.ok("User authenticated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    }
