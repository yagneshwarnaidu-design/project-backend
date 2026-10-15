package com.hospital.management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DoctorRequest(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @Email(message = "Email must be valid") String email,
        String phone,
        String specialization,
        Integer experience,
        String availability,
        Long departmentId
) {
}
