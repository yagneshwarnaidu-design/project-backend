package com.hospital.management.controller;

import com.hospital.management.dto.BillRequest;
import com.hospital.management.entity.Bill;
import com.hospital.management.entity.Payment;
import com.hospital.management.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<Bill> createBill(@Valid @RequestBody BillRequest request) {
        return ResponseEntity.ok(billService.createBill(request));
    }

    @PostMapping("/{billId}/payments")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    public ResponseEntity<Payment> createPayment(
            @PathVariable Long billId,
            @RequestParam String paymentMethod,
            @RequestParam BigDecimal amount
    ) {
        return ResponseEntity.ok(billService.createPayment(billId, paymentMethod, amount));
    }
}
