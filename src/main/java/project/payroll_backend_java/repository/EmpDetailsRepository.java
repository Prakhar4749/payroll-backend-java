package project.payroll_backend_java.repository;

import project.payroll_backend_java.entity.EmpDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface EmpDetailsRepository extends JpaRepository<EmpDetails, String> {

    // Basic queries
    boolean existsByeEmail(String email);
    boolean existsByeMobileNumber(Long mobileNumber);

    // NOW THIS WILL WORK - because relationships are properly defined
    @Query("SELECT e FROM EmpDetails e " +
            "LEFT JOIN FETCH e.department " +
            "LEFT JOIN FETCH e.bankDetails " +
            "LEFT JOIN FETCH e.deductionDetails " +
            "LEFT JOIN FETCH e.earningDetails " +
            "WHERE e.eId = :eId")
    Optional<EmpDetails> findByIdWithAllDetails(@Param("eId") String eId);

    // Other useful methods
    @Query("SELECT e FROM EmpDetails e WHERE e.department.dId = :dId")
    List<EmpDetails> findByDepartment_dId(@Param("dId") String dId);
}
