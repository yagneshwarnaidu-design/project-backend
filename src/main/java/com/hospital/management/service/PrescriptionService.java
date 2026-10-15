package com.hospital.management.service;

import com.hospital.management.dto.PrescriptionItemRequest;
import com.hospital.management.dto.PrescriptionRequest;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.Prescription;
import com.hospital.management.entity.PrescriptionItem;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public List<Prescription> getPrescriptionsForPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    public Prescription createPrescription(PrescriptionRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.patientId()));
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.doctorId()));

        Prescription prescription = new Prescription();
        prescription.setPrescriptionId("PRS-" + System.currentTimeMillis());
        prescription.setDiagnosis(request.diagnosis());
        prescription.setNotes(request.notes());
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);

        List<PrescriptionItem> items = new ArrayList<>();
        if (request.items() != null) {
            for (PrescriptionItemRequest itemRequest : request.items()) {
                PrescriptionItem item = new PrescriptionItem();
                item.setMedicineName(itemRequest.medicineName());
                item.setDosage(itemRequest.dosage());
                item.setFrequency(itemRequest.frequency());
                item.setDuration(itemRequest.duration());
                item.setInstructions(itemRequest.instructions());
                item.setPrescription(prescription);
                items.add(item);
            }
        }

        prescription.setItems(items);
        return prescriptionRepository.save(prescription);
    }
}
