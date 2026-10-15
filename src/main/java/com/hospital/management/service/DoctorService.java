package com.hospital.management.service;

import com.hospital.management.dto.DoctorRequest;
import com.hospital.management.entity.Department;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.User;
import com.hospital.management.exception.ConflictException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DepartmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    public List<Doctor> searchDoctors(String query) {
        if (query == null || query.isBlank()) {
            return doctorRepository.findAll();
        }
        return doctorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
    }

    public Doctor createDoctor(DoctorRequest request) {
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.departmentId()));

        User user = userRepository.findByEmail(request.email()).orElse(null);
        if (user == null) {
            throw new ResourceNotFoundException("User account for this doctor not found. Please register the account first.");
        }

        if (doctorRepository.findByUserId(user.getId()).isPresent()) {
            throw new ConflictException("This user is already assigned to a doctor profile.");
        }

        Doctor doctor = new Doctor();
        doctor.setDoctorId("DOC-" + System.currentTimeMillis());
        doctor.setFirstName(request.firstName());
        doctor.setLastName(request.lastName());
        doctor.setEmail(request.email());
        doctor.setPhone(request.phone());
        doctor.setSpecialization(request.specialization());
        doctor.setExperience(request.experience() != null ? request.experience() : 0);
        doctor.setAvailability(request.availability());
        doctor.setDepartment(department);
        doctor.setUser(user);
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = getDoctorById(id);
        if (request.departmentId() != null) {
            Department department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.departmentId()));
            doctor.setDepartment(department);
        }
        doctor.setFirstName(request.firstName());
        doctor.setLastName(request.lastName());
        doctor.setEmail(request.email());
        doctor.setPhone(request.phone());
        doctor.setSpecialization(request.specialization());
        doctor.setExperience(request.experience() != null ? request.experience() : doctor.getExperience());
        doctor.setAvailability(request.availability());

        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        Doctor doctor = getDoctorById(id);
        doctorRepository.delete(doctor);
    }
}
