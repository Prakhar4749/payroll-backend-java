package project.payroll_backend_java.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "emp_deduction_details")
public class EmpDeductionDetails {

    @Id
    @Column(name = "e_id", length = 5, nullable = false)
    private String eId;

    @Column(name = "e_name", length = 30, nullable = false)
    private String eName;

    @Column(name = "leave_days", nullable = false)
    private Integer leaveDays;

    @Column(name = "leave_deduction_amount", nullable = false)
    private Integer leaveDeductionAmount;

    @Column(name = "deduction_CPF", nullable = false)
    private Integer deductionCPF;

    @Column(name = "GIS", nullable = false)
    private Integer gis;

    @Column(name = "house_rent", nullable = false)
    private Integer houseRent;

    @Column(name = "water_charges", nullable = false)
    private Integer waterCharges;

    @Column(name = "electricity_charges", nullable = false)
    private Integer electricityCharges;

    @Column(name = "vehicle_deduction", nullable = false)
    private Integer vehicleDeduction;

    @Column(name = "HB_loan", nullable = false)
    private Integer hbLoan;

    @Column(name = "GPF_loan", nullable = false)
    private Integer gpfLoan;

    @Column(name = "festival_loan", nullable = false)
    private Integer festivalLoan;

    @Column(name = "grain_charges", nullable = false)
    private Integer grainCharges;

    @Column(name = "bank_advance", nullable = false)
    private Integer bankAdvance;

    @Column(name = "advance", nullable = false)
    private Integer advance;

    @Column(name = "RGPV_advance", nullable = false)
    private Integer rgpvAdvance;

    @Column(name = "income_tax", nullable = false)
    private Integer incomeTax;

    @Column(name = "professional_tax", nullable = false)
    private Integer professionalTax;

    // ADD THIS RELATIONSHIP - This is the key addition!
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "e_id")
    @JsonIgnore
    private EmpDetails empDetails;

    // Constructors
    public EmpDeductionDetails() {}

    public EmpDeductionDetails(String eId, String eName) {
        this.eId = eId;
        this.eName = eName;
        // Initialize all integer fields with 0
        this.leaveDays = 0;
        this.leaveDeductionAmount = 0;
        this.deductionCPF = 0;
        this.gis = 0;
        this.houseRent = 0;
        this.waterCharges = 0;
        this.electricityCharges = 0;
        this.vehicleDeduction = 0;
        this.hbLoan = 0;
        this.gpfLoan = 0;
        this.festivalLoan = 0;
        this.grainCharges = 0;
        this.bankAdvance = 0;
        this.advance = 0;
        this.rgpvAdvance = 0;
        this.incomeTax = 0;
        this.professionalTax = 0;
    }

    // All Getters and Setters
    public String getEId() { return eId; }
    public void setEId(String eId) { this.eId = eId; }

    public String getEName() { return eName; }
    public void setEName(String eName) { this.eName = eName; }

    public Integer getLeaveDays() { return leaveDays; }
    public void setLeaveDays(Integer leaveDays) { this.leaveDays = leaveDays; }

    public Integer getLeaveDeductionAmount() { return leaveDeductionAmount; }
    public void setLeaveDeductionAmount(Integer leaveDeductionAmount) { this.leaveDeductionAmount = leaveDeductionAmount; }

    public Integer getDeductionCPF() { return deductionCPF; }
    public void setDeductionCPF(Integer deductionCPF) { this.deductionCPF = deductionCPF; }

    public Integer getGis() { return gis; }
    public void setGis(Integer gis) { this.gis = gis; }

    public Integer getHouseRent() { return houseRent; }
    public void setHouseRent(Integer houseRent) { this.houseRent = houseRent; }

    public Integer getWaterCharges() { return waterCharges; }
    public void setWaterCharges(Integer waterCharges) { this.waterCharges = waterCharges; }

    public Integer getElectricityCharges() { return electricityCharges; }
    public void setElectricityCharges(Integer electricityCharges) { this.electricityCharges = electricityCharges; }

    public Integer getVehicleDeduction() { return vehicleDeduction; }
    public void setVehicleDeduction(Integer vehicleDeduction) { this.vehicleDeduction = vehicleDeduction; }

    public Integer getHbLoan() { return hbLoan; }
    public void setHbLoan(Integer hbLoan) { this.hbLoan = hbLoan; }

    public Integer getGpfLoan() { return gpfLoan; }
    public void setGpfLoan(Integer gpfLoan) { this.gpfLoan = gpfLoan; }

    public Integer getFestivalLoan() { return festivalLoan; }
    public void setFestivalLoan(Integer festivalLoan) { this.festivalLoan = festivalLoan; }

    public Integer getGrainCharges() { return grainCharges; }
    public void setGrainCharges(Integer grainCharges) { this.grainCharges = grainCharges; }

    public Integer getBankAdvance() { return bankAdvance; }
    public void setBankAdvance(Integer bankAdvance) { this.bankAdvance = bankAdvance; }

    public Integer getAdvance() { return advance; }
    public void setAdvance(Integer advance) { this.advance = advance; }

    public Integer getRgpvAdvance() { return rgpvAdvance; }
    public void setRgpvAdvance(Integer rgpvAdvance) { this.rgpvAdvance = rgpvAdvance; }

    public Integer getIncomeTax() { return incomeTax; }
    public void setIncomeTax(Integer incomeTax) { this.incomeTax = incomeTax; }

    public Integer getProfessionalTax() { return professionalTax; }
    public void setProfessionalTax(Integer professionalTax) { this.professionalTax = professionalTax; }

    // NEW GETTER/SETTER FOR RELATIONSHIP
    public EmpDetails getEmpDetails() { return empDetails; }
    public void setEmpDetails(EmpDetails empDetails) { this.empDetails = empDetails; }
}
