package com.hospital.management.repository;

import com.hospital.management.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByDoctorId(String doctorId);

    Optional<Doctor> findByUserId(Long userId);

    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);

    List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
