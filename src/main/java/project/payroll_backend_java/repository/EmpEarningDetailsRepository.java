package project.payroll_backend_java.repository;

import project.payroll_backend_java.entity.EmpEarningDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmpEarningDetailsRepository extends JpaRepository<EmpEarningDetails, String> {

    // Find by basic salary range
    List<EmpEarningDetails> findByBasicSalaryBetween(Integer minSalary, Integer maxSalary);

    // Find employees with specific allowances
    List<EmpEarningDetails> findByHraGreaterThan(Integer amount);
    List<EmpEarningDetails> findByDaGreaterThan(Integer amount);
    List<EmpEarningDetails> findByMedicalGreaterThan(Integer amount);

    // Find by basic salary greater than
    List<EmpEarningDetails> findByBasicSalaryGreaterThan(Integer basicSalary);

    // Calculate total earnings for an employee
    @Query("SELECT (COALESCE(e.basicSalary, 0) + " +
            "COALESCE(e.specialPay, 0) + " +
            "COALESCE(e.dearnessAllowance, 0) + " +
            "COALESCE(e.da, 0) + " +
            "COALESCE(e.ada, 0) + " +
            "COALESCE(e.interimRelief, 0) + " +
            "COALESCE(e.hra, 0) + " +
            "COALESCE(e.cca, 0) + " +
            "COALESCE(e.conveyance, 0) + " +
            "COALESCE(e.medical, 0) + " +
            "COALESCE(e.washingAllowance, 0) + " +
            "COALESCE(e.bdp, 0) + " +
            "COALESCE(e.arrears, 0)) " +
            "FROM EmpEarningDetails e WHERE e.eId = :eId")
    Integer calculateTotalEarnings(@Param("eId") String eId);
}
