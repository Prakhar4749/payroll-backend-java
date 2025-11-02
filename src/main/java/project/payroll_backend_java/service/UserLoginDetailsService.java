package project.payroll_backend_java.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.payroll_backend_java.entity.UserLoginDetails;
import project.payroll_backend_java.repository.UserLoginDetailsRepository;

import java.util.*;

@Service
public class UserLoginDetailsService {

    @Autowired
    private UserLoginDetailsRepository userRepo;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String SECRET_KEY = "MyNewSuperSecretKeyThatIsAtLeast32BytesLong!";

    // Register User
    public Map<String, Object> registerUser(String user_name, String user_password) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate input
            if (user_name == null || user_name.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "user_name is required");
                response.put("result", "");
                return response;
            }

            if (user_password == null || user_password.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "user_password is required");
                response.put("result", "");
                return response;
            }

            // Check if username already exists
            int userCount = userRepo.countByUserName(user_name);
            if (userCount > 0) {
                response.put("success", false);
                response.put("message", "Username already exists");
                response.put("result", false);
                return response;
            }

            // Hash password
            String hashedPassword = passwordEncoder.encode(user_password);

            // Insert new user
            int result = userRepo.registerUser(user_name, hashedPassword);
            if (result > 0) {
                response.put("success", true);
                response.put("message", "User registered successfully");
                response.put("result", "");
            } else {
                response.put("success", false);
                response.put("message", "Failed to register user");
                response.put("result", "");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error during registration: " + e.getMessage());
            response.put("result", e.getMessage());
        }
        return response;
    }

    // Login User
    public Map<String, Object> loginUser(String user_name, String user_password) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate input
            if (user_name == null || user_name.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Username is required");
                response.put("result", null);
                return response;
            }

            if (user_password == null || user_password.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password is required");
                response.put("result", null);
                return response;
            }

            // Find user
            Optional<UserLoginDetails> userOptional = userRepo.findUserForLogin(user_name);
            if (!userOptional.isPresent()) {
                response.put("success", false);
                response.put("message", "User not found");
                response.put("result", null);
                return response;
            }

            UserLoginDetails user = userOptional.get();

            // Check if password exists
            if (user.getUser_password() == null || user.getUser_password().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "User password is missing in the database");
                response.put("result", null);
                return response;
            }

            // Compare passwords
            boolean isMatch = passwordEncoder.matches(user_password, user.getUser_password());
            if (!isMatch) {
                response.put("success", false);
                response.put("message", "Invalid Password! Please enter the correct password");
                response.put("result", null);
                return response;
            }

            // Generate JWT token
            String token = Jwts.builder()
                    .setSubject(user.getUser_name())
                    .claim("name", user.getUser_name())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();

            Map<String, String> result = new HashMap<>();
            result.put("token", token);
            result.put("user_name", user_name);

            response.put("success", true);
            response.put("message", "Login Successful!");
            response.put("result", result);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error during login: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    // Change Password
    public Map<String, Object> changePassword(String user_name, String current_password, String new_password) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate input
            if (new_password == null || new_password.trim().isEmpty() ||
                    current_password == null || current_password.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Please enter valid current and new passwords.");
                response.put("result", null);
                return response;
            }

            // Check if passwords are the same
            if (new_password.equals(current_password)) {
                response.put("success", false);
                response.put("message", "Current and new passwords cannot be the same.");
                response.put("result", null);
                return response;
            }

            // Get user details
            Optional<UserLoginDetails> userOptional = userRepo.getUserDetails(user_name);
            if (!userOptional.isPresent()) {
                response.put("success", false);
                response.put("message", "User not found.");
                response.put("result", null);
                return response;
            }

            UserLoginDetails user = userOptional.get();

            // Verify current password
            boolean isMatch = passwordEncoder.matches(current_password, user.getUser_password());
            if (!isMatch) {
                response.put("success", false);
                response.put("message", "Invalid current password.");
                response.put("result", null);
                return response;
            }

            // Hash new password
            String hashedPassword = passwordEncoder.encode(new_password);

            // Update password
            int result = userRepo.updatePassword(user_name, hashedPassword);
            if (result > 0) {
                response.put("success", true);
                response.put("message", "Password changed successfully.");
                response.put("result", null);
            } else {
                response.put("success", false);
                response.put("message", "Failed to change password.");
                response.put("result", null);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error in changing password: " + e.getMessage());
            response.put("result", e.getMessage());
        }
        return response;
    }

    // Change Username
    public Map<String, Object> changeUserName(String current_user_name, String new_user_name, String user_password) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate input
            if (new_user_name == null || new_user_name.trim().isEmpty() ||
                    user_password == null || user_password.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Please enter a valid new username and password.");
                response.put("result", null);
                return response;
            }

            // Check if usernames are the same
            if (new_user_name.equals(current_user_name)) {
                response.put("success", false);
                response.put("message", "Current and new usernames cannot be the same.");
                response.put("result", null);
                return response;
            }

            // Check if new username already exists
            int userCount = userRepo.countByUserName(new_user_name);
            if (userCount > 0) {
                response.put("success", false);
                response.put("message", "Username already exists.");
                response.put("result", null);
                return response;
            }

            // Get user details
            Optional<UserLoginDetails> userOptional = userRepo.getUserDetails(current_user_name);
            if (userOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found.");
                response.put("result", null);
                return response;
            }

            UserLoginDetails user = userOptional.get();

            // Verify password
            boolean isMatch = passwordEncoder.matches(user_password, user.getUser_password());
            if (!isMatch) {
                response.put("success", false);
                response.put("message", "Invalid password.");
                response.put("result", null);
                return response;
            }

            // Update username
            int result = userRepo.updateUserName(current_user_name, new_user_name);
            if (result > 0) {
                response.put("success", true);
                response.put("message", "Username changed successfully.");
                response.put("result", null);
            } else {
                response.put("success", false);
                response.put("message", "Failed to change username.");
                response.put("result", null);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error during username change: " + e.getMessage());
            response.put("result", e.getMessage());
        }
        return response;
    }

    // Get All Users
    public Map<String, Object> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<UserLoginDetails> users = userRepo.findAllUsers();
            response.put("success", true);
            response.put("message", "Users fetched successfully");
            response.put("result", users);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching users: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    // Delete User
    public Map<String, Object> deleteUser(String user_name, String password) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Check if user exists and get password
            Optional<UserLoginDetails> userOptional = userRepo.findByUserName(user_name);
            if (userOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                response.put("result", null);
                return response;
            }

            UserLoginDetails user = userOptional.get();
            String storedPassword = user.getUser_name();

            // Compare passwords
            boolean isPasswordMatch = passwordEncoder.matches(password, storedPassword);
            if (!isPasswordMatch) {
                response.put("success", false);
                response.put("message", "Incorrect password");
                response.put("result", null);
                return response;
            }

            // Delete the user
            int result = userRepo.deleteByUserName(user_name);
            if (result > 0) {
                response.put("success", true);
                response.put("message", "User deleted successfully");
                response.put("result", null);
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete user");
                response.put("result", null);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Internal server error: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }
}