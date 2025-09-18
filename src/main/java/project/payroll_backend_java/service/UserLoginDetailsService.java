package project.payroll_backend_java.service;

import project.payroll_backend_java.entity.UserLoginDetails;
import java.util.List;
import java.util.Optional;

public interface UserLoginDetailsService {

    // Basic CRUD operations
    UserLoginDetails saveUser(UserLoginDetails user);
    Optional<UserLoginDetails> getUserByUsername(String userName);
    List<UserLoginDetails> getAllUsers();
    UserLoginDetails updateUser(UserLoginDetails user);
    void deleteUser(String userName);

    // Authentication methods
    boolean validateCredentials(String userName, String password);
    Optional<UserLoginDetails> authenticateUser(String userName, String password);
    boolean updatePassword(String userName, String newPassword);
    boolean existsByUsername(String userName);
}
