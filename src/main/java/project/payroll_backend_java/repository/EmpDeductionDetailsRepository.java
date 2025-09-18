package project.payroll_backend_java.repository;

import project.payroll_backend_java.entity.EmpDeductionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmpDeductionDetailsRepository extends JpaRepository<EmpDeductionDetails, String> {

    // Find employees with leave deductions
    List<EmpDeductionDetails> findByLeaveDaysGreaterThan(Integer leaveDays);

    // Find employees with specific deduction types
    List<EmpDeductionDetails> findByDeductionCPFGreaterThan(Integer amount);
    List<EmpDeductionDetails> findByGisGreaterThan(Integer amount);
    List<EmpDeductionDetails> findByHouseRentGreaterThan(Integer amount);

    // Calculate total deductions for an employee
    @Query("SELECT (COALESCE(d.leaveDeductionAmount, 0) + " +
            "COALESCE(d.deductionCPF, 0) + " +
            "COALESCE(d.gis, 0) + " +
            "COALESCE(d.houseRent, 0) + " +
            "COALESCE(d.waterCharges, 0) + " +
            "COALESCE(d.electricityCharges, 0) + " +
            "COALESCE(d.vehicleDeduction, 0) + " +
            "COALESCE(d.hbLoan, 0) + " +
            "COALESCE(d.gpfLoan, 0) + " +
            "COALESCE(d.festivalLoan, 0) + " +
            "COALESCE(d.grainCharges, 0) + " +
            "COALESCE(d.bankAdvance, 0) + " +
            "COALESCE(d.advance, 0) + " +
            "COALESCE(d.rgpvAdvance, 0) + " +
            "COALESCE(d.incomeTax, 0) + " +
            "COALESCE(d.professionalTax, 0)) " +
            "FROM EmpDeductionDetails d WHERE d.eId = :eId")
    Integer calculateTotalDeductions(@Param("eId") String eId);
}
