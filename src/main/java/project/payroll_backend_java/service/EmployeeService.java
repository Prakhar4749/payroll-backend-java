package project.payroll_backend_java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.payroll_backend_java.entity.EmpDetails;
import project.payroll_backend_java.repository.EmployeeRepository;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository empRepo;

    // Cached data for basic employee list
    private List<Map<String, Object>> cachedBasicEmployees = null;
    private boolean isCacheValid = false;

    /**
     * Format date for MySQL (YYYY-MM-DD)
     * Corresponds to: formatDateForMySQL()
     */
    private String formatDateForMySQL(String dateInput) throws ParseException {
        if (dateInput == null || dateInput.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid date input");
        }

        // Remove time component if present
        String dateString = dateInput.split("[T\\s]")[0];

        // Try different date patterns
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;

        // Try YYYY-MM-DD or YYYY/MM/DD
        if (dateString.matches("^\\d{4}[/-]\\d{1,2}[/-]\\d{1,2}$")) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            inputFormat.setLenient(false);
            date = inputFormat.parse(dateString.replace("/", "-"));
        }
        // Try DD-MM-YYYY or DD/MM/YYYY
        else if (dateString.matches("^\\d{1,2}[/-]\\d{1,2}[/-]\\d{4}$")) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
            inputFormat.setLenient(false);
            date = inputFormat.parse(dateString.replace("/", "-"));
        }
        else {
            throw new ParseException("Invalid date format: " + dateString, 0);
        }

        return outputFormat.format(date);
    }

    /**
     * Increment employee ID (E0001 -> E0002)
     * Corresponds to: incrementString()
     */
    private String incrementEmployeeId(String lastId) {
        if (lastId == null || lastId.isEmpty()) {
            return "E0001";
        }
        String prefix = lastId.substring(0, lastId.length() - 4);
        String numberPart = lastId.substring(lastId.length() - 4);
        int incremented = Integer.parseInt(numberPart) + 1;
        return prefix + String.format("%04d", incremented);
    }

    /**
     * Convert image to Base64 string
     */
    private String convertImageToBase64(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return "NULL";
        }
        byte[] bytes = file.getBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes);
        return "data:image/jpeg;base64," + base64;
    }

    /**
     * Get all basic employee details (for listing)
     * Corresponds to: get_all_basic__emp_details()
     */
    public Map<String, Object> getAllBasicEmployeeDetails() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Return cached data if available
            if (isCacheValid && cachedBasicEmployees != null) {
                response.put("success", true);
                response.put("result", cachedBasicEmployees);
                response.put("message", "Employee basic details fetched successfully");
                return response;
            }

            // Fetch from database
            List<Map<String, Object>> employees = empRepo.findAllBasicEmployees();

            // Cache the result
            cachedBasicEmployees = employees;
            isCacheValid = true;

            response.put("success", true);
            response.put("result", employees);
            response.put("message", "Employee basic details fetched successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("result", null);
            response.put("message", "There is some problem in fetching employee basic details");
        }
        return response;
    }

    /**
     * Get all employee details by ID
     * Corresponds to: get_all_e_id_emp_details()
     */
    public Map<String, Object> getAllEmployeeDetailsByEid(String e_id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Fetch all details
            EmpDetails empDetails = empRepo.findEmployeeById(e_id);

            if (empDetails == null) {
                response.put("success", false);
                response.put("message", "No details found for employee ID - " + e_id);
                response.put("result", null);
                return response;
            }

            Map<String, Object> empBankDetails = empRepo.findEmployeeBankDetails(e_id);
            Map<String, Object> empDeductionDetails = empRepo.findEmployeeDeductionDetails(e_id);
            Map<String, Object> empEarningDetails = empRepo.findEmployeeEarningDetails(e_id);
            Map<String, Object> deptDetails = empRepo.findDepartmentById(empDetails.getD_id());

            // Format dates
            String dob = formatDateForMySQL(empDetails.getE_DOB().toString());
            String doj = formatDateForMySQL(empDetails.getE_date_of_joining().toString());

            // Handle photo
            byte[] photo = empDetails.getE_photo();

            // Build result
            Map<String, Object> result = new HashMap<>();
            result.put("e_photo", photo);

            // Convert EmpDetails to Map for consistent response
            Map<String, Object> empDetailsMap = new HashMap<>();
            empDetailsMap.put("e_id", empDetails.getE_id());
            empDetailsMap.put("e_name", empDetails.getE_name());
            empDetailsMap.put("e_mobile_number", empDetails.getE_mobile_number());
            empDetailsMap.put("e_gender", empDetails.getE_gender());
            empDetailsMap.put("e_email", empDetails.getE_email());
            empDetailsMap.put("e_address", empDetails.getE_address());
            empDetailsMap.put("e_photo", ""); // Remove binary data
            empDetailsMap.put("d_id", empDetails.getD_id());
            empDetailsMap.put("e_designation", empDetails.getE_designation());
            empDetailsMap.put("e_group", empDetails.getE_group());
            empDetailsMap.put("e_date_of_joining", doj);
            empDetailsMap.put("e_DOB", dob);

            result.put("emp_details", empDetailsMap);
            result.put("dept_details", deptDetails);
            result.put("emp_bank_details", empBankDetails);
            result.put("emp_deduction_details", empDeductionDetails);
            result.put("emp_earning_details", empEarningDetails);

            response.put("success", true);
            response.put("result", result);
            response.put("message", "Here are the details of " + e_id);
        } catch (Exception e) {
            response.put("success", false);
            response.put("result", null);
            response.put("message", "There is some problem fetching details of - " + e_id);
        }
        return response;
    }

    /**
     * Check if employee data exists (for adding new employee)
     * Corresponds to: check_for_data()
     */
    public Map<String, Object> checkForData(Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> empDetails = (Map<String, Object>) requestData.get("emp_details");
            Map<String, Object> empBankDetails = (Map<String, Object>) requestData.get("emp_bank_details");

            String mobile = empDetails != null && empDetails.get("e_mobile_number") != null
                    ? String.valueOf(empDetails.get("e_mobile_number"))
                    : null;

            String bank = empBankDetails != null && empBankDetails.get("e_bank_acc_number") != null
                    ? String.valueOf(empBankDetails.get("e_bank_acc_number"))
                    : null;

            String pan = empBankDetails != null && empBankDetails.get("e_pan_number") != null
                    ? String.valueOf(empBankDetails.get("e_pan_number"))
                    : null;

            String dId = empDetails != null && empDetails.get("d_id") != null
                    ? String.valueOf(empDetails.get("d_id"))
                    : null;


            // Check for existing records
            Map<String, Object> checkResult = empRepo.checkEmployeeExistsForAdd(
                    mobile != null ? mobile : "",
                    bank != null ? bank : "",
                    pan != null ? pan : ""
            );

            Long mobileCount = ((Number) checkResult.get("mobile_count")).longValue();
            Long bankCount = ((Number) checkResult.get("bank_count")).longValue();
            Long panCount = ((Number) checkResult.get("pan_count")).longValue();

            Map<String, Object> result = new HashMap<>();
            StringBuilder message = new StringBuilder();

            // Check mobile
            if (mobileCount == 0 && mobile != null) {
                result.put("e_mobile_number", true);
            } else {
                result.put("e_mobile_number", false);
                message.append("Mobile number already exists \n");
            }

            // Check bank account
            if (bankCount == 0 && bank != null) {
                result.put("e_bank_acc_number", true);
            } else {
                result.put("e_bank_acc_number", false);
                message.append("Bank account number already exists \n");
            }

            // Check PAN
            if (panCount == 0 && pan != null) {
                result.put("e_pan_number", true);
            } else {
                result.put("e_pan_number", false);
                message.append("PAN number already exists \n");
            }

            // Check department
            Map<String, Object> deptResult = empRepo.validateDepartmentId(dId);
            if (deptResult != null && !deptResult.isEmpty()) {
                result.put("d_id", true);
                result.put("d_name", deptResult.get("d_name"));
            } else {
                result.put("d_id", false);
                result.put("d_name", false);
                message.append("Department ID is invalid \n");
            }

            String finalMessage = message.length() > 0 ? message.toString().trim() : "Every detail is correct and unique";

            response.put("success", true);
            response.put("result", result);
            response.put("message", finalMessage);
        } catch (Exception e) {
            response.put("success", false);
            response.put("result", e);
            response.put("message", "An unexpected error occurred while processing your request." );
        }
        return response;
    }

    /**
     * Check for update (validates uniqueness excluding current employee)
     * Corresponds to: chk_for_update()
     */
    public Map<String, Object> checkForUpdate(Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> empDetails = (Map<String, Object>) requestData.get("emp_details");
            Map<String, Object> empBankDetails = (Map<String, Object>) requestData.get("emp_bank_details");

            String eId = empDetails != null && empDetails.get("e_id") != null
                    ? String.valueOf(empDetails.get("e_id"))
                    : null;

            String mobile = empDetails != null && empDetails.get("e_mobile_number") != null
                    ? String.valueOf(empDetails.get("e_mobile_number"))
                    : null;

            String bank = empBankDetails != null && empBankDetails.get("e_bank_acc_number") != null
                    ? String.valueOf(empBankDetails.get("e_bank_acc_number"))
                    : null;

            String pan = empBankDetails != null && empBankDetails.get("e_pan_number") != null
                    ? String.valueOf(empBankDetails.get("e_pan_number"))
                    : null;

            String dId = empDetails != null && empDetails.get("d_id") != null
                    ? String.valueOf(empDetails.get("d_id"))
                    : null;

            Map<String, Object> result = new HashMap<>();
            StringBuilder message = new StringBuilder();

            // Check mobile (excluding current employee)
            List<String> mobileUsers = empRepo.findByMobileNumber(mobile);
            if ((mobileUsers.isEmpty() || (mobileUsers.size() == 1 && mobileUsers.get(0).equals(eId))) && mobile != null) {
                result.put("e_mobile_number", true);
            } else {
                result.put("e_mobile_number", false);
                message.append("Mobile number already exists or is invalid. \n");
            }

            // Check bank account (excluding current employee)
            List<String> bankUsers = empRepo.findByBankAccount(bank);
            if ((bankUsers.isEmpty() || (bankUsers.size() == 1 && bankUsers.get(0).equals(eId))) && bank != null) {
                result.put("e_bank_acc_number", true);
            } else {
                result.put("e_bank_acc_number", false);
                message.append("Bank account number already exists or is invalid. \n");
            }

            // Check PAN (excluding current employee)
            List<String> panUsers = empRepo.findByPanNumber(pan);
            if ((panUsers.isEmpty() || (panUsers.size() == 1 && panUsers.get(0).equals(eId))) && pan != null) {
                result.put("e_pan_number", true);
            } else {
                result.put("e_pan_number", false);
                message.append("PAN number already exists or is invalid. \n");
            }

            // Check department
            Map<String, Object> deptResult = empRepo.validateDepartmentId(dId);
            if (deptResult != null && !deptResult.isEmpty()) {
                result.put("d_id", true);
                result.put("d_name", deptResult.get("d_name"));
            } else {
                result.put("d_id", false);
                result.put("d_name", false);
                message.append("Department ID is invalid or not found. \n");
            }

            String finalMessage = message.length() > 0 ? message.toString().trim() : "Every detail is correct and unique";

            response.put("success", true);
            response.put("result", result);
            response.put("message", finalMessage);
        } catch (Exception e) {
            response.put("success", false);
            response.put("result", null);
            response.put("message", "An unexpected error occurred while processing your request.");
        }
        return response;
    }

    /**
     * Delete employee by ID
     * Corresponds to: delete_e_id()
     */
    @Transactional
    public Map<String, Object> deleteEmployee(String e_id) {
        Map<String, Object> response = new HashMap<>();

        // Invalidate cache
        isCacheValid = false;

        try {
            // Validate e_id
            if (e_id == null || e_id.length() != 5) {
                response.put("success", false);
                response.put("message", "Enter the valid e_id");
                response.put("result", null);
                return response;
            }

            // Delete in correct order (foreign key constraints)
            empRepo.deleteEmployeeBankDetails(e_id);
            empRepo.deleteEmployeeDeductionDetails(e_id);
            empRepo.deleteEmployeeEarningDetails(e_id);
            int result = empRepo.deleteEmployeeMainDetails(e_id);

            if (result > 0) {
                response.put("success", true);
                response.put("message", "Employee details of id: " + e_id + " is successfully deleted");
                response.put("result", result);
            } else {
                response.put("success", false);
                response.put("message", "No employee found with id: " + e_id);
                response.put("result", null);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "There is some problem in deleting the " + e_id);
            response.put("result", null);
        }
        return response;
    }

    /**
     * Add new employee
     * Corresponds to: add_new_emp()
     */
    @Transactional
    public Map<String, Object> addNewEmployee(Map<String, Object> requestData, MultipartFile imageFile) {
        Map<String, Object> response = new HashMap<>();

        // Invalidate cache
        isCacheValid = false;

        try {
            // Process image
            String imgStr64 = convertImageToBase64(imageFile);

            // Generate new employee ID
            String latestId = empRepo.findLatestEmployeeId();
            String newEId = latestId != null ? incrementEmployeeId(latestId) : "E0001";

            // Extract data from request
            Map<String, Object> empDetails = (Map<String, Object>) requestData.get("emp_details");
            Map<String, Object> empBankDetails = (Map<String, Object>) requestData.get("emp_bank_details");
            Map<String, Object> empDeductionDetails = (Map<String, Object>) requestData.get("emp_deduction_details");
            Map<String, Object> empEarningDetails = (Map<String, Object>) requestData.get("emp_earning_details");

            // Format dates
            String formattedDOJ = formatDateForMySQL((String) empDetails.get("e_date_of_joining"));
            String formattedDOB = formatDateForMySQL((String) empDetails.get("e_DOB"));

            // Insert into emp_details
            empRepo.addEmployee(
                    newEId,
                    String.valueOf(empDetails.get("e_name")),
                    String.valueOf(empDetails.get("e_mobile_number")), // safer conversion
                    String.valueOf(empDetails.get("e_gender")),
                    String.valueOf(empDetails.get("e_email")),
                    String.valueOf(empDetails.get("e_address")),
                    imgStr64,
                    String.valueOf(empDetails.get("d_id")),
                    String.valueOf(empDetails.get("e_designation")),
                    String.valueOf(empDetails.get("e_group")),
                    formattedDOJ,
                    formattedDOB
            );

// Insert into emp_bank_details
            empRepo.addEmployeeBank(
                    newEId,
                    String.valueOf(empBankDetails.get("e_name")),
                    String.valueOf(empBankDetails.get("e_bank_name")),
                    String.valueOf(empBankDetails.get("e_bank_acc_number")), // safer conversion
                    String.valueOf(empBankDetails.get("e_pan_number")),
                    String.valueOf(empBankDetails.get("e_bank_IFSC")),
                    empBankDetails.get("e_cpf_or_gpf_number") != null ?
                            String.valueOf(empBankDetails.get("e_cpf_or_gpf_number")) : null
            );


            // Insert into emp_deduction_details
            empRepo.addEmployeeDeduction(
                    newEId,
                    (String) empDetails.get("e_name"),
                    0, // leave_days
                    0, // leave_deduction_amount
                    parseIntOrDefault(empDeductionDetails.get("deduction_CPF")),
                    parseIntOrDefault(empDeductionDetails.get("GIS")),
                    parseIntOrDefault(empDeductionDetails.get("house_rent")),
                    parseIntOrDefault(empDeductionDetails.get("water_charges")),
                    parseIntOrDefault(empDeductionDetails.get("electricity_charges")),
                    parseIntOrDefault(empDeductionDetails.get("vehicle_deduction")),
                    parseIntOrDefault(empDeductionDetails.get("HB_loan")),
                    parseIntOrDefault(empDeductionDetails.get("GPF_loan")),
                    parseIntOrDefault(empDeductionDetails.get("festival_loan")),
                    parseIntOrDefault(empDeductionDetails.get("grain_charges")),
                    parseIntOrDefault(empDeductionDetails.get("bank_advance")),
                    parseIntOrDefault(empDeductionDetails.get("advance")),
                    parseIntOrDefault(empDeductionDetails.get("RGPV_advance")),
                    parseIntOrDefault(empDeductionDetails.get("income_tax")),
                    parseIntOrDefault(empDeductionDetails.get("professional_tax"))
            );

            // Insert into emp_earning_details
            empRepo.addEmployeeEarning(
                    newEId,
                    (String) empDetails.get("e_name"),
                    parseIntOrDefault(empEarningDetails.get("basic_salary")),
                    parseIntOrDefault(empEarningDetails.get("special_pay")),
                    parseIntOrDefault(empEarningDetails.get("dearness_allowance")),
                    parseIntOrDefault(empEarningDetails.get("DA")),
                    parseIntOrDefault(empEarningDetails.get("ADA")),
                    parseIntOrDefault(empEarningDetails.get("interim_relief")),
                    parseIntOrDefault(empEarningDetails.get("HRA")),
                    parseIntOrDefault(empEarningDetails.get("CCA")),
                    parseIntOrDefault(empEarningDetails.get("conveyance")),
                    parseIntOrDefault(empEarningDetails.get("medical")),
                    parseIntOrDefault(empEarningDetails.get("washing_allowance")),
                    parseIntOrDefault(empEarningDetails.get("BDP")),
                    parseIntOrDefault(empEarningDetails.get("arrears"))
            );

            response.put("success", true);
            response.put("message", "Employee added successfully, " + newEId + " is your E_id.");
            response.put("result", newEId);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Failed to add employee.");
            response.put("result", null);
        }
        return response;
    }

    /**
     * Update employee
     * Corresponds to: update_emp()
     */
    @Transactional
    public Map<String, Object> updateEmployee(Map<String, Object> requestData, MultipartFile imageFile, String existingPhoto) {
        Map<String, Object> response = new HashMap<>();

        // Invalidate cache
        isCacheValid = false;

        try {
            // Handle image
            String imgStr64;
            if (imageFile != null && !imageFile.isEmpty()) {
                imgStr64 = convertImageToBase64(imageFile);
            } else if (existingPhoto != null && !existingPhoto.equals("Null")) {
                imgStr64 = existingPhoto;
            } else {
                imgStr64 = "NULL";
            }

            // Extract data from request
            Map<String, Object> empDetails = (Map<String, Object>) requestData.get("emp_details");
            Map<String, Object> empBankDetails = (Map<String, Object>) requestData.get("emp_bank_details");
            Map<String, Object> empDeductionDetails = (Map<String, Object>) requestData.get("emp_deduction_details");
            Map<String, Object> empEarningDetails = (Map<String, Object>) requestData.get("emp_earning_details");

            String eId = (String) empDetails.get("e_id");

            // Format dates
            String formattedDOJ = formatDateForMySQL((String) empDetails.get("e_date_of_joining"));
            String formattedDOB = formatDateForMySQL((String) empDetails.get("e_DOB"));

            // Update emp_details
            empRepo.updateEmployee(
                    eId,
                    (String) empDetails.get("e_name"),
                    String.valueOf(empDetails.get("e_mobile_number")), // converts Long to String safely
                    (String) empDetails.get("e_gender"),
                    (String) empDetails.get("e_email"),
                    (String) empDetails.get("e_address"),
                    imgStr64,
                    String.valueOf(empDetails.get("d_id")),            // safer conversion
                    (String) empDetails.get("e_designation"),
                    (String) empDetails.get("e_group"),
                    formattedDOJ,
                    formattedDOB
            );

            // Update emp_bank_details
            empRepo.updateEmployeeBank(
                    eId,
                    String.valueOf(empDetails.get("e_name")),
                    String.valueOf(empBankDetails.get("e_bank_name")),
                    String.valueOf(empBankDetails.get("e_bank_acc_number")), // safe conversion from Long/Integer
                    String.valueOf(empBankDetails.get("e_pan_number")),
                    String.valueOf(empBankDetails.get("e_bank_IFSC")),
                    empBankDetails.get("e_cpf_or_gpf_number") != null ?
                            String.valueOf(empBankDetails.get("e_cpf_or_gpf_number")) : null // safe conversion
            );


            // Update emp_deduction_details
            empRepo.updateEmployeeDeduction(
                    eId,
                    (String) empDetails.get("e_name"),
                    0, // leave_days
                    0, // leave_deduction_amount
                    parseIntOrDefault(empDeductionDetails.get("deduction_CPF")),
                    parseIntOrDefault(empDeductionDetails.get("GIS")),
                    parseIntOrDefault(empDeductionDetails.get("house_rent")),
                    parseIntOrDefault(empDeductionDetails.get("water_charges")),
                    parseIntOrDefault(empDeductionDetails.get("electricity_charges")),
                    parseIntOrDefault(empDeductionDetails.get("vehicle_deduction")),
                    parseIntOrDefault(empDeductionDetails.get("HB_loan")),
                    parseIntOrDefault(empDeductionDetails.get("GPF_loan")),
                    parseIntOrDefault(empDeductionDetails.get("festival_loan")),
                    parseIntOrDefault(empDeductionDetails.get("grain_charges")),
                    parseIntOrDefault(empDeductionDetails.get("bank_advance")),
                    parseIntOrDefault(empDeductionDetails.get("advance")),
                    parseIntOrDefault(empDeductionDetails.get("RGPV_advance")),
                    parseIntOrDefault(empDeductionDetails.get("income_tax")),
                    parseIntOrDefault(empDeductionDetails.get("professional_tax"))
            );

            // Update emp_earning_details
            empRepo.updateEmployeeEarning(
                    eId,
                    (String) empDetails.get("e_name"),
                    parseIntOrDefault(empEarningDetails.get("basic_salary")),
                    parseIntOrDefault(empEarningDetails.get("special_pay")),
                    parseIntOrDefault(empEarningDetails.get("dearness_allowance")),
                    parseIntOrDefault(empEarningDetails.get("DA")),
                    parseIntOrDefault(empEarningDetails.get("ADA")),
                    parseIntOrDefault(empEarningDetails.get("interim_relief")),
                    parseIntOrDefault(empEarningDetails.get("HRA")),
                    parseIntOrDefault(empEarningDetails.get("CCA")),
                    parseIntOrDefault(empEarningDetails.get("conveyance")),
                    parseIntOrDefault(empEarningDetails.get("medical")),
                    parseIntOrDefault(empEarningDetails.get("washing_allowance")),
                    parseIntOrDefault(empEarningDetails.get("BDP")),
                    parseIntOrDefault(empEarningDetails.get("arrears"))
            );

            response.put("success", true);
            response.put("message", "Employee - " + eId + " data updated successfully.");
            response.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error updating employee data.");
            response.put("result", null);
        }
        return response;
    }

    /**
     * Helper method to parse integer with default value
     */
    private int parseIntOrDefault(Object value) {
        if (value == null) return 0;
        try {
            if (value instanceof Integer) return (Integer) value;
            if (value instanceof String) return Integer.parseInt((String) value);
            return ((Number) value).intValue();
        } catch (Exception e) {
            return 0;
        }
    }
}