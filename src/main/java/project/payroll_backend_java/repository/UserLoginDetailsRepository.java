package project.payroll_backend_java.repository;

import project.payroll_backend_java.entity.UserLoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface UserLoginDetailsRepository extends JpaRepository<UserLoginDetails, String> {

    // Find user by username
    Optional<UserLoginDetails> findByUserName(String userName);

    // Check if username exists
    boolean existsByUserName(String userName);

    // Find user by username and password (for login validation)
    Optional<UserLoginDetails> findByUserNameAndUserPassword(String userName, String userPassword);

    // Update password for a user
    @Modifying
    @Transactional
    @Query("UPDATE UserLoginDetails u SET u.userPassword = :newPassword WHERE u.userName = :userName")
    int updatePassword(@Param("userName") String userName, @Param("newPassword") String newPassword);
}
