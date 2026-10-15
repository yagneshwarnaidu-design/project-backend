package com.hospital.management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PatientRequest(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotNull(message = "Date of birth is required") LocalDate dateOfBirth,
        String gender,
        String phone,
        @Email(message = "Email must be valid") String email,
        String address,
        String medicalHistory
) {
}
