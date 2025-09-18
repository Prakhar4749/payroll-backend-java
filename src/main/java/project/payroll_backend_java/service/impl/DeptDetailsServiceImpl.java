package project.payroll_backend_java.service.impl;

import project.payroll_backend_java.entity.DeptDetails;
import project.payroll_backend_java.repository.DeptDetailsRepository;
import project.payroll_backend_java.service.DeptDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DeptDetailsServiceImpl implements DeptDetailsService {

    @Autowired
    private DeptDetailsRepository deptDetailsRepository;

    @Override
    public DeptDetails saveDepartment(DeptDetails department) {
        if (department.getDId() == null || department.getDId().trim().isEmpty()) {
            throw new IllegalArgumentException("Department ID cannot be null or empty");
        }
        if (department.getDName() == null || department.getDName().trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be null or empty");
        }

        return deptDetailsRepository.save(department);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeptDetails> getDepartmentById(String dId) {
        return deptDetailsRepository.findById(dId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeptDetails> getAllDepartments() {
        List<DeptDetails> departments = deptDetailsRepository.findAll();
        departments.sort((d1, d2) -> d1.getDId().compareTo(d2.getDId()));
        return departments;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeptDetails> getAllDepartmentsOrderedById() {
        return getAllDepartments(); // Simply call the existing method
    }

    @Override
    public DeptDetails updateDepartment(DeptDetails department) {
        if (!deptDetailsRepository.existsById(department.getDId())) {
            throw new IllegalArgumentException("Department not found with ID: " + department.getDId());
        }
        return deptDetailsRepository.save(department);
    }

    @Override
    public void deleteDepartment(String dId) {
        if (!deptDetailsRepository.existsById(dId)) {
            throw new IllegalArgumentException("Department not found with ID: " + dId);
        }
        deptDetailsRepository.deleteById(dId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeptDetails> getDepartmentByName(String dName) {
        // Use standard JPA method instead of custom one
        List<DeptDetails> departments = deptDetailsRepository.findAll();
        return departments.stream()
                .filter(dept -> dept.getDName().equals(dName))
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String dName) {
        // Use standard approach instead of custom repository method
        List<DeptDetails> departments = deptDetailsRepository.findAll();
        return departments.stream()
                .anyMatch(dept -> dept.getDName().equals(dName));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeptDetails> getDepartmentWithEmployees(String dId) {
        // For now, just return the department without employees
        // You can enhance this later when employee relationships are fully set up
        return getDepartmentById(dId);
    }
}
