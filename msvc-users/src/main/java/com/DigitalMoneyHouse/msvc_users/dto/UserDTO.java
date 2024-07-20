package com.DigitalMoneyHouse.msvc_users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserDTO {

private Long id;

        @NotBlank(message = "Name is mandatory")
        @Size(max = 100, message = "Name can have at most 100 characters")
        private String firstName;
        @NotBlank(message = "Name is mandatory")
        @Size(max = 100, message = "Name can have at most 100 characters")
        private String lastName;


        @NotBlank(message = "DNI is mandatory")
        @Pattern(regexp = "\\d+", message = "DNI must be numeric")
        @Size(min = 7, max = 8, message = "DNI must be between 7 and 8 digits")
        private String dni;

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email should be valid")
        @Size(max = 100, message = "Email can have at most 100 characters")
        private String email;

        @NotBlank(message = "Phone is mandatory")
        @Pattern(regexp = "\\d+", message = "Phone number must be numeric")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
        private String phone;

        @NotBlank(message = "Password is mandatory")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        private String password;


    }
