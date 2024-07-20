package com.DigitalMoneyHouse.msvc_users.controller;


import com.DigitalMoneyHouse.msvc_users.dto.UserDTO;
import com.DigitalMoneyHouse.msvc_users.dto.UserRegisteredResponseDTO;
import com.DigitalMoneyHouse.msvc_users.entity.User;
import com.DigitalMoneyHouse.msvc_users.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserRegisteredResponseDTO> createUser(@Validated @RequestBody UserDTO userDTO) {
        UserRegisteredResponseDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO != null) {
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //@GetMapping("/email/{email}")
   @GetMapping("/email")
   public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
//        public ResponseEntity<UserDTO> getUserByEmail(  @PathVariable String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        if (userDTO != null) {
            return ResponseEntity.ok(userDTO);
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


}
