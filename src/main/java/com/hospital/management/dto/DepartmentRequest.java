package com.hospital.management.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequest(
        @NotBlank(message = "Department name is required") String name,
        String description
) {
}
