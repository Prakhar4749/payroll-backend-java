package project.payroll_backend_java.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "emp_deduction_details")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@NoArgsConstructor
@AllArgsConstructor
public class EmpDeductionDetails {

    @Id
    @Column(name = "e_id", length = 5, nullable = false)
    private String e_id;

    @Column(name = "e_name", length = 30, nullable = false)
    private String e_name;

    @Column(name = "leave_days", nullable = false)
    private Integer leave_days;

    @Column(name = "leave_deduction_amount", nullable = false)
    private Integer leave_deduction_amount;

    @Column(name = "deduction_CPF", nullable = false)
    private Integer deduction_CPF;

    @Column(name = "GIS", nullable = false)
    private Integer GIS;

    @Column(name = "house_rent", nullable = false)
    private Integer house_rent;

    @Column(name = "water_charges", nullable = false)
    private Integer water_charges;

    @Column(name = "electricity_charges", nullable = false)
    private Integer electricity_charges;

    @Column(name = "vehicle_deduction", nullable = false)
    private Integer vehicle_deduction;

    @Column(name = "HB_loan", nullable = false)
    private Integer HB_loan;

    @Column(name = "GPF_loan", nullable = false)
    private Integer GPF_loan;

    @Column(name = "festival_loan", nullable = false)
    private Integer festival_loan;

    @Column(name = "grain_charges", nullable = false)
    private Integer grain_charges;

    @Column(name = "bank_advance", nullable = false)
    private Integer bank_advance;

    @Column(name = "advance", nullable = false)
    private Integer advance;

    @Column(name = "RGPV_advance", nullable = false)
    private Integer RGPV_advance;

    @Column(name = "income_tax", nullable = false)
    private Integer income_tax;

    @Column(name = "professional_tax", nullable = false)
    private Integer professional_tax;

    // One-to-One relationship with EmpDetails (foreign key on same column e_id)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "e_id", referencedColumnName = "e_id", insertable = false, updatable = false)
    @JsonIgnore
    private EmpDetails emp_details;

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    public String getE_name() {
        return e_name;
    }

    public void setE_name(String e_name) {
        this.e_name = e_name;
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

    public EmpDetails getEmp_details() {
        return emp_details;
    }

    public void setEmp_details(EmpDetails emp_details) {
        this.emp_details = emp_details;
    }
}
