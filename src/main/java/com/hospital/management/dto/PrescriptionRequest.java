package com.hospital.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PrescriptionRequest(
        @NotNull(message = "Patient ID is required") Long patientId,
        @NotNull(message = "Doctor ID is required") Long doctorId,
        @NotBlank(message = "Diagnosis is required") String diagnosis,
        String notes,
        List<PrescriptionItemRequest> items
) {
}
