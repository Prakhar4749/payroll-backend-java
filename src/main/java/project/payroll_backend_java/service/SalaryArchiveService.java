package project.payroll_backend_java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.payroll_backend_java.entity.DeptDetails;
import project.payroll_backend_java.entity.EmpBankDetails;
import project.payroll_backend_java.entity.EmpDetails;
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

            // Fetch payslip from salary_archive
            SalaryArchive payslip = salaryArchiveRepo.findPayslipByDetails(e_id, salary_month, salary_year);

            if (payslip == null) {
                response.put("success", false);
                response.put("message", "Payslip not found for employee " + e_id +
                        " for period " + salary_month + "/" + salary_year);
                response.put("result", null);
                return response;
            }

            // Fetch employee details
            Map<String, Object> empDetails = salaryArchiveRepo.findEmpDetailsById(e_id);
            if (empDetails == null || empDetails.isEmpty()) {
                response.put("success", false);
                response.put("message", "Employee details not found for e_id: " + e_id);
                response.put("result", null);
                return response;
            }

            // Fetch department details
            Map<String, Object> deptDetails = salaryArchiveRepo.findDeptDetailsByEmpId(e_id);

            // Fetch bank details
            Map<String, Object> bankDetails = salaryArchiveRepo.findBankDetailsByEmpId(e_id);

            // Build salary_details from SalaryArchive entity
            Map<String, Object> salaryDetails = new HashMap<>();
            salaryDetails.put("e_id", payslip.getE_id());
            salaryDetails.put("salary_month", payslip.getSalary_month());
            salaryDetails.put("salary_year", payslip.getSalary_year());
            salaryDetails.put("e_name", payslip.getE_name());
            salaryDetails.put("payslip_issue_date", payslip.getPayslip_issue_date());

            // Earning details
            salaryDetails.put("basic_salary", payslip.getBasic_salary());
            salaryDetails.put("special_pay", payslip.getSpecial_pay());
            salaryDetails.put("dearness_allowance", payslip.getDearness_allowance());
            salaryDetails.put("DA", payslip.getDA());
            salaryDetails.put("ADA", payslip.getADA());
            salaryDetails.put("interim_relief", payslip.getInterim_relief());
            salaryDetails.put("HRA", payslip.getHRA());
            salaryDetails.put("CCA", payslip.getCCA());
            salaryDetails.put("conveyance", payslip.getConveyance());
            salaryDetails.put("medical", payslip.getMedical());
            salaryDetails.put("washing_allowance", payslip.getWashing_allowance());
            salaryDetails.put("BDP", payslip.getBDP());
            salaryDetails.put("arrears", payslip.getArrears());

            // Deduction details
            salaryDetails.put("leave_days", payslip.getLeave_days());
            salaryDetails.put("leave_deduction_amount", payslip.getLeave_deduction_amount());
            salaryDetails.put("deduction_CPF", payslip.getDeduction_CPF());
            salaryDetails.put("GIS", payslip.getGIS());
            salaryDetails.put("house_rent", payslip.getHouse_rent());
            salaryDetails.put("water_charges", payslip.getWater_charges());
            salaryDetails.put("electricity_charges", payslip.getElectricity_charges());
            salaryDetails.put("vehicle_deduction", payslip.getVehicle_deduction());
            salaryDetails.put("HB_loan", payslip.getHB_loan());
            salaryDetails.put("GPF_loan", payslip.getGPF_loan());
            salaryDetails.put("festival_loan", payslip.getFestival_loan());
            salaryDetails.put("grain_charges", payslip.getGrain_charges());
            salaryDetails.put("bank_advance", payslip.getBank_advance());
            salaryDetails.put("advance", payslip.getAdvance());
            salaryDetails.put("RGPV_advance", payslip.getRGPV_advance());
            salaryDetails.put("income_tax", payslip.getIncome_tax());
            salaryDetails.put("professional_tax", payslip.getProfessional_tax());

            // Totals
            salaryDetails.put("total_earning", payslip.getTotal_earning());
            salaryDetails.put("total_deduction", payslip.getTotal_deduction());
            salaryDetails.put("net_payable", payslip.getNet_payable());

            // Build the final result structure
            Map<String, Object> result = new HashMap<>();
            result.put("emp_details", empDetails);
            result.put("dept_details", deptDetails);
            result.put("bank_details", bankDetails);
            result.put("salary_details", salaryDetails);

            response.put("success", true);
            response.put("message", "Payslip retrieved successfully");
            response.put("result", result);

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

    // Helper method to map slary data
    private Map<String, Object> buildEmpDetailsMap(EmpDetails emp) {
        Map<String, Object> map = new HashMap<>();
        map.put("e_id", emp.getE_id());
        map.put("e_name", emp.getE_name());
        map.put("e_mobile_number", emp.getE_mobile_number());
        map.put("e_gender", emp.getE_gender());
        map.put("e_email", emp.getE_email());
        map.put("e_address", emp.getE_address());
        map.put("e_photo", emp.getE_photo());
        map.put("d_id", emp.getD_id());
        map.put("e_designation", emp.getE_designation());
        map.put("e_group", emp.getE_group());
        map.put("e_date_of_joining", emp.getE_date_of_joining());
        map.put("e_DOB", emp.getE_DOB());
        return map;
    }

    private Map<String, Object> buildDeptDetailsMap(DeptDetails dept) {
        Map<String, Object> map = new HashMap<>();
        map.put("d_id", dept.getD_id());
        map.put("d_name", dept.getD_name());
        return map;
    }

    private Map<String, Object> buildBankDetailsMap(EmpBankDetails bank) {
        Map<String, Object> map = new HashMap<>();
        map.put("e_id", bank.getE_id());
        map.put("e_name", bank.getE_name());
        map.put("e_bank_name", bank.getE_bank_name());
        map.put("e_bank_acc_number", bank.getE_bank_acc_number());
        map.put("e_pan_number", bank.getE_pan_number());
        map.put("e_bank_IFSC", bank.getE_bank_IFSC());
        map.put("e_cpf_or_gpf_number", bank.getE_cpf_or_gpf_number());
        return map;
    }

    private Map<String, Object> buildSalaryDetailsMap(SalaryArchive payslip) {
        Map<String, Object> map = new HashMap<>();
        map.put("e_id", payslip.getE_id());
        map.put("salary_month", payslip.getSalary_month());
        map.put("salary_year", payslip.getSalary_year());
        map.put("e_name", payslip.getE_name());
        map.put("payslip_issue_date", payslip.getPayslip_issue_date());

        // Earning details
        map.put("basic_salary", payslip.getBasic_salary());
        map.put("special_pay", payslip.getSpecial_pay());
        map.put("dearness_allowance", payslip.getDearness_allowance());
        map.put("DA", payslip.getDA());
        map.put("ADA", payslip.getADA());
        map.put("interim_relief", payslip.getInterim_relief());
        map.put("HRA", payslip.getHRA());
        map.put("CCA", payslip.getCCA());
        map.put("conveyance", payslip.getConveyance());
        map.put("medical", payslip.getMedical());
        map.put("washing_allowance", payslip.getWashing_allowance());
        map.put("BDP", payslip.getBDP());
        map.put("arrears", payslip.getArrears());

        // Deduction details
        map.put("leave_days", payslip.getLeave_days());
        map.put("leave_deduction_amount", payslip.getLeave_deduction_amount());
        map.put("deduction_CPF", payslip.getDeduction_CPF());
        map.put("GIS", payslip.getGIS());
        map.put("house_rent", payslip.getHouse_rent());
        map.put("water_charges", payslip.getWater_charges());
        map.put("electricity_charges", payslip.getElectricity_charges());
        map.put("vehicle_deduction", payslip.getVehicle_deduction());
        map.put("HB_loan", payslip.getHB_loan());
        map.put("GPF_loan", payslip.getGPF_loan());
        map.put("festival_loan", payslip.getFestival_loan());
        map.put("grain_charges", payslip.getGrain_charges());
        map.put("bank_advance", payslip.getBank_advance());
        map.put("advance", payslip.getAdvance());
        map.put("RGPV_advance", payslip.getRGPV_advance());
        map.put("income_tax", payslip.getIncome_tax());
        map.put("professional_tax", payslip.getProfessional_tax());

        // Totals
        map.put("total_earning", payslip.getTotal_earning());
        map.put("total_deduction", payslip.getTotal_deduction());
        map.put("net_payable", payslip.getNet_payable());

        return map;
    }


}