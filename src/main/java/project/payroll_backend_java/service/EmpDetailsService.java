package project.payroll_backend_java.service;

import project.payroll_backend_java.entity.EmpDetails;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmpDetailsService {

    // Basic CRUD operations
    EmpDetails saveEmployee(EmpDetails employee);
    Optional<EmpDetails> getEmployeeById(String eId);
    // Add this method back
    Optional<EmpDetails> getEmployeeWithAllDetails(String eId);
    List<EmpDetails> getAllEmployees();
    EmpDetails updateEmployee(EmpDetails employee);
    void deleteEmployee(String eId);

    // Business logic methods
    List<EmpDetails> getEmployeesByDepartment(String dId);
    List<EmpDetails> getEmployeesByDesignation(String designation);
    List<EmpDetails> getEmployeesByGender(String gender);
    List<EmpDetails> searchEmployeesByName(String name);
    List<EmpDetails> getEmployeesJoinedBetween(LocalDate startDate, LocalDate endDate);

    // Validation methods
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(Long mobileNumber);


    Long getEmployeeCountByDepartment(String dId);
}
