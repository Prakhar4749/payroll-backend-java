package project.payroll_backend_java.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.payroll_backend_java.entity.EmpDetails;

import java.util.List;
import java.util.Map;

@Repository
public interface EmployeeRepository extends JpaRepository<EmpDetails, String> {

    // ===========================
    // FETCH OPERATIONS
    // ===========================

    /**
     * Fetch all employees with basic details only (for listing)
     * Corresponds to: get_all_basic__emp_details()
     */
    @Query(value = "SELECT e_id, e_name, e_mobile_number, e_email, e_address, e_designation " +
            "FROM emp_details ORDER BY e_id ASC", nativeQuery = true)
    List<Map<String, Object>> findAllBasicEmployees();

    /**
     * Fetch all employees (full details)
     */
    @Query(value = "SELECT * FROM emp_details ORDER BY e_id ASC", nativeQuery = true)
    List<EmpDetails> findAllEmployees();

    /**
     * Fetch employee by ID
     * Corresponds to: get_all_e_id_emp_details()
     */
    @Query(value = "SELECT * FROM emp_details WHERE e_id = :e_id", nativeQuery = true)
    EmpDetails findEmployeeById(@Param("e_id") String e_id);

    /**
     * Fetch employee bank details by ID
     */
    @Query(value = "SELECT * FROM emp_bank_details WHERE e_id = :e_id", nativeQuery = true)
    Map<String, Object> findEmployeeBankDetails(@Param("e_id") String e_id);

    /**
     * Fetch employee deduction details by ID
     */
    @Query(value = "SELECT * FROM emp_deduction_details WHERE e_id = :e_id", nativeQuery = true)
    Map<String, Object> findEmployeeDeductionDetails(@Param("e_id") String e_id);

    /**
     * Fetch employee earning details by ID
     */
    @Query(value = "SELECT * FROM emp_earning_details WHERE e_id = :e_id", nativeQuery = true)
    Map<String, Object> findEmployeeEarningDetails(@Param("e_id") String e_id);

    /**
     * Fetch department details by department ID
     */
    @Query(value = "SELECT * FROM dept_details WHERE d_id = :d_id", nativeQuery = true)
    Map<String, Object> findDepartmentById(@Param("d_id") String d_id);

    /**
     * Get the latest employee ID for auto-increment
     */
    @Query(value = "SELECT e_id FROM emp_details ORDER BY e_id DESC LIMIT 1", nativeQuery = true)
    String findLatestEmployeeId();

    // ===========================
    // VALIDATION OPERATIONS
    // ===========================

    /**
     * Check if mobile, bank account, or PAN exists (for add new employee)
     * Corresponds to: check_for_data()
     */
    @Query(value = "SELECT " +
            "COALESCE(SUM(CASE WHEN ed.e_mobile_number = :mobile THEN 1 ELSE 0 END), 0) AS mobile_count, " +
            "COALESCE(SUM(CASE WHEN ebd.e_bank_acc_number = :bank THEN 1 ELSE 0 END), 0) AS bank_count, " +
            "COALESCE(SUM(CASE WHEN ebd.e_pan_number = :pan THEN 1 ELSE 0 END), 0) AS pan_count " +
            "FROM emp_details ed " +
            "LEFT JOIN emp_bank_details ebd ON ed.e_id = ebd.e_id", nativeQuery = true)
    Map<String, Object> checkEmployeeExistsForAdd(
            @Param("mobile") String mobile,
            @Param("bank") String bank,
            @Param("pan") String pan
    );

    /**
     * Check if mobile exists (excluding specific employee ID)
     */
    @Query(value = "SELECT e_id FROM emp_details WHERE e_mobile_number = :mobile", nativeQuery = true)
    List<String> findByMobileNumber(@Param("mobile") String mobile);

    /**
     * Check if bank account exists (excluding specific employee ID)
     */
    @Query(value = "SELECT e_id FROM emp_bank_details WHERE e_bank_acc_number = :bank", nativeQuery = true)
    List<String> findByBankAccount(@Param("bank") String bank);

    /**
     * Check if PAN exists (excluding specific employee ID)
     */
    @Query(value = "SELECT e_id FROM emp_bank_details WHERE e_pan_number = :pan", nativeQuery = true)
    List<String> findByPanNumber(@Param("pan") String pan);

    /**
     * Validate department ID and fetch department name
     * Corresponds to: check_for_data() and chk_for_update()
     */
    @Query(value = "SELECT d_id, d_name FROM dept_details WHERE d_id = :d_id", nativeQuery = true)
    Map<String, Object> validateDepartmentId(@Param("d_id") String d_id);

    // ===========================
    // INSERT OPERATIONS
    // ===========================

    /**
     * Insert new employee basic details
     * Corresponds to: add_new_emp()
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO emp_details (e_id, e_name, e_mobile_number, e_gender, e_email, e_address, " +
            "e_photo, d_id, e_designation, e_group, e_date_of_joining, e_DOB) " +
            "VALUES (:e_id, :e_name, :e_mobile_number, :e_gender, :e_email, :e_address, " +
            ":e_photo, :d_id, :e_designation, :e_group, :e_date_of_joining, :e_DOB)", nativeQuery = true)
    int addEmployee(
            @Param("e_id") String e_id,
            @Param("e_name") String e_name,
            @Param("e_mobile_number") String e_mobile_number,
            @Param("e_gender") String e_gender,
            @Param("e_email") String e_email,
            @Param("e_address") String e_address,
            @Param("e_photo") String e_photo,
            @Param("d_id") String d_id,
            @Param("e_designation") String e_designation,
            @Param("e_group") String e_group,
            @Param("e_date_of_joining") String e_date_of_joining,
            @Param("e_DOB") String e_DOB
    );

    /**
     * Insert employee bank details
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO emp_bank_details (e_id, e_name, e_bank_name, e_bank_acc_number, " +
            "e_pan_number, e_bank_IFSC, e_cpf_or_gpf_number) " +
            "VALUES (:e_id, :e_name, :e_bank_name, :e_bank_acc_number, :e_pan_number, " +
            ":e_bank_IFSC, :e_cpf_or_gpf_number)", nativeQuery = true)
    int addEmployeeBank(
            @Param("e_id") String e_id,
            @Param("e_name") String e_name,
            @Param("e_bank_name") String e_bank_name,
            @Param("e_bank_acc_number") String e_bank_acc_number,
            @Param("e_pan_number") String e_pan_number,
            @Param("e_bank_IFSC") String e_bank_IFSC,
            @Param("e_cpf_or_gpf_number") String e_cpf_or_gpf_number
    );

    /**
     * Insert employee deduction details
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO emp_deduction_details (e_id, e_name, leave_days, leave_deduction_amount, " +
            "deduction_CPF, GIS, house_rent, water_charges, electricity_charges, vehicle_deduction, " +
            "HB_loan, GPF_loan, festival_loan, grain_charges, bank_advance, advance, RGPV_advance, " +
            "income_tax, professional_tax) " +
            "VALUES (:e_id, :e_name, :leave_days, :leave_deduction_amount, :deduction_CPF, :GIS, " +
            ":house_rent, :water_charges, :electricity_charges, :vehicle_deduction, :HB_loan, " +
            ":GPF_loan, :festival_loan, :grain_charges, :bank_advance, :advance, :RGPV_advance, " +
            ":income_tax, :professional_tax)", nativeQuery = true)
    int addEmployeeDeduction(
            @Param("e_id") String e_id,
            @Param("e_name") String e_name,
            @Param("leave_days") int leave_days,
            @Param("leave_deduction_amount") int leave_deduction_amount,
            @Param("deduction_CPF") int deduction_CPF,
            @Param("GIS") int GIS,
            @Param("house_rent") int house_rent,
            @Param("water_charges") int water_charges,
            @Param("electricity_charges") int electricity_charges,
            @Param("vehicle_deduction") int vehicle_deduction,
            @Param("HB_loan") int HB_loan,
            @Param("GPF_loan") int GPF_loan,
            @Param("festival_loan") int festival_loan,
            @Param("grain_charges") int grain_charges,
            @Param("bank_advance") int bank_advance,
            @Param("advance") int advance,
            @Param("RGPV_advance") int RGPV_advance,
            @Param("income_tax") int income_tax,
            @Param("professional_tax") int professional_tax
    );

    /**
     * Insert employee earning details
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO emp_earning_details (e_id, e_name, basic_salary, special_pay, " +
            "dearness_allowance, DA, ADA, interim_relief, HRA, CCA, conveyance, medical, " +
            "washing_allowance, BDP, arrears) " +
            "VALUES (:e_id, :e_name, :basic_salary, :special_pay, :dearness_allowance, :DA, :ADA, " +
            ":interim_relief, :HRA, :CCA, :conveyance, :medical, :washing_allowance, :BDP, :arrears)",
            nativeQuery = true)
    int addEmployeeEarning(
            @Param("e_id") String e_id,
            @Param("e_name") String e_name,
            @Param("basic_salary") int basic_salary,
            @Param("special_pay") int special_pay,
            @Param("dearness_allowance") int dearness_allowance,
            @Param("DA") int DA,
            @Param("ADA") int ADA,
            @Param("interim_relief") int interim_relief,
            @Param("HRA") int HRA,
            @Param("CCA") int CCA,
            @Param("conveyance") int conveyance,
            @Param("medical") int medical,
            @Param("washing_allowance") int washing_allowance,
            @Param("BDP") int BDP,
            @Param("arrears") int arrears
    );

    // ===========================
    // UPDATE OPERATIONS
    // ===========================

    /**
     * Update employee basic details
     * Corresponds to: update_emp()
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE emp_details SET " +
            "e_name = :e_name, e_mobile_number = :e_mobile_number, e_gender = :e_gender, " +
            "e_email = :e_email, e_address = :e_address, e_photo = :e_photo, d_id = :d_id, " +
            "e_designation = :e_designation, e_group = :e_group, e_date_of_joining = :e_date_of_joining, " +
            "e_DOB = :e_DOB " +
            "WHERE e_id = :e_id", nativeQuery = true)
    int updateEmployee(
            @Param("e_id") String e_id,
            @Param("e_name") String e_name,
            @Param("e_mobile_number") String e_mobile_number,
            @Param("e_gender") String e_gender,
            @Param("e_email") String e_email,
            @Param("e_address") String e_address,
            @Param("e_photo") String e_photo,
            @Param("d_id") String d_id,
            @Param("e_designation") String e_designation,
            @Param("e_group") String e_group,
            @Param("e_date_of_joining") String e_date_of_joining,
            @Param("e_DOB") String e_DOB
    );

    /**
     * Update employee bank details
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE emp_bank_details SET " +
            "e_name = :e_name, e_bank_name = :e_bank_name, e_bank_acc_number = :e_bank_acc_number, " +
            "e_pan_number = :e_pan_number, e_bank_IFSC = :e_bank_IFSC, " +
            "e_cpf_or_gpf_number = :e_cpf_or_gpf_number " +
            "WHERE e_id = :e_id", nativeQuery = true)
    int updateEmployeeBank(
            @Param("e_id") String e_id,
            @Param("e_name") String e_name,
            @Param("e_bank_name") String e_bank_name,
            @Param("e_bank_acc_number") String e_bank_acc_number,
            @Param("e_pan_number") String e_pan_number,
            @Param("e_bank_IFSC") String e_bank_IFSC,
            @Param("e_cpf_or_gpf_number") String e_cpf_or_gpf_number
    );

    /**
     * Update employee deduction details
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE emp_deduction_details SET " +
            "e_name = :e_name, leave_days = :leave_days, leave_deduction_amount = :leave_deduction_amount, " +
            "deduction_CPF = :deduction_CPF, GIS = :GIS, house_rent = :house_rent, " +
            "water_charges = :water_charges, electricity_charges = :electricity_charges, " +
            "vehicle_deduction = :vehicle_deduction, HB_loan = :HB_loan, GPF_loan = :GPF_loan, " +
            "festival_loan = :festival_loan, grain_charges = :grain_charges, bank_advance = :bank_advance, " +
            "advance = :advance, RGPV_advance = :RGPV_advance, income_tax = :income_tax, " +
            "professional_tax = :professional_tax " +
            "WHERE e_id = :e_id", nativeQuery = true)
    int updateEmployeeDeduction(
            @Param("e_id") String e_id,
            @Param("e_name") String e_name,
            @Param("leave_days") int leave_days,
            @Param("leave_deduction_amount") int leave_deduction_amount,
            @Param("deduction_CPF") int deduction_CPF,
            @Param("GIS") int GIS,
            @Param("house_rent") int house_rent,
            @Param("water_charges") int water_charges,
            @Param("electricity_charges") int electricity_charges,
            @Param("vehicle_deduction") int vehicle_deduction,
            @Param("HB_loan") int HB_loan,
            @Param("GPF_loan") int GPF_loan,
            @Param("festival_loan") int festival_loan,
            @Param("grain_charges") int grain_charges,
            @Param("bank_advance") int bank_advance,
            @Param("advance") int advance,
            @Param("RGPV_advance") int RGPV_advance,
            @Param("income_tax") int income_tax,
            @Param("professional_tax") int professional_tax
    );

    /**
     * Update employee earning details
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE emp_earning_details SET " +
            "e_name = :e_name, basic_salary = :basic_salary, special_pay = :special_pay, " +
            "dearness_allowance = :dearness_allowance, DA = :DA, ADA = :ADA, " +
            "interim_relief = :interim_relief, HRA = :HRA, CCA = :CCA, conveyance = :conveyance, " +
            "medical = :medical, washing_allowance = :washing_allowance, BDP = :BDP, arrears = :arrears " +
            "WHERE e_id = :e_id", nativeQuery = true)
    int updateEmployeeEarning(
            @Param("e_id") String e_id,
            @Param("e_name") String e_name,
            @Param("basic_salary") int basic_salary,
            @Param("special_pay") int special_pay,
            @Param("dearness_allowance") int dearness_allowance,
            @Param("DA") int DA,
            @Param("ADA") int ADA,
            @Param("interim_relief") int interim_relief,
            @Param("HRA") int HRA,
            @Param("CCA") int CCA,
            @Param("conveyance") int conveyance,
            @Param("medical") int medical,
            @Param("washing_allowance") int washing_allowance,
            @Param("BDP") int BDP,
            @Param("arrears") int arrears
    );

    // ===========================
    // DELETE OPERATIONS
    // ===========================

    /**
     * Delete employee from bank details
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM emp_bank_details WHERE e_id = :e_id", nativeQuery = true)
    int deleteEmployeeBankDetails(@Param("e_id") String e_id);

    /**
     * Delete employee from deduction details
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM emp_deduction_details WHERE e_id = :e_id", nativeQuery = true)
    int deleteEmployeeDeductionDetails(@Param("e_id") String e_id);

    /**
     * Delete employee from earning details
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM emp_earning_details WHERE e_id = :e_id", nativeQuery = true)
    int deleteEmployeeEarningDetails(@Param("e_id") String e_id);

    /**
     * Delete employee from main details table
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM emp_details WHERE e_id = :e_id", nativeQuery = true)
    int deleteEmployeeMainDetails(@Param("e_id") String e_id);
}