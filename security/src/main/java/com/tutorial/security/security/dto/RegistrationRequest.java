package com.tutorial.security.security.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegistrationRequest(

        @NotBlank(message = "First name is required")
        @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
        String firstName, //TODO: allow international characters in names like O’Connor, José, Mary Anne

        @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
        @NotBlank(message = "Last name is required")
        String lastName,

        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password
) {}
