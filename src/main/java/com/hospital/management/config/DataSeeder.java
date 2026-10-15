package com.hospital.management.config;

import com.hospital.management.entity.Department;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.Role;
import com.hospital.management.entity.User;
import com.hospital.management.enums.ERole;
import com.hospital.management.repository.DepartmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.RoleRepository;
import com.hospital.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        seedRoles();
        seedSampleData();
    }

    private void seedRoles() {
        for (ERole role : ERole.values()) {
            roleRepository.findByName(role).orElseGet(() -> roleRepository.save(new Role(null, role)));
        }
    }

    private void seedSampleData() {
        if (userRepository.count() > 0) {
            return;
        }

        Role adminRole = roleRepository.findByName(ERole.ADMIN).orElseThrow();
        Role doctorRole = roleRepository.findByName(ERole.DOCTOR).orElseThrow();
        Role patientRole = roleRepository.findByName(ERole.PATIENT).orElseThrow();

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@hospital.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("System");
        admin.setLastName("Admin");
        admin.setPhone("9876543210");
        admin.setEnabled(true);
        admin.setRoles(Set.of(adminRole));
        userRepository.save(admin);

        User doctorUser = new User();
        doctorUser.setUsername("doctor");
        doctorUser.setEmail("doctor@hospital.com");
        doctorUser.setPassword(passwordEncoder.encode("doctor123"));
        doctorUser.setFirstName("Aisha");
        doctorUser.setLastName("Patel");
        doctorUser.setPhone("9123456780");
        doctorUser.setEnabled(true);
        doctorUser.setRoles(Set.of(doctorRole));
        userRepository.save(doctorUser);

        User patientUser = new User();
        patientUser.setUsername("patient");
        patientUser.setEmail("patient@hospital.com");
        patientUser.setPassword(passwordEncoder.encode("patient123"));
        patientUser.setFirstName("Rahul");
        patientUser.setLastName("Sharma");
        patientUser.setPhone("9988776655");
        patientUser.setEnabled(true);
        patientUser.setRoles(Set.of(patientRole));
        userRepository.save(patientUser);

        Department cardiology = departmentRepository.save(new Department(null, "Cardiology", "Heart care specialists"));
        Department neurology = departmentRepository.save(new Department(null, "Neurology", "Neurological treatment and rehabilitation"));

        Doctor doctor = new Doctor();
        doctor.setDoctorId("DOC-1001");
        doctor.setFirstName("Aisha");
        doctor.setLastName("Patel");
        doctor.setEmail("doctor@hospital.com");
        doctor.setPhone("9123456780");
        doctor.setSpecialization("Cardiology");
        doctor.setExperience(12);
        doctor.setAvailability("Mon-Fri 9:00 AM - 5:00 PM");
        doctor.setDepartment(cardiology);
        doctor.setUser(doctorUser);
        doctorRepository.save(doctor);

        Patient patient = new Patient();
        patient.setPatientId("PAT-1001");
        patient.setFirstName("Rahul");
        patient.setLastName("Sharma");
        patient.setDateOfBirth(LocalDate.of(1992, 5, 18));
        patient.setGender("Male");
        patient.setPhone("9988776655");
        patient.setEmail("patient@hospital.com");
        patient.setAddress("123 Wellness Avenue, Hyderabad");
        patient.setMedicalHistory("No major previous illness");
        patient.setUser(patientUser);
        patientRepository.save(patient);

        Department general = departmentRepository.save(new Department(null, "General Medicine", "Care for common illnesses"));
        Department pediatrics = departmentRepository.save(new Department(null, "Pediatrics", "Child health care"));
        departmentRepository.save(general);
        departmentRepository.save(pediatrics);
    }
}
