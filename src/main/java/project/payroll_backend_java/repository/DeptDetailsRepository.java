package project.payroll_backend_java.repository;

import project.payroll_backend_java.entity.DeptDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DeptDetailsRepository extends JpaRepository<DeptDetails, String> {

    // Find department by name
    Optional<DeptDetails> findBydName(String dName);

    // Check if department exists by name
    boolean existsBydName(String dName);

    // Custom query to find department with employee count
    @Query("SELECT d FROM DeptDetails d LEFT JOIN FETCH d.employees WHERE d.dId = :dId")
    Optional<DeptDetails> findByIdWithEmployees(@Param("dId") String dId);
}
