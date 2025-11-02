package project.payroll_backend_java.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import project.payroll_backend_java.entity.SalaryArchiveId;

@Entity
@Table(name = "salary_archive")
@IdClass(SalaryArchiveId.class)

@NoArgsConstructor
@AllArgsConstructor
public class SalaryArchive {

    @Id
    @Column(name = "e_id")
    private String e_id;

    @Id
    @Column(name = "salary_month", length = 10)
    private String salary_month;

    @Id
    @Column(name = "salary_year")
    private Integer salary_year;

    @Column(name = "e_name", length = 30, nullable = false)
    private String e_name;

    @Column(name = "payslip_issue_date", nullable = false)
    private LocalDateTime payslip_issue_date;

    // All earning fields
    @Column(name = "basic_salary", nullable = false)
    private Integer basic_salary;

    @Column(name = "special_pay")
    private Integer special_pay;

    @Column(name = "dearness_allowance")
    private Integer dearness_allowance;

    @Column(name = "DA")
    private Integer DA;

    @Column(name = "ADA")
    private Integer ADA;

    @Column(name = "interim_relief")
    private Integer interim_relief;

    @Column(name = "HRA")
    private Integer HRA;

    @Column(name = "CCA")
    private Integer CCA;

    @Column(name = "conveyance")
    private Integer conveyance;

    @Column(name = "medical")
    private Integer medical;

    @Column(name = "washing_allowance")
    private Integer washing_allowance;

    @Column(name = "BDP")
    private Integer BDP;

    @Column(name = "arrears")
    private Integer arrears;

    // All deduction fields
    @Column(name = "leave_days")
    private Integer leave_days;

    @Column(name = "leave_deduction_amount")
    private Integer leave_deduction_amount;

    @Column(name = "deduction_CPF")
    private Integer deduction_CPF;

    @Column(name = "GIS")
    private Integer GIS;

    @Column(name = "house_rent")
    private Integer house_rent;

    @Column(name = "water_charges")
    private Integer water_charges;

    @Column(name = "electricity_charges")
    private Integer electricity_charges;

    @Column(name = "vehicle_deduction")
    private Integer vehicle_deduction;

    @Column(name = "HB_loan")
    private Integer HB_loan;

    @Column(name = "GPF_loan")
    private Integer GPF_loan;

    @Column(name = "festival_loan")
    private Integer festival_loan;

    @Column(name = "grain_charges")
    private Integer grain_charges;

    @Column(name = "bank_advance")
    private Integer bank_advance;

    @Column(name = "advance")
    private Integer advance;

    @Column(name = "RGPV_advance")
    private Integer RGPV_advance;

    @Column(name = "income_tax")
    private Integer income_tax;

    @Column(name = "professional_tax")
    private Integer professional_tax;

    // Calculated fields
    @Column(name = "total_earning")
    private Integer total_earning;

    @Column(name = "total_deduction")
    private Integer total_deduction;

    @Column(name = "net_payable")
    private Integer net_payable;

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    public String getSalary_month() {
        return salary_month;
    }

    public void setSalary_month(String salary_month) {
        this.salary_month = salary_month;
    }

    public Integer getSalary_year() {
        return salary_year;
    }

    public void setSalary_year(Integer salary_year) {
        this.salary_year = salary_year;
    }

    public String getE_name() {
        return e_name;
    }

    public void setE_name(String e_name) {
        this.e_name = e_name;
    }

    public LocalDateTime getPayslip_issue_date() {
        return payslip_issue_date;
    }

    public void setPayslip_issue_date(LocalDateTime payslip_issue_date) {
        this.payslip_issue_date = payslip_issue_date;
    }

    public Integer getBasic_salary() {
        return basic_salary;
    }

    public void setBasic_salary(Integer basic_salary) {
        this.basic_salary = basic_salary;
    }

    public Integer getSpecial_pay() {
        return special_pay;
    }

    public void setSpecial_pay(Integer special_pay) {
        this.special_pay = special_pay;
    }

    public Integer getDearness_allowance() {
        return dearness_allowance;
    }

    public void setDearness_allowance(Integer dearness_allowance) {
        this.dearness_allowance = dearness_allowance;
    }

    public Integer getDA() {
        return DA;
    }

    public void setDA(Integer DA) {
        this.DA = DA;
    }

    public Integer getADA() {
        return ADA;
    }

    public void setADA(Integer ADA) {
        this.ADA = ADA;
    }

    public Integer getInterim_relief() {
        return interim_relief;
    }

    public void setInterim_relief(Integer interim_relief) {
        this.interim_relief = interim_relief;
    }

    public Integer getHRA() {
        return HRA;
    }

    public void setHRA(Integer HRA) {
        this.HRA = HRA;
    }

    public Integer getCCA() {
        return CCA;
    }

    public void setCCA(Integer CCA) {
        this.CCA = CCA;
    }

    public Integer getConveyance() {
        return conveyance;
    }

    public void setConveyance(Integer conveyance) {
        this.conveyance = conveyance;
    }

    public Integer getMedical() {
        return medical;
    }

    public void setMedical(Integer medical) {
        this.medical = medical;
    }

    public Integer getWashing_allowance() {
        return washing_allowance;
    }

    public void setWashing_allowance(Integer washing_allowance) {
        this.washing_allowance = washing_allowance;
    }

    public Integer getBDP() {
        return BDP;
    }

    public void setBDP(Integer BDP) {
        this.BDP = BDP;
    }

    public Integer getArrears() {
        return arrears;
    }

    public void setArrears(Integer arrears) {
        this.arrears = arrears;
    }

    public Integer getLeave_days() {
        return leave_days;
    }

    public void setLeave_days(Integer leave_days) {
        this.leave_days = leave_days;
    }

    public Integer getLeave_deduction_amount() {
        return leave_deduction_amount;
    }

    public void setLeave_deduction_amount(Integer leave_deduction_amount) {
        this.leave_deduction_amount = leave_deduction_amount;
    }

    public Integer getDeduction_CPF() {
        return deduction_CPF;
    }

    public void setDeduction_CPF(Integer deduction_CPF) {
        this.deduction_CPF = deduction_CPF;
    }

    public Integer getGIS() {
        return GIS;
    }

    public void setGIS(Integer GIS) {
        this.GIS = GIS;
    }

    public Integer getHouse_rent() {
        return house_rent;
    }

    public void setHouse_rent(Integer house_rent) {
        this.house_rent = house_rent;
    }

    public Integer getWater_charges() {
        return water_charges;
    }

    public void setWater_charges(Integer water_charges) {
        this.water_charges = water_charges;
    }

    public Integer getElectricity_charges() {
        return electricity_charges;
    }

    public void setElectricity_charges(Integer electricity_charges) {
        this.electricity_charges = electricity_charges;
    }

    public Integer getVehicle_deduction() {
        return vehicle_deduction;
    }

    public void setVehicle_deduction(Integer vehicle_deduction) {
        this.vehicle_deduction = vehicle_deduction;
    }

    public Integer getHB_loan() {
        return HB_loan;
    }

    public void setHB_loan(Integer HB_loan) {
        this.HB_loan = HB_loan;
    }

    public Integer getGPF_loan() {
        return GPF_loan;
    }

    public void setGPF_loan(Integer GPF_loan) {
        this.GPF_loan = GPF_loan;
    }

    public Integer getFestival_loan() {
        return festival_loan;
    }

    public void setFestival_loan(Integer festival_loan) {
        this.festival_loan = festival_loan;
    }

    public Integer getGrain_charges() {
        return grain_charges;
    }

    public void setGrain_charges(Integer grain_charges) {
        this.grain_charges = grain_charges;
    }

    public Integer getBank_advance() {
        return bank_advance;
    }

    public void setBank_advance(Integer bank_advance) {
        this.bank_advance = bank_advance;
    }

    public Integer getAdvance() {
        return advance;
    }

    public void setAdvance(Integer advance) {
        this.advance = advance;
    }

    public Integer getRGPV_advance() {
        return RGPV_advance;
    }

    public void setRGPV_advance(Integer RGPV_advance) {
        this.RGPV_advance = RGPV_advance;
    }

    public Integer getIncome_tax() {
        return income_tax;
    }

    public void setIncome_tax(Integer income_tax) {
        this.income_tax = income_tax;
    }

    public Integer getProfessional_tax() {
        return professional_tax;
    }

    public void setProfessional_tax(Integer professional_tax) {
        this.professional_tax = professional_tax;
    }

    public Integer getTotal_earning() {
        return total_earning;
    }

    public void setTotal_earning(Integer total_earning) {
        this.total_earning = total_earning;
    }

    public Integer getTotal_deduction() {
        return total_deduction;
    }

    public void setTotal_deduction(Integer total_deduction) {
        this.total_deduction = total_deduction;
    }

    public Integer getNet_payable() {
        return net_payable;
    }

    public void setNet_payable(Integer net_payable) {
        this.net_payable = net_payable;
    }
}
