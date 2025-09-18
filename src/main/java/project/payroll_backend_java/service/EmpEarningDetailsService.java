package project.payroll_backend_java.service;

import project.payroll_backend_java.entity.EmpEarningDetails;
import java.util.List;
import java.util.Optional;

public interface EmpEarningDetailsService {

    // Basic CRUD operations
    EmpEarningDetails saveEarningDetails(EmpEarningDetails earningDetails);
    Optional<EmpEarningDetails> getEarningDetailsById(String eId);
    List<EmpEarningDetails> getAllEarningDetails();
    EmpEarningDetails updateEarningDetails(EmpEarningDetails earningDetails);
    void deleteEarningDetails(String eId);

    // Business logic methods
    List<EmpEarningDetails> getEmployeesBySalaryRange(Integer minSalary, Integer maxSalary);
    Integer calculateTotalEarnings(String eId);
    Double getAverageBasicSalary();
}
