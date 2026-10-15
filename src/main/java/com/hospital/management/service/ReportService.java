package com.hospital.management.service;

import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.Bill;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.enums.AppointmentStatus;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.BillRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;

    public Map<String, Object> getDashboardSummary() {
        long patients = patientRepository.count();
        long doctors = doctorRepository.count();
        long appointments = appointmentRepository.count();
        long pending = appointmentRepository.countByStatus(AppointmentStatus.PENDING);
        BigDecimal revenue = billRepository.findAll().stream()
                .map(Bill::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalPatients", patients);
        summary.put("totalDoctors", doctors);
        summary.put("totalAppointments", appointments);
        summary.put("pendingAppointments", pending);
        summary.put("totalRevenue", revenue);
        return summary;
    }

    public Map<String, Object> getReportsByDateRange(LocalDate fromDate, LocalDate toDate) {
        List<Appointment> appointments = appointmentRepository.findByAppointmentDateBetween(fromDate, toDate);
        long patients = patientRepository.count();
        long doctors = doctorRepository.count();
        BigDecimal revenue = billRepository.findAll().stream()
                .map(Bill::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> report = new HashMap<>();
        report.put("fromDate", fromDate);
        report.put("toDate", toDate);
        report.put("appointments", appointments.size());
        report.put("totalPatients", patients);
        report.put("totalDoctors", doctors);
        report.put("totalRevenue", revenue);
        return report;
    }
}
