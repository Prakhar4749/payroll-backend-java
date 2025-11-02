package project.payroll_backend_java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.payroll_backend_java.service.UserLoginDetailsService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserLoginDetailsService userService;

    // Register User
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, String> request) {
        try {
            String user_name = request.get("user_name");
            String user_password = request.get("user_password");

            Map<String, Object> serviceResponse = userService.registerUser(user_name, user_password);
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error during registration: " + err.getMessage());
            response.put("result", err.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Login User
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> request) {
        try {
            String user_name = request.get("user_name");
            String user_password = request.get("user_password");

            Map<String, Object> serviceResponse = userService.loginUser(user_name, user_password);

            // Return appropriate HTTP status based on success
            if (!(Boolean) serviceResponse.get("success")) {
                String message = (String) serviceResponse.get("message");
                if (message.contains("not found")) {
                    return ResponseEntity.status(404).body(serviceResponse);
                } else if (message.contains("Invalid Password")) {
                    return ResponseEntity.status(401).body(serviceResponse);
                } else if (message.contains("required")) {
                    return ResponseEntity.status(400).body(serviceResponse);
                } else if (message.contains("missing")) {
                    return ResponseEntity.status(500).body(serviceResponse);
                }
            }

            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error during login: " + err.getMessage());
            response.put("result", null);
            return ResponseEntity.status(500).body(response);
        }
    }

    // Change Password
    @PutMapping("/change_password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> request) {
        try {
            String user_name = request.get("user_name");
            String current_password = request.get("current_password");
            String new_password = request.get("new_password");

            Map<String, Object> serviceResponse = userService.changePassword(user_name, current_password, new_password);

            // Return appropriate HTTP status
            if (!(Boolean) serviceResponse.get("success")) {
                String message = (String) serviceResponse.get("message");
                if (message.contains("Error")) {
                    return ResponseEntity.status(500).body(serviceResponse);
                }
            }

            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error in changing password: " + err.getMessage());
            response.put("result", err.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Change Username
    @PutMapping("/change_username")
    public ResponseEntity<Map<String, Object>> changeUserName(@RequestBody Map<String, String> request) {
        try {
            String current_user_name = request.get("current_user_name");
            String new_user_name = request.get("new_user_name");
            String user_password = request.get("user_password");

            Map<String, Object> serviceResponse = userService.changeUserName(current_user_name, new_user_name, user_password);

            // Return appropriate HTTP status
            if (!(Boolean) serviceResponse.get("success")) {
                String message = (String) serviceResponse.get("message");
                if (message.contains("Error")) {
                    return ResponseEntity.status(500).body(serviceResponse);
                }
            }

            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error during username change: " + err.getMessage());
            response.put("result", err.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Get All Users
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        try {
            Map<String, Object> serviceResponse = userService.getAllUsers();
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error fetching users: " + err.getMessage());
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    // Delete User
    @DeleteMapping("/delete_user/{user_name}/{password}")
    public ResponseEntity<Map<String, Object>> deleteUser(
            @PathVariable("user_name") String user_name,
            @PathVariable("password") String password) {
        try {
            Map<String, Object> serviceResponse = userService.deleteUser(user_name, password);

            // Return appropriate HTTP status
            if (!(Boolean) serviceResponse.get("success")) {
                String message = (String) serviceResponse.get("message");
                if (message.contains("Internal server error")) {
                    return ResponseEntity.status(500).body(serviceResponse);
                }
            }

            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error deleting user: " + err.getMessage());
            response.put("result", null);
            return ResponseEntity.status(500).body(response);
        }
    }
}