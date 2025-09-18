package project.payroll_backend_java.controller;

import project.payroll_backend_java.entity.UserLoginDetails;
import project.payroll_backend_java.service.UserLoginDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserLoginDetailsService userService;

    @Value("${app.secret-key:admin}")
    private String secretKey;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * POST /auth/register - Register a new user (EXACT Node.js translation)
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String userName = request.get("user_name");
            String userPassword = request.get("user_password");

            // Check if username and password are provided (EXACT validation from Node.js)
            if (userName == null || userName.trim().isEmpty() ||
                    userPassword == null || userPassword.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "user_name and user_password are required");
                response.put("result", "");
                return ResponseEntity.status(400).body(response);
            }

            // Check if the username already exists (EXACT logic from Node.js)
            if (userService.existsByUsername(userName.trim())) {
                response.put("success", false);
                response.put("message", "Username already exists");
                response.put("result", false);
                return ResponseEntity.ok(response);
            }

            // Hash the password before storing (EXACT BCrypt from Node.js)
            String hashedPassword = passwordEncoder.encode(userPassword);

            // Create new user
            UserLoginDetails newUser = new UserLoginDetails();
            newUser.setUserName(userName.trim());
            newUser.setUserPassword(hashedPassword);

            userService.saveUser(newUser);

            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("result", "");

            return ResponseEntity.ok(response);

        } catch (Exception err) {
            System.err.println("Database query error: " + err.getMessage());
            response.put("success", false);
            response.put("message", "Error during registration");
            response.put("result", err.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * POST /auth/login - User login (EXACT Node.js translation)
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String userName = request.get("user_name");
            String userPassword = request.get("user_password");

            // Validate input (EXACT validation from Node.js)
            if (userName == null || userName.trim().isEmpty() ||
                    userPassword == null || userPassword.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Username and password are required");
                return ResponseEntity.status(400).body(response);
            }

            // Find user by username
            Optional<UserLoginDetails> userOpt = userService.getUserByUsername(userName.trim());

            // If user does not exist (EXACT response from Node.js)
            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.status(404).body(response);
            }

            UserLoginDetails user = userOpt.get();

            // Ensure password exists before comparing (EXACT check from Node.js)
            if (user.getUserPassword() == null || user.getUserPassword().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "User password is missing in the database");
                return ResponseEntity.status(500).body(response);
            }

            // Compare passwords securely (EXACT BCrypt compare from Node.js)
            boolean isMatch = passwordEncoder.matches(userPassword, user.getUserPassword());

            if (!isMatch) {
                response.put("success", false);
                response.put("message", "Invalid Password! Please enter the correct password");
                return ResponseEntity.status(401).body(response);
            }

            // Generate JWT token (EXACT JWT logic from Node.js)
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
            String token = Jwts.builder()
                    .claim("name", user.getUserName())
                    .signWith(key)
                    .compact();

            // EXACT response format from Node.js
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user_name", userName);

            response.put("success", true);
            response.put("message", "Login Successful!");
            response.put("result", result);

            return ResponseEntity.ok(response);

        } catch (Exception err) {
            System.err.println("Database query error: " + err.getMessage());
            response.put("success", false);
            response.put("message", "Error during login");
            response.put("result", err.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * PUT /auth/change_password - Change password (EXACT Node.js translation)
     */
    @PutMapping("/change_password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String userName = request.get("user_name");
            String currentPassword = request.get("current_password");
            String newPassword = request.get("new_password");

            // Check if passwords are provided (EXACT validation from Node.js)
            if (newPassword == null || newPassword.trim().isEmpty() ||
                    currentPassword == null || currentPassword.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Please enter valid current and new passwords.");
                return ResponseEntity.ok(response);
            }

            // Check if the new password is the same as the current one (EXACT check from Node.js)
            if (newPassword.equals(currentPassword)) {
                response.put("success", false);
                response.put("message", "Current and new passwords cannot be the same.");
                return ResponseEntity.ok(response);
            }

            // Retrieve the user details based on the username
            Optional<UserLoginDetails> userOpt = userService.getUserByUsername(userName);

            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "User not found.");
                return ResponseEntity.ok(response);
            }

            UserLoginDetails user = userOpt.get();

            // Compare the current password with the stored hashed password (EXACT BCrypt from Node.js)
            boolean isMatch = passwordEncoder.matches(currentPassword, user.getUserPassword());

            if (!isMatch) {
                response.put("success", false);
                response.put("message", "Invalid current password.");
                return ResponseEntity.ok(response);
            }

            // Hash the new password (EXACT BCrypt from Node.js)
            String hashedPassword = passwordEncoder.encode(newPassword);

            // Update the password in the database
            boolean updated = userService.updatePassword(userName, hashedPassword);

            if (updated) {
                response.put("success", true);
                response.put("message", "Password changed successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Error in changing password.");
                return ResponseEntity.status(500).body(response);
            }

        } catch (Exception err) {
            System.err.println("Database query error: " + err.getMessage());
            response.put("success", false);
            response.put("message", "Error in changing password.");
            response.put("result", err.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * PUT /auth/change_user_name - Change username (EXACT Node.js translation)
     */
    @PutMapping("/change_user_name")
    public ResponseEntity<Map<String, Object>> changeUserName(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String currentUserName = request.get("current_user_name");
            String newUserName = request.get("new_user_name");
            String userPassword = request.get("user_password");

            // Check if required fields are provided (EXACT validation from Node.js)
            if (newUserName == null || newUserName.trim().isEmpty() ||
                    userPassword == null || userPassword.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Please enter a valid new username and password.");
                return ResponseEntity.ok(response);
            }

            // Check if the current username is the same as the new username (EXACT check from Node.js)
            if (newUserName.equals(currentUserName)) {
                response.put("success", false);
                response.put("message", "Current and new usernames cannot be the same.");
                return ResponseEntity.ok(response);
            }

            // Check if the new username already exists (EXACT check from Node.js)
            if (userService.existsByUsername(newUserName.trim())) {
                response.put("success", false);
                response.put("message", "Username already exists.");
                return ResponseEntity.ok(response);
            }

            // Get user details based on current username
            Optional<UserLoginDetails> userOpt = userService.getUserByUsername(currentUserName);

            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "User not found.");
                return ResponseEntity.ok(response);
            }

            UserLoginDetails user = userOpt.get();

            // Compare the provided password with the stored password (EXACT BCrypt from Node.js)
            boolean isMatch = passwordEncoder.matches(userPassword, user.getUserPassword());

            if (!isMatch) {
                response.put("success", false);
                response.put("message", "Invalid password.");
                return ResponseEntity.ok(response);
            }

            // Update the username in the database (Delete and create new due to primary key)
            userService.deleteUser(currentUserName);

            UserLoginDetails newUser = new UserLoginDetails();
            newUser.setUserName(newUserName.trim());
            newUser.setUserPassword(user.getUserPassword()); // Keep same hashed password

            userService.saveUser(newUser);

            response.put("success", true);
            response.put("message", "Username changed successfully.");

            return ResponseEntity.ok(response);

        } catch (Exception err) {
            System.err.println("Database query error: " + err.getMessage());
            response.put("success", false);
            response.put("message", "Error during username change.");
            response.put("result", err.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * GET /auth/getalluser - Get all users (EXACT Node.js translation)
     */
    @GetMapping("/getalluser")
    public ResponseEntity<List<UserLoginDetails>> getAllUsers() {
        try {
            List<UserLoginDetails> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /auth/deleteuser/:user_name/:password - Delete user (EXACT Node.js translation)
     */
    @DeleteMapping("/deleteuser/{user_name}/{password}")
    public ResponseEntity<Map<String, Object>> deleteUser(
            @PathVariable("user_name") String userName,
            @PathVariable("password") String password) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Check if user exists and get stored hashed password (EXACT logic from Node.js)
            Optional<UserLoginDetails> userOpt = userService.getUserByUsername(userName);

            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }

            UserLoginDetails user = userOpt.get();
            String storedPassword = user.getUserPassword();

            // Compare passwords (EXACT BCrypt compare from Node.js)
            boolean isPasswordMatch = passwordEncoder.matches(password, storedPassword);

            if (!isPasswordMatch) {
                response.put("success", false);
                response.put("message", "Incorrect password");
                return ResponseEntity.ok(response);
            }

            // Delete the user if password matches
            userService.deleteUser(userName);

            response.put("success", true);
            response.put("message", "User deleted successfully");

            return ResponseEntity.ok(response);

        } catch (Exception err) {
            System.err.println("Error deleting user: " + err.getMessage());
            response.put("success", false);
            response.put("message", "Internal server error");
            return ResponseEntity.status(500).body(response);
        }
    }
}
