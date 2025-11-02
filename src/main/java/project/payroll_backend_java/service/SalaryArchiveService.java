package project.payroll_backend_java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.payroll_backend_java.entity.SalaryArchive;
import project.payroll_backend_java.repository.SalaryArchiveRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class SalaryArchiveService {

    @Autowired
    private SalaryArchiveRepository salaryArchiveRepo;

    public Map<String, Object> checkEmployeeId(String e_id) {
        Map<String, Object> response = new HashMap<>();
        try {
            int exists = salaryArchiveRepo.checkEmployeeExists(e_id);

            Map<String, Object> result = new HashMap<>();
            result.put("e_id", exists > 0);

            response.put("success", true);
            response.put("message", exists > 0 ? "Employee ID found" : "Employee ID not found");
            response.put("result", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error checking employee ID: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    public Map<String, Object> checkPayslipInArchive(String e_id, String salary_month, Integer salary_year) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate input
            if (e_id == null || e_id.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "e_id cannot be empty");
                response.put("result", null);
                return response;
            }

            if (salary_month == null || salary_month.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "salary_month cannot be empty");
                response.put("result", null);
                return response;
            }

            if (salary_year == null) {
                response.put("success", false);
                response.put("message", "salary_year cannot be empty");
                response.put("result", null);
                return response;
            }

            // Normalize salary_month format
            salary_month = normalizeSalaryMonth(salary_month);

            int payslip = salaryArchiveRepo.checkPayslipExists(e_id, salary_month, salary_year);

            Map<String, Object> result = new HashMap<>();
            result.put("payslip", payslip > 0);

            if (payslip != 0) {
                response.put("success", true);
                response.put("message", "Payslip found in archive");
                response.put("result", result);
            } else {
                response.put("success", true);
                response.put("message", "Payslip not found in archive");
                response.put("result", result);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error checking payslip: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    public Map<String, Object> createSalaryArchive(Map<String, Object> payslipData) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Extract nested objects
            Map<String, Object> salaryDetails = (Map<String, Object>) payslipData.get("salary_details");
            Map<String, Object> earningDetails = (Map<String, Object>) payslipData.get("emp_earning_details");
            Map<String, Object> deductionDetails = (Map<String, Object>) payslipData.get("emp_deduction_details");

            // Validate nested objects exist
            if (salaryDetails == null || earningDetails == null || deductionDetails == null) {
                response.put("success", false);
                response.put("message", "Missing required data sections: salary_details, emp_earning_details, or emp_deduction_details");
                response.put("result", null);
                return response;
            }

            // Extract and validate required fields
            String e_id = (String) salaryDetails.get("e_id");
            String salary_month = (String) salaryDetails.get("salary_month");
            Integer salary_year = salaryDetails.get("salary_year") != null ?
                    Integer.valueOf(salaryDetails.get("salary_year").toString()) : null;
            String e_name = (String) earningDetails.get("e_name");

            // Normalize salary_month format
            salary_month = normalizeSalaryMonth(salary_month);

            // Validate required fields
            if (e_id == null || e_id.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "e_id cannot be empty");
                response.put("result", null);
                return response;
            }

            if (salary_month == null || salary_month.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "salary_month cannot be empty");
                response.put("result", null);
                return response;
            }

            if (salary_year == null) {
                response.put("success", false);
                response.put("message", "salary_year cannot be empty");
                response.put("result", null);
                return response;
            }

            if (e_name == null || e_name.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "e_name cannot be empty");
                response.put("result", null);
                return response;
            }

            // Check if payslip already exists
            int existingPayslip = salaryArchiveRepo.checkPayslipExists(e_id, salary_month, salary_year);

            if (existingPayslip != 0) {
                response.put("success", false);
                response.put("message", "Payslip already exists for this employee and period");
                response.put("result", null);
                return response;
            }

            // Get current timestamp for payslip_issue_date
            String payslip_issue_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Extract all earning components from emp_earning_details
            Integer basic_salary = getIntegerValue(earningDetails, "basic_salary");
            Integer special_pay = getIntegerValue(earningDetails, "special_pay");
            Integer dearness_allowance = getIntegerValue(earningDetails, "dearness_allowance");
            Integer DA = getIntegerValue(earningDetails, "DA");
            Integer ADA = getIntegerValue(earningDetails, "ADA");
            Integer interim_relief = getIntegerValue(earningDetails, "interim_relief");
            Integer HRA = getIntegerValue(earningDetails, "HRA");
            Integer CCA = getIntegerValue(earningDetails, "CCA");
            Integer conveyance = getIntegerValue(earningDetails, "conveyance");
            Integer medical = getIntegerValue(earningDetails, "medical");
            Integer washing_allowance = getIntegerValue(earningDetails, "washing_allowance");
            Integer BDP = getIntegerValue(earningDetails, "BDP");
            Integer arrears = getIntegerValue(earningDetails, "arrears");

            // Extract all deduction components from emp_deduction_details
            Integer leave_days = getIntegerValue(deductionDetails, "leave_days");
            Integer leave_deduction_amount = getIntegerValue(deductionDetails, "leave_deduction_amount");
            Integer deduction_CPF = getIntegerValue(deductionDetails, "deduction_CPF");
            Integer GIS = getIntegerValue(deductionDetails, "GIS");
            Integer house_rent = getIntegerValue(deductionDetails, "house_rent");
            Integer water_charges = getIntegerValue(deductionDetails, "water_charges");
            Integer electricity_charges = getIntegerValue(deductionDetails, "electricity_charges");
            Integer vehicle_deduction = getIntegerValue(deductionDetails, "vehicle_deduction");
            Integer HB_loan = getIntegerValue(deductionDetails, "HB_loan");
            Integer GPF_loan = getIntegerValue(deductionDetails, "GPF_loan");
            Integer festival_loan = getIntegerValue(deductionDetails, "festival_loan");
            Integer grain_charges = getIntegerValue(deductionDetails, "grain_charges");
            Integer bank_advance = getIntegerValue(deductionDetails, "bank_advance");
            Integer advance = getIntegerValue(deductionDetails, "advance");
            Integer RGPV_advance = getIntegerValue(deductionDetails, "RGPV_advance");
            Integer income_tax = getIntegerValue(deductionDetails, "income_tax");
            Integer professional_tax = getIntegerValue(deductionDetails, "professional_tax");

            // Calculate totals
            Integer total_earning = basic_salary + special_pay + dearness_allowance + DA + ADA +
                    interim_relief + HRA + CCA + conveyance + medical +
                    washing_allowance + BDP + arrears;

            Integer total_deduction = leave_deduction_amount + deduction_CPF + GIS + house_rent +
                    water_charges + electricity_charges + vehicle_deduction +
                    HB_loan + GPF_loan + festival_loan + grain_charges +
                    bank_advance + advance + RGPV_advance + income_tax + professional_tax;

            Integer net_payable = total_earning - total_deduction;

            // Create new payslip
            int result = salaryArchiveRepo.createPayslip(
                    e_id, salary_month, salary_year, e_name, payslip_issue_date,
                    basic_salary, special_pay, dearness_allowance, DA, ADA, interim_relief,
                    HRA, CCA, conveyance, medical, washing_allowance, BDP, arrears,
                    leave_days, leave_deduction_amount, deduction_CPF, GIS, house_rent,
                    water_charges, electricity_charges, vehicle_deduction, HB_loan, GPF_loan,
                    festival_loan, grain_charges, bank_advance, advance, RGPV_advance,
                    income_tax, professional_tax, total_earning, total_deduction, net_payable
            );

            if (result > 0) {
                SalaryArchive createdPayslip = salaryArchiveRepo.findPayslipByDetails(e_id, salary_month, salary_year);
                response.put("success", true);
                response.put("message", "Payslip created successfully");
                response.put("result", createdPayslip);
            } else {
                response.put("success", false);
                response.put("message", "Failed to create payslip");
                response.put("result", null);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating payslip: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    public Map<String, Object> getPayslip(String e_id, String salary_month, Integer salary_year) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate input
            if (e_id == null || e_id.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "e_id cannot be empty");
                response.put("result", null);
                return response;
            }

            if (salary_month == null || salary_month.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "salary_month cannot be empty");
                response.put("result", null);
                return response;
            }

            if (salary_year == null) {
                response.put("success", false);
                response.put("message", "salary_year cannot be empty");
                response.put("result", null);
                return response;
            }

            // Normalize salary_month format
            salary_month = normalizeSalaryMonth(salary_month);

            SalaryArchive payslip = salaryArchiveRepo.findPayslipByDetails(e_id, salary_month, salary_year);

            if (payslip != null) {
                response.put("success", true);
                response.put("message", "Payslip retrieved successfully");
                response.put("result", payslip);
            } else {
                response.put("success", false);
                response.put("message", "Payslip not found for employee " + e_id +
                        " for period " + salary_month + "/" + salary_year);
                response.put("result", null);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving payslip: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    // Helper method to safely extract Integer values from Map
    private Integer getIntegerValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return 0;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Helper method to normalize salary_month format
    private String normalizeSalaryMonth(String month) {
        if (month == null || month.trim().isEmpty()) {
            return null;
        }

        month = month.trim();

        // If it's a number (1-12), convert to month name
        try {
            int monthNum = Integer.parseInt(month);
            if (monthNum >= 1 && monthNum <= 12) {
                String[] months = {"January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"};
                return months[monthNum - 1];
            }
        } catch (NumberFormatException e) {
            // Not a number, continue with string processing
        }

        // Capitalize first letter and make rest lowercase
        return month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
    }
}