package project.payroll_backend_java.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import project.payroll_backend_java.entity.DeptDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface DeptDetailsRepository extends JpaRepository<DeptDetails, String> {



   @Query(value="SELECT * FROM dept_details ORDER BY d_id",nativeQuery = true)
    List<DeptDetails> findAllDept();

   @Query(value="SELECT * FROM dept_details Where d_id = :d_id",nativeQuery = true)
    DeptDetails findDeptById(@Param("d_id") String d_id);

    @Modifying
    @Transactional
    @Query(value="Delete FROM dept_details Where d_id = :d_id",nativeQuery = true)
    int deleteDeptById(@Param("d_id") String d_id);

    @Modifying
    @Transactional
    @Query(value="INSERT INTO dept_details (d_id,d_name) values( :d_id, :d_name);",nativeQuery = true)
    int addNewDept(@Param("d_id") String d_id, @Param("d_name") String d_name);

    @Modifying
    @Transactional
    @Query(value="UPDATE dept_details SET d_id = :new_d_id, d_name = :new_d_name  WHERE d_id = :d_id",nativeQuery = true)
    int addNewDept(@Param("d_id") String d_id, @Param("new_d_name") String new_d_name, @Param("new_d_id") String new_d_id);

    @Query(
            value = "SELECT " +
                    "SUM(CASE WHEN UPPER(REPLACE(d_id, ' ', '')) = UPPER(REPLACE(:d_id, ' ', '')) THEN 1 ELSE 0 END) AS d_id_count, " +
                    "SUM(CASE WHEN UPPER(REPLACE(d_name, ' ', '')) = UPPER(REPLACE(:d_name, ' ', '')) THEN 1 ELSE 0 END) AS d_name_count " +
                    "FROM dept_details",
            nativeQuery = true
    )
    Map<String, Object> checkIsitPresent(@Param("d_id") String d_id, @Param("d_name") String d_name);
}


