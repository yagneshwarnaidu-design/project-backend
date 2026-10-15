package com.hospital.management.dto;

import com.hospital.management.enums.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequest(
        @NotNull(message = "Patient ID is required") Long patientId,
        @NotNull(message = "Doctor ID is required") Long doctorId,
        @NotNull(message = "Appointment date is required") @Future(message = "Appointment date must be in the future") LocalDate appointmentDate,
        @NotNull(message = "Appointment time is required") LocalTime appointmentTime,
        AppointmentStatus status,
        String notes
) {
}
