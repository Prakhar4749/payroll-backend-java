package project.payroll_backend_java.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.payroll_backend_java.entity.SalaryArchive;
import project.payroll_backend_java.entity.SalaryArchiveId;

@Repository
public interface SalaryArchiveRepository extends JpaRepository<SalaryArchive, SalaryArchiveId> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM emp_details WHERE e_id = :e_id LIMIT 1",
            nativeQuery = true)
    int checkEmployeeExists(@Param("e_id") String e_id);

    @Query(value = "SELECT * FROM salary_archive WHERE e_id = :e_id AND salary_month = :salary_month AND salary_year = :salary_year",
            nativeQuery = true)
    SalaryArchive findPayslipByDetails(@Param("e_id") String e_id,
                                       @Param("salary_month") String salary_month,
                                       @Param("salary_year") Integer salary_year);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO salary_archive (e_id, salary_month, salary_year, e_name, payslip_issue_date, " +
            "basic_salary, special_pay, dearness_allowance, DA, ADA, interim_relief, HRA, CCA, conveyance, " +
            "medical, washing_allowance, BDP, arrears, leave_days, leave_deduction_amount, deduction_CPF, " +
            "GIS, house_rent, water_charges, electricity_charges, vehicle_deduction, HB_loan, GPF_loan, " +
            "festival_loan, grain_charges, bank_advance, advance, RGPV_advance, income_tax, professional_tax, " +
            "total_earning, total_deduction, net_payable) " +
            "VALUES (:e_id, :salary_month, :salary_year, :e_name, :payslip_issue_date, " +
            ":basic_salary, :special_pay, :dearness_allowance, :DA, :ADA, :interim_relief, :HRA, :CCA, :conveyance, " +
            ":medical, :washing_allowance, :BDP, :arrears, :leave_days, :leave_deduction_amount, :deduction_CPF, " +
            ":GIS, :house_rent, :water_charges, :electricity_charges, :vehicle_deduction, :HB_loan, :GPF_loan, " +
            ":festival_loan, :grain_charges, :bank_advance, :advance, :RGPV_advance, :income_tax, :professional_tax, " +
            ":total_earning, :total_deduction, :net_payable)",
            nativeQuery = true)
    int createPayslip(@Param("e_id") String e_id,
                      @Param("salary_month") String salary_month,
                      @Param("salary_year") Integer salary_year,
                      @Param("e_name") String e_name,
                      @Param("payslip_issue_date") String payslip_issue_date,
                      @Param("basic_salary") Integer basic_salary,
                      @Param("special_pay") Integer special_pay,
                      @Param("dearness_allowance") Integer dearness_allowance,
                      @Param("DA") Integer DA,
                      @Param("ADA") Integer ADA,
                      @Param("interim_relief") Integer interim_relief,
                      @Param("HRA") Integer HRA,
                      @Param("CCA") Integer CCA,
                      @Param("conveyance") Integer conveyance,
                      @Param("medical") Integer medical,
                      @Param("washing_allowance") Integer washing_allowance,
                      @Param("BDP") Integer BDP,
                      @Param("arrears") Integer arrears,
                      @Param("leave_days") Integer leave_days,
                      @Param("leave_deduction_amount") Integer leave_deduction_amount,
                      @Param("deduction_CPF") Integer deduction_CPF,
                      @Param("GIS") Integer GIS,
                      @Param("house_rent") Integer house_rent,
                      @Param("water_charges") Integer water_charges,
                      @Param("electricity_charges") Integer electricity_charges,
                      @Param("vehicle_deduction") Integer vehicle_deduction,
                      @Param("HB_loan") Integer HB_loan,
                      @Param("GPF_loan") Integer GPF_loan,
                      @Param("festival_loan") Integer festival_loan,
                      @Param("grain_charges") Integer grain_charges,
                      @Param("bank_advance") Integer bank_advance,
                      @Param("advance") Integer advance,
                      @Param("RGPV_advance") Integer RGPV_advance,
                      @Param("income_tax") Integer income_tax,
                      @Param("professional_tax") Integer professional_tax,
                      @Param("total_earning") Integer total_earning,
                      @Param("total_deduction") Integer total_deduction,
                      @Param("net_payable") Integer net_payable);
}