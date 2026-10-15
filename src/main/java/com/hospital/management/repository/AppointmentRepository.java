package com.hospital.management.repository;

import com.hospital.management.entity.Appointment;
import com.hospital.management.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByAppointmentId(String appointmentId);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusNot(Long doctorId, LocalDate date, LocalTime time,
                                                                               AppointmentStatus status);

    long countByStatus(AppointmentStatus status);

    List<Appointment> findByAppointmentDateBetween(LocalDate startDate, LocalDate endDate);
}
