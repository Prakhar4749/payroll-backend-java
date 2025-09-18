package project.payroll_backend_java.repository;

import project.payroll_backend_java.entity.SalaryArchive;
import project.payroll_backend_java.entity.SalaryArchiveId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalaryArchiveRepository extends JpaRepository<SalaryArchive, SalaryArchiveId> {

    // Find salary records for a specific employee
    List<SalaryArchive> findByeId(String eId);

    // Find salary records for a specific month and year
    List<SalaryArchive> findBySalaryMonthAndSalaryYear(String salaryMonth, Integer salaryYear);

    // Find salary records for a specific year
    List<SalaryArchive> findBySalaryYear(Integer salaryYear);

    // Check if salary exists for employee in specific month/year
    boolean existsByeIdAndSalaryMonthAndSalaryYear(String eId, String salaryMonth, Integer salaryYear);
}
