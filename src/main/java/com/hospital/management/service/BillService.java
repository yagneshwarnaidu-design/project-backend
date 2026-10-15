package com.hospital.management.service;

import com.hospital.management.dto.BillRequest;
import com.hospital.management.entity.Bill;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.Payment;
import com.hospital.management.enums.BillStatus;
import com.hospital.management.enums.PaymentStatus;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.BillRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final PatientRepository patientRepository;
    private final PaymentRepository paymentRepository;

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
    }

    public Bill createBill(BillRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.patientId()));

        BigDecimal consultationFee = request.consultationFee() != null ? request.consultationFee() : BigDecimal.ZERO;
        BigDecimal otherCharges = request.otherCharges() != null ? request.otherCharges() : BigDecimal.ZERO;
        BigDecimal totalAmount = consultationFee.add(otherCharges);

        Bill bill = new Bill();
        bill.setBillNumber("BILL-" + System.currentTimeMillis());
        bill.setConsultationFee(consultationFee);
        bill.setOtherCharges(otherCharges);
        bill.setTotalAmount(totalAmount);
        bill.setStatus(request.status() != null ? BillStatus.valueOf(request.status().toUpperCase()) : BillStatus.PENDING);
        bill.setPatient(patient);
        return billRepository.save(bill);
    }

    public Payment createPayment(Long billId, String paymentMethod, BigDecimal amount) {
        Bill bill = getBillById(billId);

        Payment payment = new Payment();
        payment.setPaymentId("PAY-" + System.currentTimeMillis());
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setBill(bill);

        if (amount.compareTo(bill.getTotalAmount()) >= 0) {
            bill.setStatus(BillStatus.PAID);
        } else {
            bill.setStatus(BillStatus.PARTIALLY_PAID);
        }
        billRepository.save(bill);

        return paymentRepository.save(payment);
    }
}
