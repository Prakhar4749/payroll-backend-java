package project.payroll_backend_java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.payroll_backend_java.service.EmployeeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/emp")
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Caching mechanism (EXACT from Node.js)
    private static boolean toRunSaved = false;
    private static List<Map<String, Object>> dataWhichIsNotChange = null;

    /**
     * Get all basic employee details (for listing)
     * Corresponds to: get_all_basic__emp_details()
     * GET /emp/
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllBasicEmpDetails() {
        Map<String, Object> response = new HashMap<>();

        if (toRunSaved && dataWhichIsNotChange != null) {
            response.put("success", true);
            response.put("message", "Employee basic details fetched successfully");
            response.put("result", dataWhichIsNotChange);
            return ResponseEntity.ok(response);
        }

        try {
            Map<String, Object> serviceResponse = employeeService.getAllBasicEmployeeDetails();

            if ((Boolean) serviceResponse.get("success")) {
                dataWhichIsNotChange = (List<Map<String, Object>>) serviceResponse.get("result");
                toRunSaved = true;
            }

            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            response.put("success", false);
            response.put("message", "There is some problem in fetching employee basic details");
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Get all details of a specific employee by ID
     * Corresponds to: get_all_e_id_emp_details()
     * GET /emp/{e_id}
     */
    @GetMapping("/{e_id}")
    public ResponseEntity<Map<String, Object>> getEmpDetailsById(@PathVariable("e_id") String e_id) {
        try {
            Map<String, Object> serviceResponse = employeeService.getAllEmployeeDetailsByEid(e_id);
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "There is some problem fetching details of - " + e_id);
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Delete employee by ID
     * Corresponds to: delete_e_id()
     * DELETE /emp/delete_e_id/{e_id}
     */
    @DeleteMapping("/delete_e_id/{e_id}")
    public ResponseEntity<Map<String, Object>> deleteEmpById(@PathVariable("e_id") String e_id) {
        toRunSaved = false;

        try {
            Map<String, Object> serviceResponse = employeeService.deleteEmployee(e_id);
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "There is some problem in deleting the " + e_id);
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Add new employee
     * Corresponds to: add_new_emp()
     * POST /emp/add_emp
     */

    @PostMapping("/add_emp")
    public ResponseEntity<Map<String, Object>> addNewEmployeeJson(@RequestBody Map<String, Object> requestData) {
        toRunSaved = false;

        try {
            Map<String, Object> serviceResponse = employeeService.addNewEmployee(requestData, null);
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            err.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to add employee: " + err.getMessage());
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Update employee
     * Corresponds to: update_emp()
     * PUT /emp/update_emp
     */

    @PutMapping("/update_emp")
    public ResponseEntity<Map<String, Object>> updateEmployeeJson(@RequestBody Map<String, Object> requestData) {
        toRunSaved = false;

        try {
            String existingPhoto = requestData.get("e_photo") != null ?
                    requestData.get("e_photo").toString() : null;

            Map<String, Object> serviceResponse = employeeService.updateEmployee(requestData, null, existingPhoto);
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            err.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error updating employee data: " + err.getMessage());
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Check if employee data exists (for adding new employee)
     * Corresponds to: check_for_data()
     * POST /emp/chk/1
     */
    @PostMapping("/chk/1")
    public ResponseEntity<Map<String, Object>> checkForData(@RequestBody Map<String, Object> requestData) {
        try {
            Map<String, Object> serviceResponse = employeeService.checkForData(requestData);
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> result = new HashMap<>();
            result.put("e_mobile_number", false);
            result.put("e_bank_acc_number", false);
            result.put("e_pan_number", false);
            result.put("d_id", false);
            result.put("d_name", false);

            response.put("success", false);
            response.put("message", "An unexpected error occurred while processing your request.");
            response.put("result", result);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Check for update (validates uniqueness excluding current employee)
     * Corresponds to: chk_for_update()
     * POST /emp/chk/2
     */
    @PostMapping("/chk_for_update")
    public ResponseEntity<Map<String, Object>> checkForUpdate(@RequestBody Map<String, Object> requestData) {
        try {
            Map<String, Object> serviceResponse = employeeService.checkForUpdate(requestData);
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> result = new HashMap<>();
            result.put("e_mobile_number", false);
            result.put("e_bank_acc_number", false);
            result.put("e_pan_number", false);
            result.put("d_id", false);
            result.put("d_name", false);

            response.put("success", false);
            response.put("message", "An unexpected error occurred while processing your request.");
            response.put("result", result);
            return ResponseEntity.ok(response);
        }
    }
}