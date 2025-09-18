package project.payroll_backend_java.repository;

import project.payroll_backend_java.entity.EmpBankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpBankDetailsRepository extends JpaRepository<EmpBankDetails, String> {

    // Find by bank account number
    Optional<EmpBankDetails> findByeBankAccNumber(Long eBankAccNumber);

    // Find by PAN number
    Optional<EmpBankDetails> findByePanNumber(String ePanNumber);

    // Find by bank name
    List<EmpBankDetails> findByeBankName(String eBankName);

    // Find by IFSC code
    List<EmpBankDetails> findByeBankIFSC(String eBankIFSC);

    // Check if bank account exists
    boolean existsByeBankAccNumber(Long eBankAccNumber);

    // Check if PAN exists
    boolean existsByePanNumber(String ePanNumber);
}
