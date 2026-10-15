package com.hospital.management.repository;

import com.hospital.management.entity.Bill;
import com.hospital.management.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBillNumber(String billNumber);

    List<Bill> findByPatientId(Long patientId);

    long countByStatus(BillStatus status);
}
