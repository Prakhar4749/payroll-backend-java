package project.payroll_backend_java.service;

import project.payroll_backend_java.entity.EmpBankDetails;
import java.util.List;
import java.util.Optional;

public interface EmpBankDetailsService {

    // Basic CRUD operations
    EmpBankDetails saveBankDetails(EmpBankDetails bankDetails);
    Optional<EmpBankDetails> getBankDetailsById(String eId);
    List<EmpBankDetails> getAllBankDetails();
    EmpBankDetails updateBankDetails(EmpBankDetails bankDetails);
    void deleteBankDetails(String eId);

    // Business logic methods
    Optional<EmpBankDetails> getBankDetailsByAccountNumber(Long accountNumber);
    Optional<EmpBankDetails> getBankDetailsByPanNumber(String panNumber);
    List<EmpBankDetails> getBankDetailsByBankName(String bankName);
    boolean existsByAccountNumber(Long accountNumber);
    boolean existsByPanNumber(String panNumber);
}
