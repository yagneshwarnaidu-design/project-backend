package com.hospital.management.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BillRequest(
        @NotNull(message = "Patient ID is required") Long patientId,
        BigDecimal consultationFee,
        BigDecimal otherCharges,
        String status
) {
}
