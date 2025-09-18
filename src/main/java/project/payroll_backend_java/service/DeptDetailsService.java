package project.payroll_backend_java.service;

import project.payroll_backend_java.entity.DeptDetails;
import java.util.List;
import java.util.Optional;

public interface DeptDetailsService {

    // Basic CRUD operations
    DeptDetails saveDepartment(DeptDetails department);
    Optional<DeptDetails> getDepartmentById(String dId);
    List<DeptDetails> getAllDepartments();
    List<DeptDetails> getAllDepartmentsOrderedById(); // ADD THIS LINE
    DeptDetails updateDepartment(DeptDetails department);
    void deleteDepartment(String dId);

    // Business logic methods
    Optional<DeptDetails> getDepartmentByName(String dName);
    boolean existsByName(String dName);
    Optional<DeptDetails> getDepartmentWithEmployees(String dId);
}
