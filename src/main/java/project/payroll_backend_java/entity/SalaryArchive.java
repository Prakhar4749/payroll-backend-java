package project.payroll_backend_java.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_archive")
@IdClass(SalaryArchiveId.class)
public class SalaryArchive {

    @Id
    @Column(name = "e_id")
    private String eId;

    @Id
    @Column(name = "salary_month", length = 10)
    private String salaryMonth;

    @Id
    @Column(name = "salary_year")
    private Integer salaryYear;

    @Column(name = "e_name", length = 30, nullable = false)
    private String eName;

    @Column(name = "payslip_issue_date", nullable = false)
    private LocalDateTime payslipIssueDate;

    // All earning fields
    @Column(name = "basic_salary", nullable = false)
    private Integer basicSalary;

    @Column(name = "special_pay")
    private Integer specialPay;

    @Column(name = "dearness_allowance")
    private Integer dearnessAllowance;

    @Column(name = "DA")
    private Integer da;

    @Column(name = "ADA")
    private Integer ada;

    @Column(name = "interim_relief")
    private Integer interimRelief;

    @Column(name = "HRA")
    private Integer hra;

    @Column(name = "CCA")
    private Integer cca;

    @Column(name = "conveyance")
    private Integer conveyance;

    @Column(name = "medical")
    private Integer medical;

    @Column(name = "washing_allowance")
    private Integer washingAllowance;

    @Column(name = "BDP")
    private Integer bdp;

    @Column(name = "arrears")
    private Integer arrears;

    // All deduction fields
    @Column(name = "leave_days")
    private Integer leaveDays;

    @Column(name = "leave_deduction_amount")
    private Integer leaveDeductionAmount;

    @Column(name = "deduction_CPF")
    private Integer deductionCPF;

    @Column(name = "GIS")
    private Integer gis;

    @Column(name = "house_rent")
    private Integer houseRent;

    @Column(name = "water_charges")
    private Integer waterCharges;

    @Column(name = "electricity_charges")
    private Integer electricityCharges;

    @Column(name = "vehicle_deduction")
    private Integer vehicleDeduction;

    @Column(name = "HB_loan")
    private Integer hbLoan;

    @Column(name = "GPF_loan")
    private Integer gpfLoan;

    @Column(name = "festival_loan")
    private Integer festivalLoan;

    @Column(name = "grain_charges")
    private Integer grainCharges;

    @Column(name = "bank_advance")
    private Integer bankAdvance;

    @Column(name = "advance")
    private Integer advance;

    @Column(name = "RGPV_advance")
    private Integer rgpvAdvance;

    @Column(name = "income_tax")
    private Integer incomeTax;

    @Column(name = "professional_tax")
    private Integer professionalTax;

    // Calculated fields
    @Column(name = "total_earning")
    private Integer totalEarning;

    @Column(name = "total_deduction")
    private Integer totalDeduction;

    @Column(name = "net_payable")
    private Integer netPayable;

    // Constructors
    public SalaryArchive() {}

    // Getters and Setters for ALL fields
    public String getEId() { return eId; }
    public void setEId(String eId) { this.eId = eId; }

    public String getSalaryMonth() { return salaryMonth; }
    public void setSalaryMonth(String salaryMonth) { this.salaryMonth = salaryMonth; }

    public Integer getSalaryYear() { return salaryYear; }
    public void setSalaryYear(Integer salaryYear) { this.salaryYear = salaryYear; }

    public String getEName() { return eName; }
    public void setEName(String eName) { this.eName = eName; }

    public LocalDateTime getPayslipIssueDate() { return payslipIssueDate; }
    public void setPayslipIssueDate(LocalDateTime payslipIssueDate) { this.payslipIssueDate = payslipIssueDate; }

    // Earning fields getters/setters
    public Integer getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Integer basicSalary) { this.basicSalary = basicSalary; }

    public Integer getSpecialPay() { return specialPay; }
    public void setSpecialPay(Integer specialPay) { this.specialPay = specialPay; }

    public Integer getDearnessAllowance() { return dearnessAllowance; }
    public void setDearnessAllowance(Integer dearnessAllowance) { this.dearnessAllowance = dearnessAllowance; }

    public Integer getDa() { return da; }
    public void setDa(Integer da) { this.da = da; }

    public Integer getAda() { return ada; }
    public void setAda(Integer ada) { this.ada = ada; }

    public Integer getInterimRelief() { return interimRelief; }
    public void setInterimRelief(Integer interimRelief) { this.interimRelief = interimRelief; }

    public Integer getHra() { return hra; }
    public void setHra(Integer hra) { this.hra = hra; }

    public Integer getCca() { return cca; }
    public void setCca(Integer cca) { this.cca = cca; }

    public Integer getConveyance() { return conveyance; }
    public void setConveyance(Integer conveyance) { this.conveyance = conveyance; }

    public Integer getMedical() { return medical; }
    public void setMedical(Integer medical) { this.medical = medical; }

    public Integer getWashingAllowance() { return washingAllowance; }
    public void setWashingAllowance(Integer washingAllowance) { this.washingAllowance = washingAllowance; }

    public Integer getBdp() { return bdp; }
    public void setBdp(Integer bdp) { this.bdp = bdp; }

    public Integer getArrears() { return arrears; }
    public void setArrears(Integer arrears) { this.arrears = arrears; }

    // Deduction fields getters/setters
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

    // Calculated fields getters/setters
    public Integer getTotalEarning() { return totalEarning; }
    public void setTotalEarning(Integer totalEarning) { this.totalEarning = totalEarning; }

    public Integer getTotalDeduction() { return totalDeduction; }
    public void setTotalDeduction(Integer totalDeduction) { this.totalDeduction = totalDeduction; }

    public Integer getNetPayable() { return netPayable; }
    public void setNetPayable(Integer netPayable) { this.netPayable = netPayable; }
}
