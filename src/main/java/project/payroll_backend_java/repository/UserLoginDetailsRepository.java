package project.payroll_backend_java.repository;

import project.payroll_backend_java.entity.UserLoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserLoginDetailsRepository extends JpaRepository<UserLoginDetails, String> {

    // Find user by username
    @Query(value = "SELECT * FROM user_login_details WHERE user_name = :user_name", nativeQuery = true)
    Optional<UserLoginDetails> findByUserName(@Param("user_name") String user_name);

    // Get all users (ordered list)
    @Query(value = "SELECT * FROM user_login_details ORDER BY user_name", nativeQuery = true)
    List<UserLoginDetails> findAllUsers();

    // Check if username exists - returns count
    @Query(value = "SELECT COUNT(*) AS user_name FROM user_login_details WHERE user_name = :user_name", nativeQuery = true)
    int countByUserName(@Param("user_name") String user_name);

    // Check if username exists - boolean
//    boolean existsByUserName(String user_name);

    // Register new user (INSERT)
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_login_details (user_name, user_password) VALUES (:user_name, :userPassword)", nativeQuery = true)
    int registerUser(@Param("user_name") String user_name, @Param("userPassword") String userPassword);

    // Update password for a user (change password)
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_login_details SET user_password = :newPassword WHERE user_name = :user_name", nativeQuery = true)
    int updatePassword(@Param("user_name") String user_name, @Param("newPassword") String newPassword);

    // Update username (change username)
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_login_details SET user_name = :newUserName WHERE user_name = :currentUserName", nativeQuery = true)
    int updateUserName(@Param("currentUserName") String currentUserName, @Param("newUserName") String newUserName);

    // Delete user by username
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_login_details WHERE user_name = :user_name", nativeQuery = true)
    int deleteByUserName(@Param("user_name") String user_name);

    // Get user password for validation (used in delete and change operations)
    @Query(value = "SELECT user_password FROM user_login_details WHERE user_name = :user_name", nativeQuery = true)
    Optional<String> findPasswordByUserName(@Param("user_name") String user_name);

    // Check if username exists (for username availability check)
    @Query(value = "SELECT COUNT(*) AS user_count FROM user_login_details WHERE user_name = :user_name", nativeQuery = true)
    Map<String, Object> checkUserNameExists(@Param("user_name") String user_name);

    // Alternative: Check username exists with case-insensitive comparison
    @Query(value = "SELECT " +
            "SUM(CASE WHEN UPPER(REPLACE(user_name, ' ', '')) = UPPER(REPLACE(:user_name, ' ', '')) THEN 1 ELSE 0 END) AS user_name_count " +
            "FROM user_login_details", nativeQuery = true)
    Map<String, Object> checkUserNameExistsCaseInsensitive(@Param("user_name") String user_name);

    // Get complete user details for login
    @Query(value = "SELECT * FROM user_login_details WHERE user_name = :user_name", nativeQuery = true)
    Optional<UserLoginDetails> findUserForLogin(@Param("user_name") String user_name);

    // Get user details by username (for change password and change username operations)
    @Query(value = "SELECT * FROM user_login_details WHERE user_name = :user_name", nativeQuery = true)
    Optional<UserLoginDetails> getUserDetails(@Param("user_name") String user_name);
}