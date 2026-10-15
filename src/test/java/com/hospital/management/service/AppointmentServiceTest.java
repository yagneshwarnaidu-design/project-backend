package com.hospital.management.service;

import com.hospital.management.dto.AppointmentRequest;
import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.enums.AppointmentStatus;
import com.hospital.management.exception.ConflictException;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void shouldThrowConflictWhenDoctorAlreadyBooked() {
        Patient patient = new Patient();
        patient.setId(1L);

        Doctor doctor = new Doctor();
        doctor.setId(2L);

        AppointmentRequest request = new AppointmentRequest(
                1L,
                2L,
                LocalDate.now().plusDays(2),
                LocalTime.of(10, 30),
                AppointmentStatus.PENDING,
                "Follow up"
        );

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
                2L, request.appointmentDate(), request.appointmentTime(), AppointmentStatus.CANCELLED
        )).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class,
                () -> appointmentService.createAppointment(request));

        assertEquals("Doctor is already booked at the selected time", exception.getMessage());
    }
}
