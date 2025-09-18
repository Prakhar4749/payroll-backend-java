package project.payroll_backend_java.controller;

import project.payroll_backend_java.entity.*;
import project.payroll_backend_java.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/emp")
@CrossOrigin(origins = "*")
public class EmpController {

    @Autowired private EmpDetailsService empDetailsService;
    @Autowired private EmpBankDetailsService empBankDetailsService;
    @Autowired private EmpDeductionDetailsService empDeductionDetailsService;
    @Autowired private EmpEarningDetailsService empEarningDetailsService;
    @Autowired private DeptDetailsService deptDetailsService;

    // Thread-safe caching
    private static volatile boolean cacheValid = false;
    private static volatile List<Map<String, Object>> cachedBasicDetails = null;
    private static final Object cacheLock = new Object();

    // Date formatter
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * GET /emp/ - Get all basic employee details (optimized with caching)
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllBasicEmpDetails() {

        // Check cache first
        if (cacheValid && cachedBasicDetails != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", cachedBasicDetails);
            response.put("message", "Employee basic details fetched successfully (cached)");
            return ResponseEntity.ok(response);
        }

        synchronized (cacheLock) {
            if (cacheValid && cachedBasicDetails != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("result", cachedBasicDetails);
                response.put("message", "Employee basic details fetched successfully (cached)");
                return ResponseEntity.ok(response);
            }

            try {
                List<EmpDetails> employees = empDetailsService.getAllEmployees();
                List<Map<String, Object>> basicDetails = new ArrayList<>();

                for (EmpDetails emp : employees) {
                    Map<String, Object> empMap = new HashMap<>();
                    empMap.put("e_id", emp.getEId());
                    empMap.put("e_name", emp.getEName());
                    empMap.put("e_mobile_number", emp.getEMobileNumber());
                    empMap.put("e_email", emp.getEEmail());
                    empMap.put("e_address", emp.getEAddress());
                    empMap.put("e_designation", emp.getEDesignation());
                    basicDetails.add(empMap);
                }

                cachedBasicDetails = basicDetails;
                cacheValid = true;

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("result", basicDetails);
                response.put("message", "Employee basic details fetched successfully");
                return ResponseEntity.ok(response);

            } catch (Exception err) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("result", err.getMessage());
                response.put("message", "Error fetching employee basic details");
                return ResponseEntity.status(500).body(response);
            }
        }
    }

    /**
     * GET /emp/:e_id - Get all employee details by ID
     */
    @GetMapping("/{e_id}")
    public ResponseEntity<Map<String, Object>> getAllEIdEmpDetails(@PathVariable("e_id") String eId) {

        try {
            // NOW USE THE PROPER METHOD
            Optional<EmpDetails> empDetailsOpt = empDetailsService.getEmployeeWithAllDetails(eId);

            if (!empDetailsOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "No details found for employee ID - " + eId);
                return ResponseEntity.status(404).body(response);
            }

            EmpDetails empDetails = empDetailsOpt.get();

            // Now you can access all related data directly!
            Map<String, Object> result = new HashMap<>();
            result.put("emp_details", empDetails);
            result.put("dept_details", empDetails.getDepartment());
            result.put("emp_bank_details", empDetails.getBankDetails());
            result.put("emp_deduction_details", empDetails.getDeductionDetails());
            result.put("emp_earning_details", empDetails.getEarningDetails());

            // Handle photo
            String photoBase64 = "";
            if (empDetails.getEPhoto() != null && empDetails.getEPhoto().length > 0) {
                photoBase64 = new String(empDetails.getEPhoto(), "UTF-8");
            }
            result.put("e_photo", photoBase64);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", result);
            response.put("message", "Here are the details of " + eId);

            return ResponseEntity.ok(response);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("result", err.getMessage());
            response.put("message", "Error fetching details for " + eId);
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * DELETE /emp/delete/:e_id - Delete employee
     */
    @DeleteMapping("/delete/{e_id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteEId(@PathVariable("e_id") String eId) {

        // Input validation
        if (eId == null || eId.length() != 5) {
            Map<String, Object> response = new HashMap<>();
            response.put("mess", " enter the valid e_id");
            return ResponseEntity.ok(response);
        }

        // Clear cache
        synchronized (cacheLock) {
            cacheValid = false;
            cachedBasicDetails = null;
        }

        try {
            // Check if employee exists
            if (!empDetailsService.getEmployeeById(eId).isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Employee not found: " + eId);
                return ResponseEntity.ok(response);
            }

            // Delete in correct order
            empBankDetailsService.deleteBankDetails(eId);
            empDeductionDetailsService.deleteDeductionDetails(eId);
            empEarningDetailsService.deleteEarningDetails(eId);
            empDetailsService.deleteEmployee(eId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", " employee details of id: " + eId + " is succesfully deleted");
            response.put("result", "deleted");
            return ResponseEntity.ok(response);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "thir is some problum in deleting the " + eId);
            response.put("result", err.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * PUT /emp/chk/1 - Check for data uniqueness
     */
    @PutMapping("/chk/1")
    public ResponseEntity<Map<String, Object>> checkForData(@RequestBody Map<String, Object> request) {

        try {
            // Extract data safely
            Map<String, Object> empDetails = (Map<String, Object>) request.get("emp_details");
            Map<String, Object> empBankDetails = (Map<String, Object>) request.get("emp_bank_details");

            if (empDetails == null) empDetails = new HashMap<>();
            if (empBankDetails == null) empBankDetails = new HashMap<>();

            Long eMobileNumber = null;
            Long eBankAccNumber = null;
            String ePanNumber = null;
            String dId = null;

            // Extract values safely
            if (empDetails.get("e_mobile_number") != null) {
                try {
                    eMobileNumber = Long.valueOf(empDetails.get("e_mobile_number").toString());
                } catch (Exception ignored) {}
            }

            if (empBankDetails.get("e_bank_acc_number") != null) {
                try {
                    eBankAccNumber = Long.valueOf(empBankDetails.get("e_bank_acc_number").toString());
                } catch (Exception ignored) {}
            }

            ePanNumber = (String) empBankDetails.get("e_pan_number");
            dId = (String) empDetails.get("d_id");

            // Build result
            Map<String, Object> result = new HashMap<>();
            StringBuilder messageBuilder = new StringBuilder();

            // Check mobile number
            if (eMobileNumber != null) {
                boolean mobileExists = empDetailsService.existsByMobileNumber(eMobileNumber);
                result.put("e_mobile_number", !mobileExists);
                if (mobileExists) {
                    messageBuilder.append("Mobile number already exists \n");
                }
            } else {
                result.put("e_mobile_number", false);
            }

            // Check bank account number
            if (eBankAccNumber != null) {
                boolean accountExists = empBankDetailsService.existsByAccountNumber(eBankAccNumber);
                result.put("e_bank_acc_number", !accountExists);
                if (accountExists) {
                    messageBuilder.append("Bank account number already exists \n");
                }
            } else {
                result.put("e_bank_acc_number", false);
            }

            // Check PAN number
            if (ePanNumber != null && !ePanNumber.trim().isEmpty()) {
                boolean panExists = empBankDetailsService.existsByPanNumber(ePanNumber);
                result.put("e_pan_number", !panExists);
                if (panExists) {
                    messageBuilder.append("PAN number already exists \n");
                }
            } else {
                result.put("e_pan_number", false);
            }

            // Check department ID
            if (dId != null && !dId.trim().isEmpty()) {
                Optional<DeptDetails> dept = deptDetailsService.getDepartmentById(dId);
                result.put("d_id", dept.isPresent());
                result.put("d_name", dept.isPresent() ? dept.get().getDName() : false);
                if (!dept.isPresent()) {
                    messageBuilder.append("Department ID is invalid \n");
                }
            } else {
                result.put("d_id", false);
                result.put("d_name", false);
                messageBuilder.append("Department ID is invalid \n");
            }

            String message = messageBuilder.length() > 0 ?
                    messageBuilder.toString().trim() : "Every detail is correct and unique";

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", result);
            response.put("message", message);
            return ResponseEntity.ok(response);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("result", err.getMessage());
            response.put("message", "An unexpected error occurred while processing your request.");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Placeholder methods for remaining endpoints
    @PutMapping("/chk_for_update")
    public ResponseEntity<Map<String, Object>> chkForUpdate(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Update check endpoint - implementation pending");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add_emp")
    public ResponseEntity<Map<String, Object>> addNewEmp(
            @RequestParam(value = "e_photo", required = false) MultipartFile ePhoto,
            @RequestBody(required = false) Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Add employee endpoint - implementation pending");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update_emp")
    public ResponseEntity<Map<String, Object>> updateEmp(
            @RequestParam(value = "e_photo", required = false) MultipartFile ePhoto,
            @RequestBody(required = false) Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Update employee endpoint - implementation pending");
        return ResponseEntity.ok(response);
    }
}
