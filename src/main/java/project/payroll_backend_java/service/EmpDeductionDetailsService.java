package project.payroll_backend_java.service;

import project.payroll_backend_java.entity.EmpDeductionDetails;
import java.util.List;
import java.util.Optional;

public interface EmpDeductionDetailsService {

    // Basic CRUD operations
    EmpDeductionDetails saveDeductionDetails(EmpDeductionDetails deductionDetails);
    Optional<EmpDeductionDetails> getDeductionDetailsById(String eId);
    List<EmpDeductionDetails> getAllDeductionDetails();
    EmpDeductionDetails updateDeductionDetails(EmpDeductionDetails deductionDetails);
    void deleteDeductionDetails(String eId);

    // Business logic methods
    List<EmpDeductionDetails> getEmployeesWithLeaveDeductions();
    Integer calculateTotalDeductions(String eId);
}
