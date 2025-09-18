package project.payroll_backend_java.controller;

import project.payroll_backend_java.entity.*;
import project.payroll_backend_java.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Value;

import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/payslip")
@CrossOrigin(origins = "*")
public class PayslipController {

    @Autowired private SalaryArchiveService salaryArchiveService;
    @Autowired private EmpDetailsService empDetailsService;
    @Autowired private DeptDetailsService deptDetailsService;
    @Autowired private EmpBankDetailsService empBankDetailsService;
    @Autowired private EmpEarningDetailsService empEarningDetailsService;
    @Autowired private EmpDeductionDetailsService empDeductionDetailsService;
    @Autowired private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // ==================== HELPER METHODS ====================

    /**
     * Safe integer parsing with error handling
     */
    private Integer safeParseInteger(Object value, String fieldName) {
        if (value == null) return null;

        if (value instanceof Integer) {
            return (Integer) value;
        }

        if (value instanceof String) {
            String strValue = ((String) value).trim();
            if (strValue.isEmpty()) return null;

            try {
                return Integer.parseInt(strValue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid " + fieldName + " format: " + value);
            }
        }

        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + fieldName + " format: " + value);
        }
    }

    /**
     * Safe string parsing with null handling
     */
    private String safeParseString(Object value, String fieldName) {
        if (value == null) return null;
        String result = value.toString().trim();
        return result.isEmpty() ? null : result;
    }

    /**
     * Standardize month format - always return full month name
     * Handles: "1" -> "January", "01" -> "January", "Jan" -> "January", "january" -> "January"
     */
    private String standardizeMonth(Object monthValue) {
        if (monthValue == null) return null;

        String month = monthValue.toString().trim().toLowerCase();

        // Month number to name mapping
        Map<String, String> monthMap = new HashMap<>();
        monthMap.put("1", "January");
        monthMap.put("01", "January");
        monthMap.put("2", "February");
        monthMap.put("02", "February");
        monthMap.put("3", "March");
        monthMap.put("03", "March");
        monthMap.put("4", "April");
        monthMap.put("04", "April");
        monthMap.put("5", "May");
        monthMap.put("05", "May");
        monthMap.put("6", "June");
        monthMap.put("06", "June");
        monthMap.put("7", "July");
        monthMap.put("07", "July");
        monthMap.put("8", "August");
        monthMap.put("08", "August");
        monthMap.put("9", "September");
        monthMap.put("09", "September");
        monthMap.put("10", "October");
        monthMap.put("11", "November");
        monthMap.put("12", "December");

        // Short name to full name mapping
        monthMap.put("jan", "January");
        monthMap.put("feb", "February");
        monthMap.put("mar", "March");
        monthMap.put("apr", "April");
        monthMap.put("may", "May");
        monthMap.put("jun", "June");
        monthMap.put("jul", "July");
        monthMap.put("aug", "August");
        monthMap.put("sep", "September");
        monthMap.put("oct", "October");
        monthMap.put("nov", "November");
        monthMap.put("dec", "December");

        // Full name variations
        monthMap.put("january", "January");
        monthMap.put("february", "February");
        monthMap.put("march", "March");
        monthMap.put("april", "April");
        monthMap.put("june", "June");
        monthMap.put("july", "July");
        monthMap.put("august", "August");
        monthMap.put("september", "September");
        monthMap.put("october", "October");
        monthMap.put("november", "November");
        monthMap.put("december", "December");

        // Check if month exists in map
        String standardizedMonth = monthMap.get(month);
        if (standardizedMonth != null) {
            return standardizedMonth;
        }

        // If not found, check if it's already in correct format (first letter uppercase)
        String capitalizedMonth = month.substring(0, 1).toUpperCase() + month.substring(1);
        if (monthMap.containsValue(capitalizedMonth)) {
            return capitalizedMonth;
        }

        throw new IllegalArgumentException("Invalid month format: " + monthValue + ". Use month name (January) or number (1-12)");
    }

    /**
     * Validate required request fields
     */
    private void validateRequiredFields(String eId, String salaryMonth, Integer salaryYear) {
        if (eId == null || eId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID (e_id) is required");
        }
        if (salaryMonth == null || salaryMonth.trim().isEmpty()) {
            throw new IllegalArgumentException("Salary month (salary_month) is required");
        }
        if (salaryYear == null) {
            throw new IllegalArgumentException("Salary year (salary_year) is required");
        }
        if (salaryYear < 2000 || salaryYear > 2050) {
            throw new IllegalArgumentException("Invalid salary year. Must be between 2000 and 2050");
        }
    }

    // ==================== API ENDPOINTS ====================

    /**
     * POST /payslip/isit - Check if payslip is generated (with standardized inputs)
     */
    @PostMapping("/isit")
    public ResponseEntity<Map<String, Object>> chkThatPayslipIsGenerated(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Safe parsing with helper methods
            String eId = safeParseString(request.get("e_id"), "e_id");
            String salaryMonth = standardizeMonth(request.get("salary_month"));
            Integer salaryYear = safeParseInteger(request.get("salary_year"), "salary_year");

            // Validate required fields
            validateRequiredFields(eId, salaryMonth, salaryYear);

            // Check if salary archive exists
            boolean payslipExists = salaryArchiveService.existsByeIdAndSalaryMonthAndSalaryYear(eId, salaryMonth, salaryYear);

            if (payslipExists) {
                response.put("success", true);
                response.put("message", "Payslip for Employee ID " + eId + " is generated for " + salaryMonth + ", " + salaryYear + ".");
                response.put("result", Map.of("payslip", true));
            } else {
                response.put("success", true);
                response.put("message", "Payslip for Employee ID " + eId + " is NOT generated for " + salaryMonth + ", " + salaryYear + ".");
                response.put("result", Map.of("payslip", false));
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("result", null);
            return ResponseEntity.status(400).body(response);
        } catch (Exception err) {
            System.err.println("Database query error: " + err.getMessage());
            response.put("success", false);
            response.put("message", "Internal server error");
            response.put("result", err.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * GET /payslip/isit/:e_id - Check if employee ID exists
     */
    @GetMapping("/isit/{e_id}")
    public ResponseEntity<Map<String, Object>> chkEmpIdExist(@PathVariable("e_id") String eId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate employee ID format
            if (eId == null || eId.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Employee ID is required");
                response.put("result", Map.of("e_id", false));
                return ResponseEntity.status(400).body(response);
            }

            boolean empExists = empDetailsService.getEmployeeById(eId.trim()).isPresent();

            if (empExists) {
                response.put("success", true);
                response.put("message", "Employee ID " + eId + " is valid.");
                response.put("result", Map.of("e_id", true));
            } else {
                response.put("success", true);
                response.put("message", "Employee ID " + eId + " is not valid.");
                response.put("result", Map.of("e_id", false));
            }

            return ResponseEntity.ok(response);

        } catch (Exception err) {
            System.err.println("Database query error: " + err.getMessage());
            response.put("success", false);
            response.put("message", "Internal server error");
            response.put("result", err.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * PUT /payslip/create_payslip - Generate payslip with calculations
     */
    @PutMapping("/create_payslip")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateInEarningAndSaveInArchive(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Extract and validate data sections
            Map<String, Object> salaryDetails = (Map<String, Object>) request.get("salary_details");
            Map<String, Object> empDeductionDetails = (Map<String, Object>) request.get("emp_deduction_details");
            Map<String, Object> empEarningDetails = (Map<String, Object>) request.get("emp_earning_details");

            if (salaryDetails == null || empDeductionDetails == null || empEarningDetails == null) {
                response.put("success", false);
                response.put("message", "Invalid request data. Required: salary_details, emp_deduction_details, emp_earning_details");
                response.put("result", null);
                return ResponseEntity.status(400).body(response);
            }

            // Safe parsing with helper methods
            String eId = safeParseString(salaryDetails.get("e_id"), "e_id");
            String salaryMonth = standardizeMonth(salaryDetails.get("salary_month"));
            Integer salaryYear = safeParseInteger(salaryDetails.get("salary_year"), "salary_year");
            String eName = safeParseString(empEarningDetails.get("e_name"), "e_name");

            // Validate required fields
            validateRequiredFields(eId, salaryMonth, salaryYear);

            if (eName == null || eName.trim().isEmpty()) {
                throw new IllegalArgumentException("Employee name (e_name) is required");
            }

            // Check if employee exists
            if (!empDetailsService.getEmployeeById(eId).isPresent()) {
                response.put("success", false);
                response.put("message", "Employee not found: " + eId);
                response.put("result", null);
                return ResponseEntity.status(404).body(response);
            }

            // Check if payslip already exists
            if (salaryArchiveService.existsByeIdAndSalaryMonthAndSalaryYear(eId, salaryMonth, salaryYear)) {
                response.put("success", false);
                response.put("message", "Payslip already exists for " + eId + " in " + salaryMonth + ", " + salaryYear);
                response.put("result", null);
                return ResponseEntity.status(409).body(response);
            }

            LocalDateTime now = LocalDateTime.now();

            // Update emp_deduction_details
            Optional<EmpDeductionDetails> deductionOpt = empDeductionDetailsService.getDeductionDetailsById(eId);
            if (deductionOpt.isPresent()) {
                EmpDeductionDetails deduction = deductionOpt.get();

                deduction.setLeaveDays(0);
                deduction.setLeaveDeductionAmount(0);
                deduction.setDeductionCPF(safeParseInteger(empDeductionDetails.get("deduction_CPF"), "deduction_CPF"));
                deduction.setGis(safeParseInteger(empDeductionDetails.get("GIS"), "GIS"));
                deduction.setHouseRent(safeParseInteger(empDeductionDetails.get("house_rent"), "house_rent"));
                deduction.setWaterCharges(safeParseInteger(empDeductionDetails.get("water_charges"), "water_charges"));
                deduction.setElectricityCharges(safeParseInteger(empDeductionDetails.get("electricity_charges"), "electricity_charges"));
                deduction.setVehicleDeduction(safeParseInteger(empDeductionDetails.get("vehicle_deduction"), "vehicle_deduction"));
                deduction.setHbLoan(safeParseInteger(empDeductionDetails.get("HB_loan"), "HB_loan"));
                deduction.setGpfLoan(safeParseInteger(empDeductionDetails.get("GPF_loan"), "GPF_loan"));
                deduction.setFestivalLoan(safeParseInteger(empDeductionDetails.get("festival_loan"), "festival_loan"));
                deduction.setGrainCharges(safeParseInteger(empDeductionDetails.get("grain_charges"), "grain_charges"));
                deduction.setBankAdvance(safeParseInteger(empDeductionDetails.get("bank_advance"), "bank_advance"));
                deduction.setAdvance(safeParseInteger(empDeductionDetails.get("advance"), "advance"));
                deduction.setRgpvAdvance(safeParseInteger(empDeductionDetails.get("RGPV_advance"), "RGPV_advance"));
                deduction.setIncomeTax(safeParseInteger(empDeductionDetails.get("income_tax"), "income_tax"));
                deduction.setProfessionalTax(safeParseInteger(empDeductionDetails.get("professional_tax"), "professional_tax"));

                empDeductionDetailsService.updateDeductionDetails(deduction);
            }

            // Update emp_earning_details
            Optional<EmpEarningDetails> earningOpt = empEarningDetailsService.getEarningDetailsById(eId);
            if (earningOpt.isPresent()) {
                EmpEarningDetails earning = earningOpt.get();

                earning.setBasicSalary(safeParseInteger(empEarningDetails.get("basic_salary"), "basic_salary"));
                earning.setSpecialPay(safeParseInteger(empEarningDetails.get("special_pay"), "special_pay"));
                earning.setDearnessAllowance(safeParseInteger(empEarningDetails.get("dearness_allowance"), "dearness_allowance"));
                earning.setDa(safeParseInteger(empEarningDetails.get("DA"), "DA"));
                earning.setAda(safeParseInteger(empEarningDetails.get("ADA"), "ADA"));
                earning.setInterimRelief(safeParseInteger(empEarningDetails.get("interim_relief"), "interim_relief"));
                earning.setHra(safeParseInteger(empEarningDetails.get("HRA"), "HRA"));
                earning.setCca(safeParseInteger(empEarningDetails.get("CCA"), "CCA"));
                earning.setConveyance(safeParseInteger(empEarningDetails.get("conveyance"), "conveyance"));
                earning.setMedical(safeParseInteger(empEarningDetails.get("medical"), "medical"));
                earning.setWashingAllowance(safeParseInteger(empEarningDetails.get("washing_allowance"), "washing_allowance"));
                earning.setBdp(safeParseInteger(empEarningDetails.get("BDP"), "BDP"));
                earning.setArrears(safeParseInteger(empEarningDetails.get("arrears"), "arrears"));

                empEarningDetailsService.updateEarningDetails(earning);
            }

            // Calculate totals (ensuring no null values)
            Integer totalEarning = Optional.ofNullable(safeParseInteger(empEarningDetails.get("basic_salary"), "basic_salary")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empEarningDetails.get("special_pay"), "special_pay")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empEarningDetails.get("dearness_allowance"), "dearness_allowance")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empEarningDetails.get("DA"), "DA")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empEarningDetails.get("ADA"), "ADA")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empEarningDetails.get("interim_relief"), "interim_relief")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empEarningDetails.get("HRA"), "HRA")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empEarningDetails.get("CCA"), "CCA")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empEarningDetails.get("conveyance"), "conveyance")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empEarningDetails.get("medical"), "medical")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empEarningDetails.get("washing_allowance"), "washing_allowance")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empEarningDetails.get("BDP"), "BDP")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empEarningDetails.get("arrears"), "arrears")).orElse(0);

            Integer totalDeduction = Optional.ofNullable(safeParseInteger(empDeductionDetails.get("leave_deduction_amount"), "leave_deduction_amount")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("deduction_CPF"), "deduction_CPF")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("GIS"), "GIS")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("house_rent"), "house_rent")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("water_charges"), "water_charges")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("electricity_charges"), "electricity_charges")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("vehicle_deduction"), "vehicle_deduction")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("HB_loan"), "HB_loan")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("GPF_loan"), "GPF_loan")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("festival_loan"), "festival_loan")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("grain_charges"), "grain_charges")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("bank_advance"), "bank_advance")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("advance"), "advance")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("RGPV_advance"), "RGPV_advance")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("income_tax"), "income_tax")).orElse(0) +
                    Optional.ofNullable(safeParseInteger(empDeductionDetails.get("professional_tax"), "professional_tax")).orElse(0);

            Integer netPayable = totalEarning - totalDeduction;

            // Create salary archive record
            SalaryArchive archive = new SalaryArchive();
            archive.setEId(eId);
            archive.setSalaryMonth(salaryMonth);
            archive.setSalaryYear(salaryYear);
            archive.setEName(eName);
            archive.setPayslipIssueDate(now);

            // Set all earning fields (with null safety)
            archive.setBasicSalary(Optional.ofNullable(safeParseInteger(empEarningDetails.get("basic_salary"), "basic_salary")).orElse(0));
            archive.setSpecialPay(Optional.ofNullable(safeParseInteger(empEarningDetails.get("special_pay"), "special_pay")).orElse(0));
            archive.setDearnessAllowance(Optional.ofNullable(safeParseInteger(empEarningDetails.get("dearness_allowance"), "dearness_allowance")).orElse(0));
            archive.setDa(Optional.ofNullable(safeParseInteger(empEarningDetails.get("DA"), "DA")).orElse(0));
            archive.setAda(Optional.ofNullable(safeParseInteger(empEarningDetails.get("ADA"), "ADA")).orElse(0));
            archive.setInterimRelief(Optional.ofNullable(safeParseInteger(empEarningDetails.get("interim_relief"), "interim_relief")).orElse(0));
            archive.setHra(Optional.ofNullable(safeParseInteger(empEarningDetails.get("HRA"), "HRA")).orElse(0));
            archive.setCca(Optional.ofNullable(safeParseInteger(empEarningDetails.get("CCA"), "CCA")).orElse(0));
            archive.setConveyance(Optional.ofNullable(safeParseInteger(empEarningDetails.get("conveyance"), "conveyance")).orElse(0));
            archive.setMedical(Optional.ofNullable(safeParseInteger(empEarningDetails.get("medical"), "medical")).orElse(0));
            archive.setWashingAllowance(Optional.ofNullable(safeParseInteger(empEarningDetails.get("washing_allowance"), "washing_allowance")).orElse(0));
            archive.setBdp(Optional.ofNullable(safeParseInteger(empEarningDetails.get("BDP"), "BDP")).orElse(0));
            archive.setArrears(Optional.ofNullable(safeParseInteger(empEarningDetails.get("arrears"), "arrears")).orElse(0));

            // Set all deduction fields (with null safety)
            archive.setLeaveDays(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("leave_days"), "leave_days")).orElse(0));
            archive.setLeaveDeductionAmount(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("leave_deduction_amount"), "leave_deduction_amount")).orElse(0));
            archive.setDeductionCPF(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("deduction_CPF"), "deduction_CPF")).orElse(0));
            archive.setGis(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("GIS"), "GIS")).orElse(0));
            archive.setHouseRent(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("house_rent"), "house_rent")).orElse(0));
            archive.setWaterCharges(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("water_charges"), "water_charges")).orElse(0));
            archive.setElectricityCharges(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("electricity_charges"), "electricity_charges")).orElse(0));
            archive.setVehicleDeduction(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("vehicle_deduction"), "vehicle_deduction")).orElse(0));
            archive.setHbLoan(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("HB_loan"), "HB_loan")).orElse(0));
            archive.setGpfLoan(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("GPF_loan"), "GPF_loan")).orElse(0));
            archive.setFestivalLoan(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("festival_loan"), "festival_loan")).orElse(0));
            archive.setGrainCharges(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("grain_charges"), "grain_charges")).orElse(0));
            archive.setBankAdvance(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("bank_advance"), "bank_advance")).orElse(0));
            archive.setAdvance(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("advance"), "advance")).orElse(0));
            archive.setRgpvAdvance(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("RGPV_advance"), "RGPV_advance")).orElse(0));
            archive.setIncomeTax(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("income_tax"), "income_tax")).orElse(0));
            archive.setProfessionalTax(Optional.ofNullable(safeParseInteger(empDeductionDetails.get("professional_tax"), "professional_tax")).orElse(0));

            // Set calculated totals
            archive.setTotalEarning(totalEarning);
            archive.setTotalDeduction(totalDeduction);
            archive.setNetPayable(netPayable);

            // Save to salary archive
            salaryArchiveService.saveSalaryRecord(archive);

            // EXACT response format from Node.js
            response.put("success", true);
            response.put("message", "Employee salary details updated and archived successfully.");
            response.put("result", Map.of(
                    "e_id", eId,
                    "salary_month", salaryMonth,
                    "salary_year", salaryYear,
                    "total_earning", totalEarning,
                    "total_deduction", totalDeduction,
                    "net_payable", netPayable
            ));

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("result", null);
            return ResponseEntity.status(400).body(response);
        } catch (Exception err) {
            System.err.println("Error updating salary details: " + err.getMessage());
            err.printStackTrace();
            response.put("success", false);
            response.put("message", "Internal server error");
            response.put("result", err.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * POST /payslip/get_pdf - Get data for PDF generation (with standardized inputs)
     */
    @PostMapping("/get_pdf")
    public ResponseEntity<Map<String, Object>> getDataForPdf(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Safe parsing with helper methods
            String eId = safeParseString(request.get("e_id"), "e_id");
            String salaryMonth = standardizeMonth(request.get("salary_month"));
            Integer salaryYear = safeParseInteger(request.get("salary_year"), "salary_year");

            // Validate required fields
            validateRequiredFields(eId, salaryMonth, salaryYear);

            // Fetch employee details
            Optional<EmpDetails> empDetailsOpt = empDetailsService.getEmployeeById(eId);

            if (!empDetailsOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Employee not found.");
                response.put("result", null);
                return ResponseEntity.ok(response);
            }

            EmpDetails empDetails = empDetailsOpt.get();

            // Fetch department details
            Optional<DeptDetails> deptDetailsOpt = Optional.empty();
            if (empDetails.getDepartment() != null) {
                deptDetailsOpt = deptDetailsService.getDepartmentById(empDetails.getDepartment().getDId());
            }

            // Fetch bank details
            Optional<EmpBankDetails> bankDetailsOpt = empBankDetailsService.getBankDetailsById(eId);

            // Fetch salary archive details (simplified approach)
            Optional<SalaryArchive> salaryDetailsOpt = salaryArchiveService.getSalaryRecordsByMonthAndYear(salaryMonth, salaryYear)
                    .stream()
                    .filter(record -> eId.equals(record.getEId()))
                    .findFirst();

            if (!salaryDetailsOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "No salary record found for " + salaryMonth + ", " + salaryYear + ".");
                response.put("result", null);
                return ResponseEntity.ok(response);
            }

            // EXACT response format from Node.js
            Map<String, Object> result = new HashMap<>();
            result.put("emp_details", empDetails);
            result.put("dept_details", deptDetailsOpt.orElse(null));
            result.put("bank_details", bankDetailsOpt.orElse(null));
            result.put("salary_details", salaryDetailsOpt.get());

            response.put("success", true);
            response.put("message", "Data fetched successfully.");
            response.put("result", result);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("result", null);
            return ResponseEntity.status(400).body(response);
        } catch (Exception err) {
            System.err.println("Error fetching data: " + err.getMessage());
            err.printStackTrace();
            response.put("success", false);
            response.put("message", "Internal server error.");
            response.put("result", err.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * POST /payslip/send_email - Send PDF to email
     */
    @PostMapping("/send_email")
    public ResponseEntity<Map<String, Object>> sendPdfToEmail(
            @RequestParam("file") MultipartFile file,
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam(value = "text", required = false) String text) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Validate required fields
            if (to == null || to.trim().isEmpty() || subject == null || subject.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Missing required fields!");
                return ResponseEntity.status(400).body(response);
            }

            // Validate email format
            Pattern emailPattern = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
            if (!emailPattern.matcher(to).matches()) {
                response.put("success", false);
                response.put("message", "Invalid email address format!");
                return ResponseEntity.status(400).body(response);
            }

            // Ensure file is uploaded
            if (file == null || file.isEmpty()) {
                response.put("success", false);
                response.put("message", "PDF file is required!");
                return ResponseEntity.status(400).body(response);
            }

            // Create and send email using MimeMessageHelper
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text != null ? text : "Please find the attached payslip.");

            // Add PDF attachment
            helper.addAttachment(file.getOriginalFilename(), file);

            // Send email
            emailSender.send(message);

            response.put("success", true);
            response.put("message", "Email with PDF attachment sent successfully!");

            return ResponseEntity.ok(response);

        } catch (Exception error) {
            System.err.println("Error sending email: " + error.getMessage());
            response.put("success", false);
            response.put("message", "Failed to send email: " + error.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
