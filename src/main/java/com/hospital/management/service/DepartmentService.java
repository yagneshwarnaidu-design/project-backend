package com.hospital.management.service;

import com.hospital.management.dto.DepartmentRequest;
import com.hospital.management.entity.Department;
import com.hospital.management.exception.ConflictException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    public Department createDepartment(DepartmentRequest request) {
        if (departmentRepository.findByName(request.name()).isPresent()) {
            throw new ConflictException("Department already exists: " + request.name());
        }

        Department department = new Department();
        department.setName(request.name());
        department.setDescription(request.description());
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Long id, DepartmentRequest request) {
        Department department = getDepartmentById(id);
        department.setName(request.name());
        department.setDescription(request.description());
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        departmentRepository.delete(department);
    }
}
